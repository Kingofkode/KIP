package com.fbu.kip.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fbu.kip.R;
import com.fbu.kip.Utils;
import com.fbu.kip.adapters.FriendRequestsAdapter;
import com.fbu.kip.databinding.ActivityAddFriendBinding;
import com.fbu.kip.models.FriendRequest;
import com.fbu.kip.models.Friendship;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

  private static final String TAG = "AddFriendActivity";

  ActivityAddFriendBinding binding;
  FriendRequestsAdapter adapter;
  List<ParseUser> allUsers;
  List<ParseUser> visibleUsers;
  List<ParseUser> friends;
  List<FriendRequest> incomingFriendRequests;
  List<FriendRequest> outgoingFriendRequests;

  boolean friendshipsLoaded = false;
  boolean incomingFriendRequestsLoaded = false;
  boolean outgoingFriendRequestsLoaded = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityAddFriendBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    queryIncomingFriendRequests();
    queryOutgoingFriendRequests();

    allUsers = new ArrayList<>();
    visibleUsers = new ArrayList<>();

    queryFriendships();

    adapter = new FriendRequestsAdapter(this, incomingFriendRequests, visibleUsers);
    binding.rvUsers.setAdapter(adapter);
    binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
    setupActionBar();
  }

  private void setupActionBar() {
    getSupportActionBar().setTitle("Add Friends");
  }

  private void queryFriendships() {
    friends = new ArrayList<>();
    ParseQuery<Friendship> friendships = ParseQuery.getQuery(Friendship.class);
    friendships.whereMatches(Friendship.KEY_USER_A, ParseUser.getCurrentUser().getObjectId());
    friendships.include(Friendship.KEY_USER_B);
    friendships.findInBackground(new FindCallback<Friendship>() {
      @Override
      public void done(List<Friendship> friendships, ParseException e) {
        for (Friendship friendship : friendships) {
          friends.add(friendship.getUserB());
        }
        friendshipsLoaded = true;
        showFriendSuggestionsIfReady();
      }
    });
  }

  private void queryIncomingFriendRequests() {
    incomingFriendRequests = new ArrayList<>();
    ParseQuery<FriendRequest> friendRequestQuery = ParseQuery.getQuery(FriendRequest.class);
    friendRequestQuery.include(FriendRequest.KEY_SENDER_ID);
    friendRequestQuery.whereMatches(FriendRequest.KEY_RECIPIENT_ID, ParseUser.getCurrentUser().getObjectId());

    friendRequestQuery.findInBackground(new FindCallback<FriendRequest>() {
      @Override
      public void done(List<FriendRequest> friendRequests, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue getting friend requests", e);
          return;
        }
        if (!friendRequests.isEmpty())
          binding.progressBar.setVisibility(View.GONE);
        incomingFriendRequests.addAll(friendRequests);
        adapter.notifyDataSetChanged();
        incomingFriendRequestsLoaded = true;
        showFriendSuggestionsIfReady();
      }
    });
  }

  private void queryOutgoingFriendRequests() {
    outgoingFriendRequests = new ArrayList<>();

    ParseQuery<FriendRequest> outgoingFriendReqQuery = ParseQuery.getQuery(FriendRequest.class);
    outgoingFriendReqQuery.include(FriendRequest.KEY_RECIPIENT_ID);
    outgoingFriendReqQuery.whereMatches(FriendRequest.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());

    outgoingFriendReqQuery.findInBackground(new FindCallback<FriendRequest>() {
      @Override
      public void done(List<FriendRequest> friendRequestList, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error querying outgoing friend requests: ", e);
          return;
        }
        outgoingFriendRequests.addAll(friendRequestList);
        outgoingFriendRequestsLoaded = true;
        showFriendSuggestionsIfReady();
      }
    });
  }

  private void showFriendSuggestionsIfReady() {
    if (friendshipsLoaded && incomingFriendRequestsLoaded && outgoingFriendRequestsLoaded) {
      binding.progressBar.setVisibility(View.GONE);
      loadAllUsers();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_add_friend, menu);

    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        updateVisibleUsers(query);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String query) {
        updateVisibleUsers(query);
        return false;
      }
    });

    return super.onCreateOptionsMenu(menu);
  }

  private void updateVisibleUsers(String query) {
    List<ParseUser> newVisibleUsers = filteredAndSortedUsers(allUsers, query, SortType.createdAt, true, ParseUser.getCurrentUser(), friends, incomingFriendRequests, outgoingFriendRequests);
    visibleUsers.clear();
    visibleUsers.addAll(newVisibleUsers);
    adapter.notifyDataSetChanged();
  }

  private void loadAllUsers() {
    ParseQuery<ParseUser> userQuery = ParseQuery.getQuery(ParseUser.class);
    userQuery.findInBackground(new FindCallback<ParseUser>() {
      @Override
      public void done(List<ParseUser> foundUsers, ParseException e) {
        allUsers = foundUsers;
        List<ParseUser> filteredUsers = filteredAndSortedUsers(allUsers, "", SortType.createdAt, true, ParseUser.getCurrentUser(), friends, incomingFriendRequests, outgoingFriendRequests);
        visibleUsers.clear();
        visibleUsers.addAll(filteredUsers);
        adapter.notifyDataSetChanged();
      }
    });
  }

  enum SortType {
    createdAt,
    updatedAt,
    name,
  }

  private List<ParseUser> filteredAndSortedUsers(List<ParseUser> inputUserList, String queryString, SortType sortType, final boolean ascending, ParseUser myself, List<ParseUser> myFriends, List<FriendRequest> incomingFriendRequests, List<FriendRequest> outgoingFriendRequests) {
    List<ParseUser> outputUserList = new ArrayList<>();
    // Filter
    for (ParseUser user : inputUserList) {
      if (!Utils.getFullName(user).contains(queryString)) // Skip if user's name doesn't even match the string the user is querying for
        continue;
      if (myself.getObjectId().equals(user.getObjectId())) // Filter out myself
        continue;
      if (containsUser(myFriends, user)) // Filter out my friends
        continue;
      if (hasSentFriendRequest(incomingFriendRequests, user, true)) // Filter out incoming friend requests
        continue;
      if (hasSentFriendRequest(outgoingFriendRequests, user, false)) // Filter out outgoing friend requests
        continue;
      outputUserList.add(user);
    }
    // Sort by when the user signed up
    class CreatedAtComparator implements Comparator<ParseUser> {
      @Override
      public int compare(ParseUser user1, ParseUser user2) {
        if (ascending)
          return user2.getCreatedAt().compareTo(user1.getCreatedAt());
        return user1.getCreatedAt().compareTo(user2.getCreatedAt());
      }
    }
    // Sort by when the user updated their profile
    class UpdatedAtComparator implements Comparator<ParseUser> {
      @Override
      public int compare(ParseUser user1, ParseUser user2) {
        if (ascending)
          return user2.getUpdatedAt().compareTo(user1.getUpdatedAt());
        return user1.getUpdatedAt().compareTo(user2.getUpdatedAt());
      }
    }
    // Sort alphabetically by name
    class NameComparator implements Comparator<ParseUser> {
      @Override
      public int compare(ParseUser user1, ParseUser user2) {
        if (ascending)
          return Utils.getFullName(user1).compareTo(Utils.getFullName(user2));
        return Utils.getFullName(user2).compareTo(Utils.getFullName(user1));
      }
    }
    // Determine which type was selected and apply one of the sorts above
    switch (sortType) {
      case createdAt:
        Collections.sort(outputUserList, new CreatedAtComparator());
        break;
      case updatedAt:
        Collections.sort(outputUserList, new UpdatedAtComparator());
        break;
      case name:
        Collections.sort(outputUserList, new NameComparator());
        break;
    }

    return outputUserList;
  }

  boolean containsUser(List<ParseUser> userList, ParseUser target) {
    for (ParseObject user : userList) {
      if (user.getObjectId().equals(target.getObjectId()))
        return true;
    }
    return false;
  }

  private boolean hasSentFriendRequest(List<FriendRequest> friendRequests, ParseUser target, boolean isIncoming) {
    // Filter out incoming friend requests
    for (FriendRequest friendRequest : friendRequests) {
      ParseUser subject = isIncoming ? friendRequest.getSender() : friendRequest.getRecipient();
      if (subject.getObjectId().equals(target.getObjectId()))
        return true;
    }
    return false;
  }

}
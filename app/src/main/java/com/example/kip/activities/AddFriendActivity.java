package com.example.kip.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kip.R;
import com.example.kip.adapters.FriendRequestsAdapter;
import com.example.kip.databinding.ActivityAddFriendBinding;
import com.example.kip.models.FriendRequest;
import com.example.kip.models.Friendship;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AddFriendActivity extends AppCompatActivity {

  private static final String TAG = "AddFriendActivity";

  ActivityAddFriendBinding binding;
  FriendRequestsAdapter adapter;
  List<FriendRequest> allFriendRequests;
  List<ParseUser> searchedUsers;
  List<ParseUser> friends;

  Boolean friendshipsLoaded = false;
  Boolean friendRequestsLoaded = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityAddFriendBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    allFriendRequests = new ArrayList<>();
    queryFriendRequests();

    searchedUsers = new ArrayList<>();
    friends = new ArrayList<>();
    queryFriendships();
    adapter = new FriendRequestsAdapter(this, allFriendRequests, searchedUsers);

    binding.rvUsers.setAdapter(adapter);
    binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
    setupActionBar();
  }

  private void setupActionBar() {
    getSupportActionBar().setTitle("Add Friends");
  }

  private void queryFriendships() {
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
        if (friendRequestsLoaded)
          queryUsers("");
      }
    });
  }

  private void queryFriendRequests() {
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

        for (FriendRequest friendRequest : friendRequests) {
          Log.i(TAG, "Friend request from: " + friendRequest.getSender().getUsername());
        }
        allFriendRequests.addAll(friendRequests);
        adapter.notifyDataSetChanged();
        friendRequestsLoaded = true;
        if (friendshipsLoaded)
          queryUsers("");

      }
    });
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
        queryUsers(query);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        queryUsers(newText);
        return false;
      }
    });

    return super.onCreateOptionsMenu(menu);
  }

  private void queryUsers(String query) {
    ParseQuery<ParseUser> userQuery = ParseQuery.getQuery(ParseUser.class);
    userQuery.whereContains("username", query);

    userQuery.findInBackground(new FindCallback<ParseUser>() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void done(List<ParseUser> foundUsers, ParseException e) {
        searchedUsers.clear();
        foundUsers.removeIf(new Predicate<ParseUser>() {
          @Override
          public boolean test(ParseUser parseUser) {
            // Filter out myself
            if (parseUser.getUsername().equals(ParseUser.getCurrentUser().getUsername()))
              return true;
            // Filter out my friends
            for (ParseUser friend : friends) {
              if (friend.getUsername().equals(parseUser.getUsername()))
                return true;
            }
            // Filter out friend requests
            for (FriendRequest friendRequest : allFriendRequests) {
              if (friendRequest.getSender().getUsername().equals(parseUser.getUsername()))
                return true;
            }
            return false;
          }
        });
        searchedUsers.addAll(foundUsers);
        adapter.notifyDataSetChanged();
      }
    });
  }


}
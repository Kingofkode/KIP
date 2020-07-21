package com.example.kip.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.kip.adapters.FriendRequestsAdapter;
import com.example.kip.databinding.ActivityAddFriendBinding;
import com.example.kip.models.FriendRequest;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

  private static final String TAG = "AddFriendActivity";

  ActivityAddFriendBinding binding;
  FriendRequestsAdapter adapter;
  ArrayList<FriendRequest> allFriendRequests;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityAddFriendBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    queryFriendRequests();
    allFriendRequests = new ArrayList<>();

    adapter = new FriendRequestsAdapter(this, allFriendRequests);

    binding.rvUsers.setAdapter(adapter);
    binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
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
      }
    });
  }
}
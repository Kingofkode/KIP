package com.example.kip.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.kip.adapters.UsersAdapter;
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
  UsersAdapter adapter;
  ArrayList<ParseUser> users;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityAddFriendBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    queryFriendRequests();
    users = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      ParseUser user1 = new ParseUser();
      user1.setUsername("Zuck");
      users.add(user1);
    }

    adapter = new UsersAdapter(this, users, false);

    binding.rvUsers.setAdapter(adapter);
    binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
  }

  private void queryFriendRequests() {
    ParseQuery<FriendRequest> friendRequestQuery = ParseQuery.getQuery(FriendRequest.class);
    friendRequestQuery.include(FriendRequest.KEY_SENDER_ID);

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
      }
    });
  }
}
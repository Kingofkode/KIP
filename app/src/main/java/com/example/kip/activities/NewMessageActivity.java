package com.example.kip.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kip.adapters.UsersAdapter;
import com.example.kip.databinding.ActivityNewMessageBinding;
import com.parse.ParseUser;

import java.util.ArrayList;

public class NewMessageActivity extends AppCompatActivity {

  private static final String TAG = "NewMessageActivity";

  ActivityNewMessageBinding binding;
  UsersAdapter adapter;
  ArrayList<ParseUser> friends;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityNewMessageBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    loadFriends();
    adapter = new UsersAdapter(this, friends, true);
    binding.rvFriends.setAdapter(adapter);
    binding.rvFriends.setLayoutManager(new LinearLayoutManager(this));

  }

  private void loadFriends() {
    friends = new ArrayList<>();
    // TODO: Actually load friends from Parse
    // Mock data
    for (int i = 0; i < 10; i++) {
      ParseUser user = new ParseUser();
      user.setUsername("Zuck");
      friends.add(user);
    }
  }
}
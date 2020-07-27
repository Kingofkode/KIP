package com.example.kip.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kip.adapters.UsersAdapter;
import com.example.kip.databinding.ActivityNewMessageBinding;
import com.example.kip.models.Friendship;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class NewMessageActivity extends AppCompatActivity {

  private static final String TAG = "NewMessageActivity";

  ActivityNewMessageBinding binding;
  UsersAdapter adapter;

  ArrayList<ParseUser> allFriends;
  ArrayList<ParseUser> visibleFriends;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityNewMessageBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    allFriends = new ArrayList<>();
    visibleFriends = new ArrayList<>();

    adapter = new UsersAdapter(this, visibleFriends, true);
    binding.rvFriends.setAdapter(adapter);
    binding.rvFriends.setLayoutManager(new LinearLayoutManager(this));

    binding.etRecipient.requestFocus();
    queryFriendships();
    setupTextWatcher();
    setupActionBar();
  }

  private void setupActionBar() {
    getSupportActionBar().setTitle("New Message");
  }

  private void setupTextWatcher() {
    binding.etRecipient.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        filterFriends(charSequence);
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
  }

  private void queryFriendships() {
    ParseQuery<Friendship> friendships = ParseQuery.getQuery(Friendship.class);
    friendships.whereMatches(Friendship.KEY_USER_A, ParseUser.getCurrentUser().getObjectId());
    friendships.include(Friendship.KEY_USER_B);
    friendships.findInBackground(new FindCallback<Friendship>() {
      @Override
      public void done(List<Friendship> friendships, ParseException e) {
        for (Friendship friendship : friendships) {
          allFriends.add(friendship.getUserB());
        }
        visibleFriends.addAll(allFriends);
        adapter.notifyDataSetChanged();
      }
    });
  }

  private void filterFriends(CharSequence query) {
    visibleFriends.clear();
    for (ParseUser friend : allFriends) {
      if (friend.getUsername().contains(query))
        visibleFriends.add(friend);
    }
    adapter.notifyDataSetChanged();
  }
}
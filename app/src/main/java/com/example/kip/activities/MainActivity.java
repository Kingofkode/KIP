package com.example.kip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.kip.fragments.ChatsFragment;
import com.example.kip.R;
import com.example.kip.fragments.SuggestionsFragment;
import com.example.kip.databinding.ActivityMainBinding;
import com.example.kip.models.Conversation;
import com.example.kip.models.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  final FragmentManager fragmentManager = getSupportFragmentManager();

  ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setupNavigationView();
  }

  private void setupNavigationView() {
    binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {
          case R.id.action_suggestions: // Suggestions tab
            fragment = new SuggestionsFragment();
            break;
          default: // Chats tab
            fragment = new ChatsFragment();
            break;
        }
        fragmentManager.beginTransaction().replace(binding.flContainer.getId(), fragment).commit();
        return true;
      }
    });
    // Set default selection
    binding.bottomNavigation.setSelectedItemId(R.id.action_chats);
  }

  // User presses profile button
  public void onProfileClick(View view) {
    Intent profileIntent = new Intent(this, ProfileActivity.class);
    startActivity(profileIntent);
  }

  // User presses add friend button
  public void onAddFriendClick(View view) {
    Intent addFriendIntent = new Intent(this, AddFriendActivity.class);
    startActivity(addFriendIntent);
  }

  // User presses new chat button
  public void onNewChatClick(View view) {
    Intent newChatIntent = new Intent(this, NewChatActivity.class);
    startActivity(newChatIntent);
  }

  private void queryConversations() {
    ParseQuery<Conversation> conversationQuery = ParseQuery.getQuery(Conversation.class);
    conversationQuery.include(Conversation.KEY_MEMBER_IDS);
    conversationQuery.include(Conversation.KEY_LAST_MESSAGE);

    conversationQuery.findInBackground(new FindCallback<Conversation>() {
      @Override
      public void done(List<Conversation> conversations, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with getting conversations", e);
          return;
        }

        for (Conversation conversation : conversations) {
          try {
            ParseUser sender = conversation.getLastMessage().getSender().fetchIfNeeded();
            Message message = conversation.getLastMessage();
            // Todo: Populate RecyclerView with data
          } catch (ParseException ex) {
            ex.printStackTrace();
          }
        }
      }
    });
  }
}
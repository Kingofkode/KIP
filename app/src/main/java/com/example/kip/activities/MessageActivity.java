package com.example.kip.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kip.adapters.MessagesAdapter;
import com.example.kip.databinding.ActivityMessageBinding;
import com.example.kip.models.Conversation;
import com.example.kip.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

  private static final String TAG = "MessageActivity";

  ActivityMessageBinding binding;
  MessagesAdapter adapter;
  ArrayList<Message> allMessages;
  ParseUser recipient;
  Conversation conversation;
  int oldMessagesSize = 0; // Used to determine if a new message has been received between polls
  Runnable mRefreshMessagesRunnable;
  Handler myHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMessageBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Get recipient
    recipient = Parcels.unwrap(getIntent().getParcelableExtra(ParseUser.class.getSimpleName()));

    // Setup recycler view
    allMessages = new ArrayList<>();

    adapter = new MessagesAdapter(this, allMessages);

    binding.rvMessages.setAdapter(adapter);

    binding.rvMessages.setLayoutManager(configureLayoutManager());

    setupKeyboardListener();
    fetchExistingConversation();
  }

  private LinearLayoutManager configureLayoutManager() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    return layoutManager;
  }

  private void fetchExistingConversation() {
    ParseQuery<Conversation> conversationParseQuery = ParseQuery.getQuery(Conversation.class);

    ArrayList<ParseUser> memberIDs = new ArrayList<>();
    memberIDs.add(recipient);
    memberIDs.add(ParseUser.getCurrentUser());

    conversationParseQuery.whereContainsAll(Conversation.KEY_MEMBER_IDS, memberIDs);
    conversationParseQuery.setLimit(1);

    conversationParseQuery.findInBackground(new FindCallback<Conversation>() {
      @Override
      public void done(List<Conversation> conversations, ParseException e) {
        // Should be 1 or 0
        if (e != null) {
          Log.e(TAG, "There was an issue with this query", e);
          return;
        }

        if (!conversations.isEmpty()) {
          conversation = conversations.get(0);
          refreshMessages();
        }
      }
    });
  }

  private void refreshMessages() {
    if (conversation == null)
      return;
    ParseQuery<Message> messageParseQuery = ParseQuery.getQuery(Message.class);
    messageParseQuery.whereMatches(Message.KEY_CONVERSATION_ID, conversation.getObjectId());
    messageParseQuery.addAscendingOrder(Message.KEY_CREATED_AT);
    // TODO: Set limit
    messageParseQuery.findInBackground(new FindCallback<Message>() {
      @Override
      public void done(List<Message> messages, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error fetching messages: ", e);
          return;
        }
        allMessages.clear();
        allMessages.addAll(messages);
        adapter.notifyDataSetChanged();
        if (allMessages.size() > oldMessagesSize) {
          binding.rvMessages.smoothScrollToPosition(allMessages.size() - 1);
        }
        oldMessagesSize = allMessages.size();
      }
    });
  }

  private void startPolling() {
    // Create a handler which can run code periodically
    final int POLL_INTERVAL = 1000; // milliseconds
    myHandler = new android.os.Handler();
    mRefreshMessagesRunnable = new Runnable() {
      @Override
      public void run() {
        Log.i(TAG, "Request update");
        refreshMessages();
        myHandler.postDelayed(this, POLL_INTERVAL);
      }
    };
    myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
  }

  private void stopPolling() {
    if (mRefreshMessagesRunnable != null && myHandler != null) {
      myHandler.removeCallbacks(mRefreshMessagesRunnable);
    }
  }

  // Scroll RecyclerView down when keyboard is presented
  private void setupKeyboardListener() {
    binding.rvMessages.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (bottom < oldBottom && !allMessages.isEmpty()) {
          binding.rvMessages.postDelayed(new Runnable() {
            @Override
            public void run() {
              binding.rvMessages.smoothScrollToPosition(allMessages.size() - 1);
            }
          }, 100);
        }
      }
    });
  }

  public void onSendClick(View view) {
    // Send message via Parse
    final Message newMessage = new Message();
    newMessage.setBody(binding.etMessage.getText().toString());
    newMessage.setSender(ParseUser.getCurrentUser());
    if (conversation == null) { // There was never a previous conversation
      conversation = createConversation();
    }

    newMessage.setConversation(conversation);
    binding.etMessage.setText("");

    newMessage.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error sending message: ", e);
          Toast.makeText(MessageActivity.this, "Error Sending Message", Toast.LENGTH_SHORT).show();
        }
        conversation.setLastMessage(newMessage);
        conversation.saveInBackground();
      }
    });
  }

  private Conversation createConversation() {
    Conversation conversation = new Conversation();
    ArrayList<ParseUser> members = new ArrayList<>();
    members.add(ParseUser.getCurrentUser());
    members.add(recipient);
    conversation.setMembers(members);
    return conversation;
  }

  @Override
  protected void onPause() {
    super.onPause();
    stopPolling();
  }

  @Override
  protected void onResume() {
    super.onResume();
    startPolling();
  }
}
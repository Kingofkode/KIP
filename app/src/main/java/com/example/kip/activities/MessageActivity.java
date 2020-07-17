package com.example.kip.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kip.adapters.MessagesAdapter;
import com.example.kip.databinding.ActivityMessageBinding;
import com.example.kip.models.Message;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

  private static final String TAG = "MessageActivity";

  ActivityMessageBinding binding;
  MessagesAdapter adapter;
  ArrayList<Message> messages;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMessageBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Setup recycler view
    messages = new ArrayList<>();
    inflateWithMockMessages();

    adapter = new MessagesAdapter(this, messages);

    binding.rvMessages.setAdapter(adapter);
    binding.rvMessages.setLayoutManager(new LinearLayoutManager(this));
    setupKeyboardListener();
  }

  private void inflateWithMockMessages() {
    // Test data
    Message message1 = new Message();
    message1.setBody("Hi Isaiah!");

    Message message2 = new Message();
    message2.setBody("Hi Zuck!");

    Message message3 = new Message();
    message3.setBody("Another");

    messages.add(message1);
    messages.add(message2);
    messages.add(message3);
  }

  // Scroll RecyclerView down when keyboard is presented
  private void setupKeyboardListener() {
    binding.rvMessages.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (bottom < oldBottom) {
          binding.rvMessages.postDelayed(new Runnable() {
            @Override
            public void run() {
              binding.rvMessages.smoothScrollToPosition(messages.size() - 1);
            }
          }, 100);
        }
      }
    });
  }

  public void onMessageProfileClick(View view) {
    Intent profileIntent = new Intent(this, ProfileActivity.class);
    // TODO: Pass along the profile to view
    this.startActivity(profileIntent);
  }

  public void onSendClick(View view) {
    // TODO: Actually send message via Parse
    Message newMessage = new Message();
    newMessage.setBody(binding.etMessage.getText().toString());
    messages.add(newMessage);
    int lastPosition = messages.size() - 1;
    adapter.notifyItemInserted(lastPosition);
    binding.rvMessages.smoothScrollToPosition(lastPosition);
  }
}
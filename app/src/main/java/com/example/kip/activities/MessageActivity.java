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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMessageBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Setup recycler view
    ArrayList<Message> messages = new ArrayList<>();
    Message message1 = new Message();
    message1.setBody("Hi Isaiah!");
    messages.add(message1);

    adapter = new MessagesAdapter(this, messages);

    binding.rvMessages.setAdapter(adapter);
    binding.rvMessages.setLayoutManager(new LinearLayoutManager(this));

  }

  public void onMessageProfileClick(View view) {
    Intent profileIntent = new Intent(this, ProfileActivity.class);
    // TODO: Pass along the profile to view
    this.startActivity(profileIntent);
  }
}
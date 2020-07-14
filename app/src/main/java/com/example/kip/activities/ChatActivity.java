package com.example.kip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kip.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

  private static final String TAG = "ChatActivity";

  ActivityChatBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityChatBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());


  }
}
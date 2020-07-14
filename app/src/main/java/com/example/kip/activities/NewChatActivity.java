package com.example.kip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kip.databinding.ActivityNewChatBinding;

public class NewChatActivity extends AppCompatActivity {

  private static final String TAG = "NewChatActivity";

  ActivityNewChatBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityNewChatBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());


  }
}
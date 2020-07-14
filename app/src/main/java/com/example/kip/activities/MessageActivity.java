package com.example.kip.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kip.databinding.ActivityMessageBinding;

public class MessageActivity extends AppCompatActivity {

  private static final String TAG = "MessageActivity";

  ActivityMessageBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMessageBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());


  }
}
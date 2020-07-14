package com.example.kip.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kip.databinding.ActivityNewMessageBinding;

public class NewMessageActivity extends AppCompatActivity {

  private static final String TAG = "NewMessageActivity";

  ActivityNewMessageBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityNewMessageBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());


  }
}
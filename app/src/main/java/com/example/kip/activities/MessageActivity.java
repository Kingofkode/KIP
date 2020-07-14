package com.example.kip.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

  public void onMessageProfileClick(View view) {
    Intent profileIntent = new Intent(this, ProfileActivity.class);
    // TODO: Pass along the profile to view
    this.startActivity(profileIntent);
  }
}
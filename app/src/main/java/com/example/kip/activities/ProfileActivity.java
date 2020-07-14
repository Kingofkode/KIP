package com.example.kip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kip.databinding.ActivityMainBinding;
import com.example.kip.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

  private static final String TAG = "ProfileActivity";

  ActivityProfileBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityProfileBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());


  }
}
package com.example.kip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
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
    populateWithSampleData();

  }
  private void populateWithSampleData() {
    // TODO: Replace with actual profile image
    Glide.with(this)
      .load("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/09/01/15/zuckprofpic.jpg?w968h681")
      .circleCrop()
      .into(binding.ivProfile);
  }
}


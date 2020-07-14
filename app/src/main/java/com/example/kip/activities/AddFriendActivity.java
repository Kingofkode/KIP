package com.example.kip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kip.databinding.ActivityAddFriendBinding;

public class AddFriendActivity extends AppCompatActivity {

    private static final String TAG = "AddFriendActivity";

    ActivityAddFriendBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
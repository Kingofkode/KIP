package com.example.kip.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kip.databinding.ActivityLoginBinding;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

  private static final String TAG = "LoginActivity";
  public static final int REGISTRATION_REQUEST_CODE = 4;

  private ActivityLoginBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Check if user is already logged in
    if (ParseUser.getCurrentUser() != null) {
      launchMainActivity();
    }

  }

  private void launchMainActivity() {
    Intent mainIntent = new Intent(this, MainActivity.class);
    startActivity(mainIntent);
    finish();
  }

  // Coming back from the sign up activity
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == REGISTRATION_REQUEST_CODE && resultCode == RESULT_OK) {
      // The user registered! Show main activity.
      Intent mainActivityIntent = new Intent(this, MainActivity.class);
      startActivity(mainActivityIntent);
      finish();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  // User pressed "Log In" button
  public void onLoginClick(View view) {
    // TODO: Actually login
    launchMainActivity();
  }

  // User pressed "Sign Up" button
  public void onRegisterClick(View view) {
    Intent registrationIntent = new Intent(this, RegistrationActivity.class);
    startActivityForResult(registrationIntent, REGISTRATION_REQUEST_CODE);
  }
}
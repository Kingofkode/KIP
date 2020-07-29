package com.fbu.kip.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fbu.kip.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
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

    setupTextWatchers();
  }

  // Enables / Disables login button depending on EditTexts
  private void setupTextWatchers() {
    TextWatcher textWatcher = new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        binding.btnLogin.setEnabled(canLogin());
      }
    };

    binding.etUsername.addTextChangedListener(textWatcher);
    binding.etPassword.addTextChangedListener(textWatcher);
  }


  private Boolean canLogin() {
    return !binding.etUsername.getText().toString().isEmpty() && !binding.etPassword.getText().toString().isEmpty();
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
    String username = binding.etUsername.getText().toString();
    String password = binding.etPassword.getText().toString();

    final ProgressDialog dialog = ProgressDialog.show(this, "", "Logging in ...", true);
    ParseUser.logInInBackground(username, password, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        dialog.dismiss();
        if (e != null) {
          // Login failed
          Log.e(TAG, "FAILED to login", e);
          Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
          return;
        }
        // Login was successful
        launchMainActivity();
      }
    });

  }

  // User pressed "Sign Up" button
  public void onRegisterClick(View view) {
    Intent registrationIntent = new Intent(this, RegistrationActivity.class);
    startActivityForResult(registrationIntent, REGISTRATION_REQUEST_CODE);
  }
}
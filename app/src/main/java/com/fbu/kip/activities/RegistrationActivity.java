package com.fbu.kip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fbu.kip.databinding.ActivityRegistrationBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegistrationActivity extends AppCompatActivity {

  private static final String TAG = "RegistrationActivity";

  ActivityRegistrationBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setupTextWatchers();

  }

  // Enables / Disables Sign Up button depending on EditTexts
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
        binding.btnSignUp.setEnabled(canSignUp());
      }
    };

    binding.etUsername.addTextChangedListener(textWatcher);
    binding.etPassword.addTextChangedListener(textWatcher);
    binding.etConfirmPassword.addTextChangedListener(textWatcher);

  }

  private boolean canSignUp() {
    String username = binding.etUsername.getText().toString();
    String password = binding.etPassword.getText().toString();
    String confirmPassword = binding.etConfirmPassword.getText().toString();

    return !username.isEmpty() && !password.isEmpty() && password.equals(confirmPassword);
  }

  // User pressed "Sign Up" button
  public void onSignUpClick(View view) {
    String username = binding.etUsername.getText().toString();
    String password = binding.etPassword.getText().toString();

    ParseUser newUser = new ParseUser();
    newUser.setUsername(username);
    newUser.setPassword(password);

    final ProgressDialog dialog = ProgressDialog.show(this, "",
      "Signing up ...", true);
    newUser.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        dialog.dismiss();
        if (e != null) {
          // Registration failed
          Log.e(TAG, "FAILED to register user ", e);
          Toast.makeText(RegistrationActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
          return;
        }
        // Registration is successful
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
      }
    });
  }
}
package com.example.kip.activities;

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

import com.example.kip.databinding.ActivityLoginBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

  private static final String TAG = "LoginActivity";
  public static final int REGISTRATION_REQUEST_CODE = 4;

  private ActivityLoginBinding binding;
  CallbackManager callbackManager;

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
    setupFacebookLoginButton();
  }

  private void setupFacebookLoginButton() {
    callbackManager = CallbackManager.Factory.create();
    binding.loginButton.setReadPermissions(Arrays.asList("email"));
    binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        fetchMyInfo(loginResult.getAccessToken());
      }

      @Override
      public void onCancel() {

      }

      @Override
      public void onError(FacebookException error) {

      }
    });
  }

  private void fetchMyInfo(AccessToken accessToken) {
    GraphRequest request = GraphRequest.newMeRequest(
      accessToken,
      new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(
          JSONObject object,
          GraphResponse response) {
          // Application code
        }
      });
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,link");
    request.setParameters(parameters);
    request.executeAsync();
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
    } else {
      // Facebook SDK
      callbackManager.onActivityResult(requestCode, resultCode, data);
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
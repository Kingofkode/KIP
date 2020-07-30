package com.fbu.kip.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.fbu.kip.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

  private static final String TAG = "LoginActivity";
  public static final int REGISTRATION_REQUEST_CODE = 4;
  public static final String FB_ID = "id";
  public static final String FB_NAME = "name";
  public static final String FULL_NAME = "fullName";

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
    binding.btnFacebook.setReadPermissions(Arrays.asList("user_link"));
    binding.btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
    final GraphRequest request = GraphRequest.newMeRequest(
      accessToken,
      new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(
          JSONObject object,
          GraphResponse response) {
          try {
            String id = object.getString(FB_ID);
            String fullName = object.getString(FB_NAME);
            registerOrLoginWithFacebook(id, fullName);
          } catch (JSONException e) {
            Toast.makeText(LoginActivity.this, "Failed to sign up with Facebook", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error parsing FB JSON: ", e);
            e.printStackTrace();
          }

        }
      });
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name");
    request.setParameters(parameters);
    request.executeAsync();
  }




  private void registerOrLoginWithFacebook(final String id, final String fullName) {
    // Determine if user already has an account
    ParseUser.logInInBackground(id, id, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (e != null) {
          // Create a Parse user for this Facebook account
          downloadFBProfilePicture(id, fullName);
        } else {
          // Successfully signed in with existing account!
          launchMainActivity();
        }
      }
    });
  }

  private void downloadFBProfilePicture(final String id, final String fullName) {
    String imageURL = "https://graph.facebook.com/" + id + "/picture?type=large";
    Glide.with(this)
      .asFile()
      .load(imageURL)
      .into(new CustomTarget<File>() {
        @Override
        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
          final ParseFile photo = new ParseFile(resource);
          // Upload to Parse
          photo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
              registerFacebookUser(id, fullName, photo);
            }
          });

        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {

        }
      });
  }

  private void registerFacebookUser(String id, String fullName, ParseFile profileImageFile) {
    ParseUser newUser = new ParseUser();
    newUser.setUsername(id);
    newUser.setPassword(id);
    newUser.put(FULL_NAME, fullName);
    newUser.put(ProfileActivity.KEY_PROFILE_IMAGE, profileImageFile);

    newUser.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error signing up with Facebook", e);
          Toast.makeText(LoginActivity.this, "Error signing up with Facebook", Toast.LENGTH_SHORT).show();
          return;
        }
        // Successfully created a Parse account for this Facebook user
        launchMainActivity();
      }
    });
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
      launchMainActivity();
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
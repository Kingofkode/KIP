package com.example.kip.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.kip.R;
import com.example.kip.databinding.ActivityProfileBinding;
import com.example.kip.models.Friendship;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends PhotoActivity {

  public static final String KEY_FRIEND_IDS = "friendIDs";
  public static final String KEY_PROFILE_IMAGE = "profileImage";

  private static final String TAG = "ProfileActivity";

  ActivityProfileBinding binding;

  ParseUser user;
  Boolean isCurrentUser;
  CallbackManager callbackManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityProfileBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Check if user was passed to us
    user = Parcels.unwrap(getIntent().getParcelableExtra(ParseUser.class.getSimpleName()));
    if (user == null) {
      user = ParseUser.getCurrentUser();
      isCurrentUser = true;
    } else {
      isCurrentUser = false;
    }

    if (isCurrentUser) {
      user.fetchInBackground(new GetCallback<ParseObject>() {
        @Override
        public void done(ParseObject object, ParseException e) {
          inflateProfile();
        }
      });
    }

    inflateProfile();
    setupActionBar();
  }

  private void setupActionBar() {
    getSupportActionBar().setTitle(user.getUsername());
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
    GraphRequest request = GraphRequest.newMeRequest(
      accessToken,
      new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(
          JSONObject object,
          GraphResponse response) {

        }
      });
    Bundle parameters = new Bundle();
    parameters.putString("fields", "link,id,name");
    request.setParameters(parameters);
    request.executeAsync();
  }

  private void inflateProfile() {
    binding.tvUsername.setText(user.getUsername());
    toggleFacebookButton();
    inflateProfileImage();
    inflateFriendCount();
  }

  private void toggleFacebookButton() {
    if (isCurrentUser) {
      setupFacebookLoginButton();
    } else {
      // Hide Facebook sign in
      binding.btnFacebook.setVisibility(View.GONE);
    }
  }

  private void inflateProfileImage() {
    ParseFile imageFileReference = user.getParseFile(KEY_PROFILE_IMAGE);
    if (imageFileReference == null) {
      binding.ivProfile.setImageResource(R.drawable.profile_placeholder);
      return;
    }
    Glide.with(this)
      .load(imageFileReference.getUrl())
      .placeholder(R.drawable.profile_placeholder)
      .circleCrop()
      .into(binding.ivProfile);

  }

  private void inflateFriendCount() {
    ParseQuery<Friendship> friendships = ParseQuery.getQuery(Friendship.class);
    friendships.whereMatches(Friendship.KEY_USER_A, user.getObjectId());
    friendships.findInBackground(new FindCallback<Friendship>() {
      @Override
      public void done(List<Friendship> friendships, ParseException e) {
        int friendCount = friendships.size();
        String friendCountString = getResources().getQuantityString(R.plurals.numberOfFriends, friendCount, friendCount);
        binding.tvFriendCount.setText(friendCountString);
      }
    });

  }

  public void onProfilePictureClick(View view) {
    if (!isCurrentUser)
      return;
    String[] options = {"Take Photo", "Choose existing photo"};
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setItems(options, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int optionIndex) {
        if (optionIndex == 0) { // Take Photo
          launchCamera();
        } else { // Choose photo
          pickPhoto();
        }
      }
    });
    builder.show();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Taken photo from camera
    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        // by this point we have the camera photo on disk
        Bitmap takenImage = rotateBitmapOrientation(photoFile.getAbsolutePath());
        // RESIZE BITMAP, see section below
        // Load the taken image into a preview
        binding.ivProfile.setImageBitmap(takenImage);
        saveProfilePicture(photoFile);
      } else { // Result was a failure
        Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
      }
    }

    // Chosen from photo library
    if (requestCode == PICK_PHOTO_CODE && data != null) {
      Uri photoUri = data.getData();

      // Load the image located at photoUri into selectedImage
      Bitmap selectedImage = loadChosenPhotoFromUri(photoUri);
      File chosenPhotoFile = bitmapToFile(selectedImage);
      saveProfilePicture(chosenPhotoFile);
      // Load the selected image into a preview
      binding.ivProfile.setImageBitmap(selectedImage);
    }

    // Facebook SDK
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  private void saveProfilePicture(File photoFile) {
    user.put(KEY_PROFILE_IMAGE, new ParseFile(photoFile));
    final ProgressDialog dialog = ProgressDialog.show(this, "", "Uploading ...", true);
    user.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        dialog.dismiss();
        if (e != null) { // Failed to save profile picture
          Log.e(TAG, "Error while saving profile picture", e);
          Toast.makeText(ProfileActivity.this, "Upload failed. Please try again.", Toast.LENGTH_SHORT).show();
          return;
        }
        // Picture saved successfully
        Log.i(TAG, "Profile photo saved successfully");
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (isCurrentUser) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.profile_menu, menu);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_logout) {
      logout();
      return true;
    }
    return super.onOptionsItemSelected(item);

  }

  private void logout() {
    final ProgressDialog dialog = ProgressDialog.show(this, "", "Logging out...", true);
    ParseUser.logOutInBackground(new LogOutCallback() {
      @Override
      public void done(ParseException e) {
        dialog.dismiss();
        Intent logInIntent = new Intent(ProfileActivity.this, LoginActivity.class);
        logInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logInIntent);
      }
    });
  }
}


package com.example.kip.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.kip.R;
import com.example.kip.databinding.ActivityProfileBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ProfileActivity extends PhotoActivity {

  public static final String KEY_FRIEND_IDS = "friendIDs";
  public static final String KEY_PROFILE_IMAGE = "profileImage";

  private static final String TAG = "ProfileActivity";

  ActivityProfileBinding binding;

  ParseUser currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityProfileBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    currentUser = ParseUser.getCurrentUser();
    populateUserData();

  }

  private void populateUserData() {
    binding.tvUsername.setText(currentUser.getUsername());
    inflateFriendCount();
    String imageUrl = null;
    try {
      imageUrl = currentUser.fetch().getParseFile(KEY_PROFILE_IMAGE).getUrl();
    } catch (ParseException e) {
      // FIXME: Keeping getting Invalid session token
      e.printStackTrace();
    }
    Glide.with(this)
      .load(imageUrl)
      .circleCrop()
      .into(binding.ivProfile);
  }

  private void inflateFriendCount() {
    List<String> friendIDs = currentUser.getList(KEY_FRIEND_IDS);
    int friendCount = 0;
    if (friendIDs != null) {
      friendCount = friendIDs.size();
    }
    String friendCountString = getResources().getQuantityString(R.plurals.numberOfFriends, friendCount, friendCount);
    binding.tvFriendCount.setText(friendCountString);
  }

  public void onProfilePictureClick(View view) {
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
        saveProfilePicture();
      } else { // Result was a failure
        Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
      }
    }

    // Chosen from photo library
    if (requestCode == PICK_PHOTO_CODE && data != null) {
      Uri photoUri = data.getData();

      // Load the image located at photoUri into selectedImage
      Bitmap selectedImage = loadChosenPhotoFromUri(photoUri);

      // Load the selected image into a preview
      binding.ivProfile.setImageBitmap(selectedImage);
    }
  }

  private void saveProfilePicture() {
    currentUser.put(KEY_PROFILE_IMAGE, new ParseFile(photoFile));
    final ProgressDialog dialog = ProgressDialog.show(this, "", "Uploading ...", true);
    currentUser.saveInBackground(new SaveCallback() {
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

}


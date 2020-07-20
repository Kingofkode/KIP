package com.example.kip.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kip.R;
import com.example.kip.databinding.ActivityProfileBinding;
import com.parse.ParseUser;

import java.io.File;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

  public static final String KEY_FRIEND_IDS = "friendIDs";

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

    // TODO: Replace with actual profile image
    Glide.with(this)
      .load("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/09/01/15/zuckprofpic.jpg?w968h681")
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
          onLaunchCamera();
        } else { // Choose photo

        }
      }
    });
    builder.show();
  }

  public final String APP_TAG = "MyCustomApp";
  public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
  public String photoFileName = "photo.jpg";
  File photoFile;

  public void onLaunchCamera() {
    // create Intent to take a picture and return control to the calling application
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Create a File reference for future access
    photoFile = getPhotoFileUri(photoFileName);

    // wrap File object into a content provider
    // required for API >= 24
    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
    Uri fileProvider = FileProvider.getUriForFile(ProfileActivity.this, "com.KIP.fileprovider", photoFile);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
    // So as long as the result is not null, it's safe to use the intent.
    if (intent.resolveActivity(getPackageManager()) != null) {
      // Start the image capture intent to take photo
      startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
  }

  // Returns the File for a photo stored on disk given the fileName
  public File getPhotoFileUri(String fileName) {
    // Get safe storage directory for photos
    // Use `getExternalFilesDir` on Context to access package-specific directories.
    // This way, we don't need to request external read/write runtime permissions.
    File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
      Log.d(APP_TAG, "failed to create directory");
    }

    // Return the file target for the photo based on filename
    File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

    return file;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        // by this point we have the camera photo on disk
        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        // RESIZE BITMAP, see section below
        // Load the taken image into a preview
        binding.ivProfile.setImageBitmap(takenImage);
      } else { // Result was a failure
        Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
      }
    }
  }



}


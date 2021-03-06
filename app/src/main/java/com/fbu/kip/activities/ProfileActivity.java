package com.fbu.kip.activities;

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
import com.facebook.login.LoginManager;
import com.fbu.kip.R;
import com.fbu.kip.Utils;
import com.fbu.kip.databinding.ActivityProfileBinding;
import com.fbu.kip.models.Conversation;
import com.fbu.kip.models.Friendship;
import com.fbu.kip.models.Message;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileActivity extends PhotoActivity {

  private static final String TAG = "ProfileActivity";
  public static final String KEY_FRIEND_IDS = "friendIDs";
  public static final String KEY_PROFILE_IMAGE = "profileImage";

  ActivityProfileBinding binding;

  ParseUser user;
  boolean isCurrentUser;

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
    if (getSupportActionBar() == null)
      return;
    getSupportActionBar().setTitle(Utils.getFullName(user));
  }

  private void inflateProfile() {
    binding.tvUsername.setText(Utils.getFullName(user));
    inflateProfileImage();
    inflateFriendCount();
    inflateFriendshipHealthBar();
  }

  private void inflateFriendshipHealthBar() {
    if (isCurrentUser)
      return;
    binding.healthBar.setVisibility(View.VISIBLE);
    ParseQuery<Conversation> conversationParseQuery = ParseQuery.getQuery(Conversation.class);
    ArrayList<ParseUser> memberIDs = new ArrayList<>();
    memberIDs.add(ParseUser.getCurrentUser());
    memberIDs.add(user);
    conversationParseQuery.whereContainsAll(Conversation.KEY_MEMBER_IDS, memberIDs);
    conversationParseQuery.include(Conversation.KEY_MEMBER_IDS);
    conversationParseQuery.addDescendingOrder(Message.KEY_UPDATED_AT);
    conversationParseQuery.findInBackground(new FindCallback<Conversation>() {
      @Override
      public void done(List<Conversation> conversationsList, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error fetching conversation", e);
          return;
        }
        if (conversationsList.isEmpty()) { // User has never had a conversation. They are out of touch
          binding.healthBar.setValue(0.0);
          return;
        }

        // Should be 1 conversation
        Conversation conversation = conversationsList.get(0);
        Utils.FriendshipStatus friendshipStatus = Utils.getFriendshipStatus(conversation.getUpdatedAt());
        switch (friendshipStatus) {
          case inTouch:
            binding.healthBar.setValue(100);
            break;
          case beenASecond:
            binding.healthBar.setValue(79);
            break;
          case beenAMinute:
            binding.healthBar.setValue(59);
            break;
          case beenAWhile:
            binding.healthBar.setValue(39);
            break;
          case beenForever:
            binding.healthBar.setValue(0);
        }
      }
    });
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
    friendships.include(Friendship.KEY_USER_B);
    friendships.findInBackground(new FindCallback<Friendship>() {
      @Override
      public void done(List<Friendship> friendships, ParseException e) {
        int friendCount = friendships.size();
        String friendCountString = getResources().getQuantityString(R.plurals.numberOfFriends, friendCount, friendCount);
        binding.tvFriendCount.setText(friendCountString);

        if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
          // Show join date
          String joinDescription = "Joined KIP on " + Utils.getFriendshipTimestamp(user.getCreatedAt());
          binding.tvFriendDuration.setText(joinDescription);
        } else {
          // Show friend duration
          for (Friendship friendship : friendships) {
            if (friendship.getUserB().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
              String friendshipDescription = "Friends since " + Utils.getFriendshipTimestamp(friendship.getCreatedAt());
              binding.tvFriendDuration.setText(friendshipDescription);
              break;
            }

          }
        }
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
    // Logout of Facebook if necessary
    LoginManager.getInstance().logOut();
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


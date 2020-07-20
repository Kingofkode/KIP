package com.example.kip.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kip.R;
import com.example.kip.databinding.ActivityProfileBinding;
import com.parse.ParseUser;

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

        } else { // Choose photo

        }
      }
    });
    builder.show();
  }
}


package com.fbu.kip.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fbu.kip.R;
import com.fbu.kip.Utils;
import com.fbu.kip.activities.MessageActivity;
import com.fbu.kip.activities.ProfileActivity;
import com.fbu.kip.databinding.ItemFriendRequestBinding;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

  Activity activity;
  List<ParseUser> users;
  boolean usersAreFriends; // Hides or shows relevant views

  public UsersAdapter(Activity activity, List<ParseUser> users, boolean usersAreFriends) {
    this.users = users;
    this.usersAreFriends = usersAreFriends;
    this.activity = activity;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(activity).inflate(R.layout.item_friend_request, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ParseUser user = users.get(position);
    holder.bind(user);
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ItemFriendRequestBinding binding;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      binding = ItemFriendRequestBinding.bind(itemView);
    }

    public void bind(final ParseUser user) {

      binding.tvUsername.setText(Utils.getFullName(user));

      ParseFile profileImageRef = user.getParseFile(ProfileActivity.KEY_PROFILE_IMAGE);
      if (profileImageRef != null) {
        Glide.with(activity)
          .load(profileImageRef.getUrl())
          .placeholder(R.drawable.profile_placeholder)
          .circleCrop()
          .into(binding.ivProfile);
      } else {
        binding.ivProfile.setImageResource(R.drawable.profile_placeholder);
      }

      binding.btnAdd.setVisibility(usersAreFriends ? View.GONE : View.VISIBLE);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent messagingIntent = new Intent(activity, MessageActivity.class);
          messagingIntent.putExtra(ParseUser.class.getSimpleName(), Parcels.wrap(user));
          activity.startActivity(messagingIntent);
          activity.finish();
        }
      });
    }

  }


}

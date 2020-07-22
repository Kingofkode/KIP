package com.example.kip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kip.R;

import com.example.kip.activities.ProfileActivity;
import com.example.kip.databinding.ItemFriendRequestBinding;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

  Context context;
  List<ParseUser> users;
  Boolean usersAreFriends; // Hides or shows relevant views

  public UsersAdapter(Context context, List<ParseUser> users, Boolean usersAreFriends) {
    this.context = context;
    this.users = users;
    this.usersAreFriends = usersAreFriends;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false);
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

    public void bind(ParseUser user) {

      binding.tvUsername.setText(user.getUsername());

      ParseFile profileImageRef = user.getParseFile(ProfileActivity.KEY_PROFILE_IMAGE);
      if (profileImageRef != null) {
        Glide.with(context)
          .load(profileImageRef.getUrl())
          .placeholder(R.drawable.profile_placeholder)
          .circleCrop()
          .into(binding.ivProfile);
      } else {
        binding.ivProfile.setImageResource(R.drawable.profile_placeholder);
      }

      binding.btnAdd.setVisibility(usersAreFriends ? View.GONE : View.VISIBLE);
    }
  }


}

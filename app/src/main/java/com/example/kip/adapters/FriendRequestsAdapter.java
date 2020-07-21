package com.example.kip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kip.R;
import com.example.kip.activities.ProfileActivity;
import com.example.kip.models.FriendRequest;
import com.parse.ParseFile;

import java.util.List;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {

  Context context;
  List<FriendRequest> friendRequests;

  public FriendRequestsAdapter(Context context, List<FriendRequest> friendRequests) {
    this.context = context;
    this.friendRequests = friendRequests;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    FriendRequest friendRequest = friendRequests.get(position);
    holder.bind(friendRequest);
  }

  @Override
  public int getItemCount() {
    return friendRequests.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    TextView tvUsername;
    ImageView ivProfile;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvUsername = itemView.findViewById(R.id.tvUsername);
      ivProfile = itemView.findViewById(R.id.ivProfile);
    }

    public void bind(FriendRequest friendRequest) {
      tvUsername.setText(friendRequest.getSender().getUsername());
      ParseFile profileImageRef = friendRequest.getSender().getParseFile(ProfileActivity.KEY_PROFILE_IMAGE);
      if (profileImageRef != null) {
        Glide.with(context)
          .load(profileImageRef.getUrl())
          .placeholder(R.drawable.profile_placeholder)
          .circleCrop()
          .into(ivProfile);
      } else {
        ivProfile.setImageResource(R.drawable.profile_placeholder);
      }
    }
  }

}

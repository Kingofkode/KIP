package com.example.kip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kip.R;
import com.example.kip.activities.ProfileActivity;
import com.example.kip.models.FriendRequest;
import com.example.kip.models.Friendship;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    Button btnAccept;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvUsername = itemView.findViewById(R.id.tvUsername);
      ivProfile = itemView.findViewById(R.id.ivProfile);
      btnAccept = itemView.findViewById(R.id.btnAdd);

    }

    public void bind(final FriendRequest friendRequest) {
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

      // User presses "Accept"
      btnAccept.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          // 1. Create friendship both ways for faster querying
          Friendship friendshipA = new Friendship();
          friendshipA.setUserA(ParseUser.getCurrentUser());
          friendshipA.setUserB(friendRequest.getSender());
          friendshipA.saveInBackground();

          Friendship friendshipB = new Friendship();
          friendshipB.setUserA(friendRequest.getSender());
          friendshipB.setUserB(ParseUser.getCurrentUser());
          friendshipB.saveInBackground();

          // 2. Delete friend request in Parse
          friendRequest.deleteInBackground();

          // 3. Delete in UI
          int position = friendRequests.indexOf(friendRequest);
          friendRequests.remove(position);
          notifyItemRemoved(position);
        }
      });
    }

  }

}

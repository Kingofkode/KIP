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
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class FriendRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int TYPE_REQUEST = 0;
  private static final int TYPE_USER = 1;

  Context context;
  List<FriendRequest> friendRequests;
  List<ParseUser> searchedUsers;

  public FriendRequestsAdapter(Context context, List<FriendRequest> friendRequests, List<ParseUser> searchedUsers) {
    this.context = context;
    this.friendRequests = friendRequests;
    this.searchedUsers = searchedUsers;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_friend_request, parent, false);
    if (viewType == TYPE_REQUEST) {
      return new RequestViewHolder(view);
    } else {
      return new UserViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (getItemViewType(position) == TYPE_REQUEST) {
      ((RequestViewHolder) holder).bind(friendRequests.get(position));
    } else { // User
      ((UserViewHolder) holder).bind(searchedUsers.get(position - friendRequests.size()));
    }
  }

  @Override
  public int getItemCount() {
    return friendRequests.size() + searchedUsers.size();
  }

  @Override
  public int getItemViewType(int position) {
    if (position >= friendRequests.size()) {
      return TYPE_USER;
    } else {
      return TYPE_REQUEST;
    }
  }

  class RequestViewHolder extends RecyclerView.ViewHolder {

    TextView tvUsername;
    ImageView ivProfile;
    Button btnAccept;

    public RequestViewHolder(@NonNull View itemView) {
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
          Toast.makeText(context, "Accepted friend request!", Toast.LENGTH_SHORT).show();
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

  class UserViewHolder extends RecyclerView.ViewHolder {

    TextView tvUsername;
    ImageView ivProfile;
    Button btnAdd;

    public UserViewHolder(@NonNull View itemView) {
      super(itemView);
      tvUsername = itemView.findViewById(R.id.tvUsername);
      ivProfile = itemView.findViewById(R.id.ivProfile);
      btnAdd = itemView.findViewById(R.id.btnAdd);
      btnAdd.setText("Add");
    }


    public void bind(final ParseUser user) {
      // User name
      tvUsername.setText(user.getUsername());

      // Profile image
      ParseFile profileImageRef = user.getParseFile(ProfileActivity.KEY_PROFILE_IMAGE);
      if (profileImageRef != null) {
        Glide.with(context)
          .load(profileImageRef.getUrl())
          .placeholder(R.drawable.profile_placeholder)
          .circleCrop()
          .into(ivProfile);
      } else {
        ivProfile.setImageResource(R.drawable.profile_placeholder);
      }

      btnAdd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Toast.makeText(context, "Sent friend request!", Toast.LENGTH_SHORT).show();
          FriendRequest friendRequest = new FriendRequest();
          friendRequest.setRecipient(user);
          friendRequest.setSender(ParseUser.getCurrentUser());
          friendRequest.saveInBackground();

          // Delete in UI
          int position = searchedUsers.indexOf(user);
          searchedUsers.remove(position);
          notifyItemRemoved(position);

        }
      });
    }
  }

}

package com.fbu.kip.adapters;

import android.content.Context;
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
import com.fbu.kip.databinding.ItemConversationBinding;
import com.fbu.kip.models.Conversation;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {

  Context context;
  List<Conversation> conversations;

  public ConversationsAdapter(Context context, List<Conversation> conversations) {
    this.context = context;
    this.conversations = conversations;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Conversation conversation = conversations.get(position);
    holder.bind(conversation);
  }

  @Override
  public int getItemCount() {
    return conversations.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ItemConversationBinding binding;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      binding = ItemConversationBinding.bind(itemView);
    }


    public void bind(final Conversation conversation) {
      ParseUser recipient = conversation.getMembers().get(0);
      if (recipient.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
        recipient = conversation.getMembers().get(1);
      }
      // Inflate username
      binding.tvUser.setText(Utils.getFullName(recipient));
      // Inflate last message
      if (conversation.getLastMessage() != null) {
        binding.tvBody.setText(conversation.getLastMessage().getBody());
      }
      // Load profile image or placeholder
      ParseFile profileImageRef = recipient.getParseFile(ProfileActivity.KEY_PROFILE_IMAGE);
      if (profileImageRef != null) {
        Glide.with(context)
          .load(profileImageRef.getUrl())
          .placeholder(R.drawable.profile_placeholder)
          .circleCrop()
          .into(binding.ivProfile);
      } else {
        binding.ivProfile.setImageResource(R.drawable.profile_placeholder);
      }

      // Inflate timestamp
      binding.tvDate.setText(Utils.getConversationTimestamp(conversation.getUpdatedAt()));

      // Click Listener
      final ParseUser finalRecipient = recipient;

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent messagingIntent = new Intent(context, MessageActivity.class);
          // Pass along the conversation object
          messagingIntent.putExtra(ParseUser.class.getSimpleName(), Parcels.wrap(finalRecipient));
          context.startActivity(messagingIntent);
        }
      });

      final ParseUser recipientProfile = recipient;
      binding.ivProfile.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
          Intent profileIntent = new Intent(context, ProfileActivity.class);
          profileIntent.putExtra(ParseUser.class.getSimpleName(), Parcels.wrap(recipientProfile));
          context.startActivity(profileIntent);
          return false;
        }
      });
    }
  }
}

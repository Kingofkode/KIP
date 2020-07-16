package com.example.kip.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kip.R;
import com.example.kip.activities.MessageActivity;
import com.example.kip.databinding.ItemConversationBinding;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {

  Context context;

  public ConversationsAdapter(Context context) {
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // TODO: Load appropriate profile image
    Glide.with(context)
      .load("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/09/01/15/zuckprofpic.jpg?w968h681")
      .circleCrop()
      .into(holder.binding.ivProfile);
  }

  @Override
  public int getItemCount() {
    return 1;
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemConversationBinding binding;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      binding = ItemConversationBinding.bind(itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      // TODO: Pass along the conversation object
      Intent chatIntent = new Intent(context, MessageActivity.class);
      context.startActivity(chatIntent);
    }
  }
}

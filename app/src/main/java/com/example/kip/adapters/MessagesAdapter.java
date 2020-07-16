package com.example.kip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kip.R;
import com.example.kip.databinding.ItemMessageBinding;
import com.example.kip.models.Message;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

  Context context;
  List<Message> messages;

  public MessagesAdapter(Context context, List<Message> messages) {
    this.context = context;
    this.messages = messages;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.binding.tvBody.setText(messages.get(position).getBody());

  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ItemMessageBinding binding;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      binding = ItemMessageBinding.bind(itemView);
    }
  }
}

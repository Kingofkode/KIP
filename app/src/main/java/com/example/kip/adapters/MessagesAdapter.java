package com.example.kip.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}

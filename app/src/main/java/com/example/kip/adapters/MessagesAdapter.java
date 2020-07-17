package com.example.kip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kip.R;
import com.example.kip.models.Message;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static int TYPE_SENT = 0;
  private static int TYPE_RECEIVED = 1;

  Context context;
  List<Message> messages;

  public MessagesAdapter(Context context, List<Message> messages) {
    this.context = context;
    this.messages = messages;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    int layout = viewType == TYPE_RECEIVED ? R.layout.item_message_left : R.layout.item_message_right;
    View view = LayoutInflater.from(context).inflate(layout, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ViewHolder myViewHolder = (ViewHolder) holder;
    Message message = messages.get(position);
    myViewHolder.bind(message);
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  @Override
  public int getItemViewType(int position) {
    return position % 2;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    TextView tvBody;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvBody = itemView.findViewById(R.id.tvBody);
    }

    public void bind(Message message) {
      tvBody.setText(message.getBody());
    }
  }
}

package com.example.kip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kip.R;
import com.example.kip.databinding.ItemUserBinding;
import com.parse.ParseUser;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

  Context context;
  List<ParseUser> users;

  public UsersAdapter(Context context, List<ParseUser> users) {
    this.context = context;
    this.users = users;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // TODO: Load actual user
    holder.binding.tvUsername.setText(users.get(position).getUsername());
    Glide.with(context)
      .load("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/09/01/15/zuckprofpic.jpg?w968h681")
      .circleCrop()
      .into(holder.binding.ivProfile);
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ItemUserBinding binding;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      binding = ItemUserBinding.bind(itemView);
    }
  }


}

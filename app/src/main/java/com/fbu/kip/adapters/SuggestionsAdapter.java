package com.fbu.kip.adapters;

import android.app.Activity;
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
import com.fbu.kip.databinding.ItemSuggestionBinding;
import com.fbu.kip.models.Suggestion;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ViewHolder> {

  Activity activity;
  List<Suggestion> suggestions;

  public SuggestionsAdapter(Activity activity, List<Suggestion> suggestions) {
    this.activity = activity;
    this.suggestions = suggestions;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(activity).inflate(R.layout.item_suggestion, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Suggestion suggestion = suggestions.get(position);
    holder.bind(suggestion);
  }

  @Override
  public int getItemCount() {
    return suggestions.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ItemSuggestionBinding binding;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      binding = ItemSuggestionBinding.bind(itemView);
    }

    public void bind(final Suggestion suggestion) {
      binding.tvUser.setText(Utils.getFullName(suggestion.getRecipient()));
      binding.tvSuggestion.setText(suggestion.getBody());

      // Load profile image or placeholder
      ParseFile profileImageRef = suggestion.getRecipient().getParseFile(ProfileActivity.KEY_PROFILE_IMAGE);
      if (profileImageRef != null) {
        Glide.with(activity)
          .load(profileImageRef.getUrl())
          .placeholder(R.drawable.profile_placeholder)
          .circleCrop()
          .into(binding.ivProfile);
      } else {
        binding.ivProfile.setImageResource(R.drawable.profile_placeholder);
      }

      // Click listener
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent newMessageIntent = new Intent(activity, MessageActivity.class);
          newMessageIntent.putExtra(ParseUser.class.getSimpleName(), Parcels.wrap(suggestion.getRecipient()));
          // Load the suggestion
          newMessageIntent.putExtra(Suggestion.class.getSimpleName(), Parcels.wrap(suggestion));
          activity.startActivity(newMessageIntent);
        }
      });
    }
  }


}

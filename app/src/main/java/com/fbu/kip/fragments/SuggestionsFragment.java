package com.fbu.kip.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fbu.kip.Utils;
import com.fbu.kip.adapters.SuggestionsAdapter;
import com.fbu.kip.databinding.FragmentSuggestionsBinding;
import com.fbu.kip.models.Friendship;
import com.fbu.kip.models.Suggestion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsFragment extends Fragment {

  private static final String TAG = "SuggestionsFragment";

  FragmentSuggestionsBinding binding;
  SuggestionsAdapter adapter;
  List<Suggestion> suggestions;

  // Required empty public constructor
  public SuggestionsFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentSuggestionsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);;
    setupRecyclerView();
    generateSuggestions();
  }

  private void generateSuggestions() {

    // Fetch friendships
    ParseQuery<Friendship> friendshipParseQuery = ParseQuery.getQuery(Friendship.class);
    friendshipParseQuery.whereMatches(Friendship.KEY_USER_A, ParseUser.getCurrentUser().getObjectId());
    friendshipParseQuery.include(Friendship.KEY_USER_B);
    friendshipParseQuery.findInBackground(new FindCallback<Friendship>() {
      @Override
      public void done(List<Friendship> friendshipList, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue getting friend requests", e);
          return;
        }
        for (int i = 0; i < friendshipList.size(); i++) {
          // Create suggestion for each one. Cycling through the list of suggestions.
          Suggestion suggestion = new Suggestion(friendshipList.get(i).getUserB(), Utils.suggestionMessages[i % Utils.suggestionMessages.length]);
          suggestions.add(suggestion);
        }
        adapter.notifyDataSetChanged();
      }

    });

  }

  private void setupRecyclerView() {
    suggestions = new ArrayList<>();
    adapter = new SuggestionsAdapter(getActivity(), suggestions);
    binding.rvSuggestions.setAdapter(adapter);
    binding.rvSuggestions.setLayoutManager(new LinearLayoutManager(getContext()));
  }
}
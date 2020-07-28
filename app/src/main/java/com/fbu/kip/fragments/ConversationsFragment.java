package com.fbu.kip.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fbu.kip.adapters.ConversationsAdapter;
import com.fbu.kip.databinding.FragmentConversationsBinding;
import com.fbu.kip.models.Conversation;
import com.fbu.kip.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ConversationsFragment extends Fragment {

  private static final String TAG = "ConversationsFragment";

  FragmentConversationsBinding binding;
  private ConversationsAdapter adapter;
  private List<Conversation> allConversations;

  // Required empty public constructor
  public ConversationsFragment() {

  }

  @Override
  public void onResume() {
    super.onResume();
    // Update conversation thread
    fetchConversations();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentConversationsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    allConversations = new ArrayList<>();
    adapter = new ConversationsAdapter(getContext(), allConversations);
    binding.rvConversations.setAdapter(adapter);
    binding.rvConversations.setLayoutManager(new LinearLayoutManager(getContext()));

  }

  private void fetchConversations() {
    ParseQuery<Conversation> conversationParseQuery = ParseQuery.getQuery(Conversation.class);
    ArrayList<ParseUser> memberIDs = new ArrayList<>();
    memberIDs.add(ParseUser.getCurrentUser());
    conversationParseQuery.whereContainsAll(Conversation.KEY_MEMBER_IDS, memberIDs);
    conversationParseQuery.include(Conversation.KEY_LAST_MESSAGE);
    conversationParseQuery.include(Conversation.KEY_MEMBER_IDS);
    conversationParseQuery.addDescendingOrder(Message.KEY_UPDATED_AT);
    conversationParseQuery.findInBackground(new FindCallback<Conversation>() {
      @Override
      public void done(List<Conversation> conversations, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error fetching conversations: ", e);
          Toast.makeText(getContext(), "Failed to load conversations", Toast.LENGTH_SHORT).show();
          return;
        }
        allConversations.clear();
        allConversations.addAll(conversations);
        adapter.notifyDataSetChanged();
      }
    });

  }
}
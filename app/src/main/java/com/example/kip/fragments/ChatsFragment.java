package com.example.kip.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kip.adapters.ConversationsAdapter;
import com.example.kip.databinding.FragmentChatsBinding;

public class ChatsFragment extends Fragment {

  private static final String TAG = "ChatsFragment";

  FragmentChatsBinding binding;
  private ConversationsAdapter adapter;

  // Required empty public constructor
  public ChatsFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentChatsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    adapter = new ConversationsAdapter(getContext());
    binding.rvChats.setAdapter(adapter);
    binding.rvChats.setLayoutManager(new LinearLayoutManager(getContext()));

  }
}
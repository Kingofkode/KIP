package com.example.kip.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kip.adapters.ConversationsAdapter;
import com.example.kip.databinding.FragmentConversationsBinding;

public class ConversationsFragment extends Fragment {

  private static final String TAG = "ConversationsFragment";

  FragmentConversationsBinding binding;
  private ConversationsAdapter adapter;

  // Required empty public constructor
  public ConversationsFragment() {

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

    adapter = new ConversationsAdapter(getContext());
    binding.rvConversations.setAdapter(adapter);
    binding.rvConversations.setLayoutManager(new LinearLayoutManager(getContext()));

  }
}
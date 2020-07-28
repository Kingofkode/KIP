package com.fbu.kip.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fbu.kip.databinding.FragmentSuggestionsBinding;

public class SuggestionsFragment extends Fragment {

  private static final String TAG = "SuggestionsFragment";

  FragmentSuggestionsBinding binding;

  // Required empty public constructor
  public SuggestionsFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = FragmentSuggestionsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}
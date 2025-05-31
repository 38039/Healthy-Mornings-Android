package com.nforge.healthymornings.model.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.databinding.ActivityShareBinding;
import com.nforge.healthymornings.viewmodel.ShareViewmodel;


public class ShareFragment extends Fragment {
    private ActivityShareBinding binding;
    private ShareViewmodel viewmodel;


    public ShareFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityShareBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = new ViewModelProvider(this).get(ShareViewmodel.class);
    }
}

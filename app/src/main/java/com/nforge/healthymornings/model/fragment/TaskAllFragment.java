package com.nforge.healthymornings.model.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nforge.healthymornings.databinding.ActivityTaskAllBinding;
import com.nforge.healthymornings.viewmodel.TaskAllViewmodel;


public class TaskAllFragment extends Fragment {
    private ActivityTaskAllBinding binding;
    private TaskAllViewmodel viewmodel;


    public TaskAllFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityTaskAllBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = new ViewModelProvider(this).get(TaskAllViewmodel.class);
    }
}

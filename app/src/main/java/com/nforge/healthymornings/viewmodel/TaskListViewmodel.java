package com.nforge.healthymornings.viewmodel;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import com.nforge.healthymornings.model.repository.TaskRepository;
import com.nforge.healthymornings.model.data.Task;


public class TaskListViewmodel extends AndroidViewModel {
    private final TaskRepository taskRepository;
    public MutableLiveData<ArrayList<Task>> tasks = new MutableLiveData<>(new ArrayList<>());


    public TaskListViewmodel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application.getApplicationContext());
    }

    public void loadTasks(Context context, Runnable onSuccess) {
        ArrayList<Task> taskList = new ArrayList<>();

        if (!taskRepository.loadUserTasks(taskList)) {
            Toast.makeText(context, "Nie udało się załadować zadań", Toast.LENGTH_SHORT).show();
            return;
        }

        tasks.setValue(taskList);
        if (onSuccess != null) onSuccess.run();
    }

    public MutableLiveData<ArrayList<Task>> getTasks() {
        return tasks;
    }
}


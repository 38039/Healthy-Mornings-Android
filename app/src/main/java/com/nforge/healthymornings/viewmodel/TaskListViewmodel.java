package com.nforge.healthymornings.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import com.nforge.healthymornings.model.repository.TaskRepository;


public class TaskListViewmodel extends AndroidViewModel {
    private final TaskRepository taskRepository;
    public MutableLiveData<ArrayList<String>>   taskTitles      = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>>  taskIdentifiers = new MutableLiveData<>(new ArrayList<>());

    public TaskListViewmodel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application.getApplicationContext());
    }

    public void populateAdapterWithTasks(ArrayAdapter<String> adapter) {
        ArrayList<String>   taskTitlesList       = taskTitles.getValue();
        ArrayList<Integer>  taskIdentifiersList  = taskIdentifiers.getValue();

        if (taskTitlesList != null && taskIdentifiersList != null) {

            if ( !taskRepository.loadUserTasks(taskTitlesList, taskIdentifiersList, adapter) ) {
                Log.e("TaskListViewModel", "populateAdapterWithTasks(): Nie udało się załadować zadań");
                Toast.makeText(this.getApplication(), "Nie udało się załadować zadań", Toast.LENGTH_SHORT).show();
                return;
            }

            taskTitles.setValue(taskTitlesList);
            taskIdentifiers.setValue(taskIdentifiersList);

            Log.v("TaskListViewModel", "populateAdapterWithTasks(): Załadowano zadania");
        }
    }
}

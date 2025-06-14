package com.nforge.healthymornings.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.repository.TaskRepository;
import com.nforge.healthymornings.model.repository.UserRepository;


public class TaskListViewmodel extends AndroidViewModel {
    private final TaskRepository taskRepository;
    private UserRepository userRepository;
    public MutableLiveData<ArrayList<String>>   taskTitles      = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>>  taskIdentifiers = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> taskPoints = new MutableLiveData<>();

    public TaskListViewmodel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application.getApplicationContext());
        userRepository = new UserRepository(application.getApplicationContext());
    }

    public void populateAdapterWithTasks(ArrayAdapter<String> adapter) {
        ArrayList<String>   taskTitlesList       = taskTitles.getValue();
        ArrayList<Integer>  taskIdentifiersList  = taskIdentifiers.getValue();

        if (taskTitlesList != null && taskIdentifiersList != null) {

            if ( !taskRepository.loadUserTasks(taskTitlesList, taskIdentifiersList, adapter) ) {
                Log.e("TaskListViewModel", "populateAdapterWithTasks(): Nie udało się załadować zadań");
                return;
            }

            taskTitles.setValue(taskTitlesList);
            taskIdentifiers.setValue(taskIdentifiersList);

            Log.v("TaskListViewModel", "populateAdapterWithTasks(): Załadowano zadania");
        }
    }

    public int getId() {
        User user = userRepository.getUserCredentials();
        return user.getIdUser();
    }

    public int getTaskCount() {
        ArrayList<String> titles = taskTitles.getValue();
        if (titles != null) {
            Log.v("TaskListViewModel", "getTaskCount(): " + titles.size());
            return titles.size();
        } else {
            Log.v("TaskListViewModel", "getTaskCount(): titles null");
            return 0;
        }
    }

}

// Viewmodel dla TaskAddActivity
package com.nforge.healthymornings.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.nforge.healthymornings.model.repository.TaskRepository;


public class TaskAddViewmodel extends AndroidViewModel {
    private final TaskRepository taskRepository;
    public MutableLiveData<String> addTaskResultLiveData = new MutableLiveData<>();
    public MutableLiveData<String> addTaskErrorLiveData  = new MutableLiveData<>();

    public TaskAddViewmodel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application.getApplicationContext());
    }

    // Do listenera w TaskAddActivity, zwraca czy zadanie zapisano poprawnie
    public LiveData<String> getTaskAddResultLiveData() {
        return addTaskResultLiveData;
    }

    // Do listenera w TaskAddActivity, zwraca komunikat błędu który wystąpił podczas dodawania zadania
    public LiveData<String> getTaskAddErrorLiveData() {
        return addTaskErrorLiveData;
    }

    public void addTask(
            String newTaskName,
            String newTaskCategory,
            String newTaskDescription,
            int newTaskPointsReward
    ) {

        if (newTaskName.isEmpty()) {
            addTaskErrorLiveData.postValue("Podaj nazwę zadania");
            return;
        }

        if (newTaskCategory.isEmpty()) {
            addTaskErrorLiveData.postValue("Podaj kategorię zadania");
            return;
        }

        if (newTaskDescription.isEmpty()) {
            addTaskErrorLiveData.postValue("Podaj opis zadania");
            return;
        }

        if (newTaskPointsReward < 0 || newTaskPointsReward > 100 ) {
            addTaskErrorLiveData.postValue("Podaj punktację zadania z zakresu od 0 do 100");
            return;
        }

        if ( !taskRepository.addUserTask(newTaskName, newTaskCategory, newTaskDescription, newTaskPointsReward) ) {
            addTaskErrorLiveData.postValue("Nie udało się dodać zadania");
            return;
        }

        addTaskResultLiveData.postValue("Dodano zadanie!");
    }
}

// ViewModel dla TaskEditActivity
package com.nforge.healthymornings.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import com.nforge.healthymornings.model.data.Task;
import com.nforge.healthymornings.model.repository.TaskRepository;


public class TaskEditViewmodel extends AndroidViewModel {
    private final TaskRepository taskRepository;
    private final MutableLiveData<Task> taskData           = new MutableLiveData<>();
    public MutableLiveData<String> taskEditResultLiveData  = new MutableLiveData<>();
    public MutableLiveData<String> taskEditErrorLiveData   = new MutableLiveData<>();

    public TaskEditViewmodel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application.getApplicationContext());
    }

    // Do listenera w TaskEditActivity, zwraca czy edycja zadania przebiegła poprawnie
    public LiveData<String> getTaskEditResultLiveData() {
        return taskEditResultLiveData;
    }

    // Do listenera w TaskEditActivity, zwraca komunikat błędu który wystąpił podczas edycji zadania
    public LiveData<String> getTaskEditErrorLiveData() {
        return taskEditErrorLiveData;
    }

    // Do listenera w TaskEditActivity, zwraca dane zadania
    public LiveData<Task> getTaskData() {
        return taskData;
    }

    // Wczytuje dane zadania z repozytorium do lokalnego rekordu
    public void loadTaskData(int taskID) {
        Task task = taskRepository.getTaskById(taskID);
        taskData.setValue(task);
    }

    // Weryfikuje czy dane do edycji zadania są poprawne, następnie przekazuje je do repozytorium
    public void updateUserTask(
            int taskID,
            String editedTaskName,
            String editedTaskCategory,
            String editedTaskDescription,
            int editedTaskPointsReward
    ) {
        if (editedTaskName.isEmpty()) {
            taskEditErrorLiveData.postValue("Proszę wprowadzić nową nazwę dla zadania");
            return;
        }

        if (editedTaskCategory.isEmpty()) {
            taskEditErrorLiveData.postValue("Proszę wprowadzić nową kategorię dla zadania");
            return;
        }

        if (editedTaskDescription.isEmpty()) {
            taskEditErrorLiveData.postValue("Proszę wprowadzić nowy opis dla zadania");
            return;
        }

        if (editedTaskPointsReward < 0 || editedTaskPointsReward > 100) {
            taskEditErrorLiveData.postValue("Proszę wprowadzić nową punktację dla zadania z zakresu od 0 do 100");
            return;
        }

        if ( !taskRepository.editTask(
                taskID,
                editedTaskName,
                editedTaskCategory,
                editedTaskDescription,
                editedTaskPointsReward
        ) )  taskEditErrorLiveData.postValue("Wystąpił błąd podczas edycji zadania");
        else taskEditResultLiveData.postValue("Zadanie zostało poprawnie edytowane");
    }

    // Przekazuje ID usuwanego zadania do repozytorium i zwraca komunikat
    public void deleteUserTask(int taskID) {
        if ( !taskRepository.deleteTask(taskID) )
            taskEditErrorLiveData.postValue("Wystąpił błąd podczas usuwania zadania");
        else taskEditResultLiveData.postValue("Zadanie zostało poprawnie usunięte");
    }

}

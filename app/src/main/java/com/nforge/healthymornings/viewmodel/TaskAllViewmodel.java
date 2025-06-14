package com.nforge.healthymornings.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nforge.healthymornings.model.data.Statistics;
import com.nforge.healthymornings.model.repository.StatisticsRepository;


public class TaskAllViewmodel extends AndroidViewModel {

    private final StatisticsRepository statisticRepository;
    private final MutableLiveData<Statistics> statisticsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> taskLoadErrorLiveData = new MutableLiveData<>();

    public TaskAllViewmodel(@NonNull Application application) {
        super(application);
        statisticRepository = new StatisticsRepository(application.getApplicationContext());
    }

    public LiveData<Statistics> getStatisticsLiveData() {
        return statisticsLiveData;
    }

    public LiveData<String> getTaskLoadErrorLiveData() {
        return taskLoadErrorLiveData;
    }

    public void loadAllTasksForCurrentUser() {
        try {
            Statistics statistics = statisticRepository.getCurrentUserStatistics();
            if (statistics != null) {
                statisticsLiveData.postValue(statistics);
            } else {
                taskLoadErrorLiveData.postValue("Brak danych do wyświetlenia.");
            }
        } catch (Exception e) {
            Log.e("TaskAllViewmodel", "loadAllTasksForCurrentUser(): " + e.getMessage());
            taskLoadErrorLiveData.postValue("Wystąpił błąd podczas wczytywania danych.");
        }
    }
}

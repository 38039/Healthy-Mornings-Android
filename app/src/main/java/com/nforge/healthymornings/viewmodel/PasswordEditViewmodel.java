package com.nforge.healthymornings.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.repository.UserRepository;

import org.jetbrains.annotations.NotNull;

public class PasswordEditViewmodel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<User>   userData             = new MutableLiveData<>();
    private final MutableLiveData<String> changePasswordResult = new MutableLiveData<>();


    public PasswordEditViewmodel(@NotNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<String> getChangePasswordResult() {
        return changePasswordResult;
    }

    // Wczytuje dane użytkownika z repozytorium do lokalnego rekordu
    public void loadUserData() {
        User user = userRepository.getUserCredentials();
        userData.setValue(user);
    }

    // Do listenera w PasswordEditActivity,
    // zwraca dane użytkownika
    public LiveData<User> getUserData() {
        return userData;
    }

    public void changePassword(String password, String confirmPassword) {
        if (password.isEmpty()) {
            changePasswordResult.setValue("Proszę podać nowe hasło");
            return;
        }

        if (confirmPassword.isEmpty() ) {
            changePasswordResult.setValue("Proszę podać hasło ponownie");
            return;
        }

        if (!password.equals(confirmPassword)) {
            changePasswordResult.setValue("Podane hasła nie są takie same");
            return;
        }

        if (password.length() < 6) {
            changePasswordResult.setValue("Nowe hasło musi mieć co najmniej 6 znaków");
            return;
        }

        if( !userRepository.changeUserPassword(password) ) {
            changePasswordResult.setValue("Wystąpił błąd podczas zmiany hasła");
            return;
        }

        changePasswordResult.setValue("Hasło zmienione pomyślnie");
    }
}

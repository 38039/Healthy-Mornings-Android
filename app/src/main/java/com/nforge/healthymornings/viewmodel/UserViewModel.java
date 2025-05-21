// Przechowuje status logowania
// Zwraca dane sesji i zalogowanego użytkownika
package com.nforge.healthymornings.viewmodel;

// ANDROID
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

// HEALTHY MORNINGS
import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.repository.UserRepository;
import com.nforge.healthymornings.model.utils.SessionManager;


public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;           // Odwołanie do repozytorium użytkownika
    //private final SessionManager sessionManager;           // Odwołanie do aktualnej sesji aplikacji
    public MutableLiveData<Boolean> loginResultLiveData  = new MutableLiveData<>();
    public MutableLiveData<String>  loginErrorLiveData   = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        //sessionManager = new SessionManager(null);
    }


    // Logika po aktywowaniu przycisku
    public void loginUser(String email, String password) {
        // Weryfikacja czy użytkownik istnieje w repozytorium
        if ( !userRepository.authenticateUser(email, password) )
            loginErrorLiveData.postValue("NIEPOPRAWNE DANE LOGOWANIA");

        // Zapis do sesji
        //sessionManager.saveUser();
        //if ( !sessionManager.saveUser(verifiedUser.getIdUser()) )
        //    loginErrorLiveData.postValue("NIE UDAŁO SIĘ ZAPISAĆ SESJI");

        loginResultLiveData.postValue(true);
    }

    // Do listenera w LoginActivity, zwraca czy użytkownik jest zalogowany
    public LiveData<Boolean> getLoginResultLiveData() {
        return loginResultLiveData;
    }

    // Do listenera w LoginActivity, zwraca komunikat błędu który wystąpił podczas logowania
    public LiveData<String> getLoginErrorLiveData() {
        return loginErrorLiveData;
    }


    // Wylogowanie użytkownika
    /*
    public void logoutUser() {
        if (!sessionManager.clearSession()) {
            Log.e("UserViewModel", "logoutUser(): NIE UDAŁO SIĘ WYCZYŚCIĆ SESJI");
            Toast.makeText(this.getApplication(), "Wystąpił problem z wylogowaniem", Toast.LENGTH_SHORT).show();
        }
    }*/

    // Sprawdza czy użytkownik jest zalogowany
    public boolean isUserLoggedIn() {
        return userRepository.getUser() != null;
    }

    // Zwraca dane użytkownika na podstawie jego identyfikatora
    public User getLoggedInUser() {
        return userRepository.getUser();
    }
}

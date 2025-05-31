// Przechowuje status logowania
// Weryfikuje czy logowanie przebiegło pomyślnie
package com.nforge.healthymornings.viewmodel;

// ANDROID
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

// HEALTHY MORNINGS
import com.nforge.healthymornings.model.repository.UserRepository;


public class LoginViewmodel extends AndroidViewModel {
    private final UserRepository userRepository;
    public MutableLiveData<String> loginResultLiveData  = new MutableLiveData<>();
    public MutableLiveData<String> loginErrorLiveData   = new MutableLiveData<>();


    public LoginViewmodel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Do listenera w LoginActivity, zwraca czy użytkownik jest zalogowany
    public LiveData<String> getLoginResultLiveData() {
        return loginResultLiveData;
    }

    // Do listenera w LoginActivity, zwraca komunikat błędu który wystąpił podczas logowania
    public LiveData<String> getLoginErrorLiveData() {
        return loginErrorLiveData;
    }


    // Logowanie użytkownika i sprawdzanie czy przebiegło ono pomyślnie
    public void loginUser(String accountLoginEmail, String accountLoginPassword) {
        // Zabezpieczenie przed nie podaniem danych logowania
        if ( accountLoginEmail.isEmpty() || accountLoginPassword.isEmpty() ) {
            loginErrorLiveData.postValue("Brakujące dane logowania");
            return;
        }

        // Weryfikacja czy użytkownik istnieje w repozytorium
        if ( !userRepository.authenticateUser(accountLoginEmail, accountLoginPassword) ) {
            loginErrorLiveData.postValue("Niepoprawne dane logowania");
            return;
        }

        loginResultLiveData.postValue("Użytkownik został pomyślnie zalogowany");
    }
}

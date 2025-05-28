package com.nforge.healthymornings.viewmodel;

// JAVA
import java.sql.Date;
import org.jetbrains.annotations.NotNull;
// ANDROID
import android.app.Application;
import android.app.DatePickerDialog;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
// HEALTHY MORNINGS
import com.nforge.healthymornings.model.repository.UserRepository;


public class RegisterViewmodel extends AndroidViewModel {
    private final UserRepository userRepository;
    public MutableLiveData<String> registerResultLiveData  = new MutableLiveData<>();
    public MutableLiveData<String>  registerErrorLiveData   = new MutableLiveData<>();


    public RegisterViewmodel(@NotNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    // Do listenera w RegisterActivity, zwraca czy użytkownik jest zalogowany
    public LiveData<String> getRegisterResultLiveData() {
        return registerResultLiveData;
    }

    // Do listenera w RegisterActivity, zwraca komunikat błędu który wystąpił podczas logowania
    public LiveData<String> getRegisterErrorLiveData() {
        return registerErrorLiveData;
    }


    // Logowanie użytkownika i sprawdzanie czy przebiegło ono pomyślnie
    public void registerUser(String accountRegisterUsername, String accountRegisterEmail, String accountRegisterPassword, String accountConfirmPassword, Date birthDate, DatePickerDialog datePickerDialog) {
        // Zabezpieczenie przed brakiem danych do rejestracji
        if ( accountRegisterUsername.isEmpty() || accountRegisterEmail.isEmpty() || accountRegisterPassword.isEmpty() || accountConfirmPassword.isEmpty() ) {
            registerErrorLiveData.postValue("Proszę podać wszystkie potrzebne informacje");
            return;
        }

        // Aplikacja sprawdzi czy hasła są takie same
        if (!accountRegisterPassword.equals(accountConfirmPassword)) {
            registerErrorLiveData.postValue("Proszę podać takie same hasła");
            return;
        }

        // Aplikacja odmówi rejestracji użytkownikowi poniżej 12-13 lat
        if (datePickerDialog.getDatePicker().getYear() > 2012) {
            registerErrorLiveData.postValue("Aby się zarejestrować użytkownić musi być powyżej 12 roku życia");
            return;
        }

        // Weryfikacja czy użytkownik istnieje w repozytorium
        if ( userRepository.doesUserExistInDatabase(accountRegisterEmail, accountRegisterUsername) ) {
            registerErrorLiveData.postValue("Użytkownik o podanej nazwie, bądź adresie email już istnieje");
            return;
        }

        // Rejestracja użytkownika w bazie danych
        if ( !userRepository.saveUserCredentials(accountRegisterUsername, accountRegisterEmail, accountRegisterPassword, birthDate) ) {
            registerErrorLiveData.postValue("Wystąpił problem z zapisem danych użytkownika do bazy danych");
            return;
        }

        registerResultLiveData.postValue("Użytkownik został pomyślnie zarejestrowany");
    }

}

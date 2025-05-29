package com.nforge.healthymornings.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.nforge.healthymornings.model.data.User;
import com.nforge.healthymornings.model.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;


public class AccountEditViewmodel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<User> userData    = new MutableLiveData<>();
    public MutableLiveData<String> userEditResult = new MutableLiveData<>();
    public MutableLiveData<String> userEditError  = new MutableLiveData<>();


    public AccountEditViewmodel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
    }

    // Do listenera w AccountEditActivity,
    // zwraca czy edycja zadania przebiegła poprawnie
    public LiveData<String> getUserEditResult() {
        return userEditResult;
    }

    // Do listenera w AccountEditActivity,
    // zwraca komunikat błędu który wystąpił podczas edycji konta użytkownika
    public LiveData<String> getUserEditError() {
        return userEditError;
    }

    // Do listenera w AccountEditActivity,
    // zwraca dane użytkownika
    public LiveData<User> getUserData() {
        return userData;
    }

    // Wczytuje dane użytkownika z repozytorium do lokalnego rekordu
    public void loadUserData() {
        User user = userRepository.getUserCredentials();
        userData.setValue(user);
    }

    public void updateUserAccount(
            String          name,
            String          surname,
            String          gender,
            String          username,
            String          email,
            String          bio,
            double          height,
            double          weight
    ) {
        if (name.isEmpty()) {
            userEditError.postValue("Proszę wprowadzić nowe imię");
            return;
        }

        if (surname.isEmpty()) {
            userEditError.postValue("Proszę wprowadzić nowe nazwisko");
            return;
        }

        if (username.isEmpty()) {
            userEditError.postValue("Proszę wprowadzić nowe nazwę użytkownika");
            return;
        }

        if (email.isEmpty()) {
            userEditError.postValue("Proszę wprowadzić nowy adres e-mail");
            return;
        }

        // Bio może być puste
//        if (bio.isEmpty()) {
//            userEditError.postValue("Proszę wprowadzić nowy opis");
//            return;
//        }

        if (height < 0 || height > 300) {
            userEditError.postValue("Proszę wprowadzić dane wzrostu z zakresu od 0 do 300");
            return;
        }

        if (weight < 0 || weight > 300) {
            userEditError.postValue("Proszę wprowadzić dane wagi z zakresu od 0 do 300");
            return;
        }


        Map<String, String> searchCriteria = new HashMap<>();

        // Sprawdza czy użytkownik podał nowy username
        if ( !username.equals(userData.getValue().getUsername()) )
            searchCriteria.put("username", username);

        // Sprawdza czy użytkownik podał nowy adres email
        if ( !email.equals(userData.getValue().getEmail()) )
            searchCriteria.put("email", email);

        // Sprawdza czy nowa nazwa / adres email nie są już używane w bazie danych
        if( userRepository.doesUserExistInDatabase(searchCriteria) ) {
            userEditError.postValue("Użytkownik o podanej nazwie, bądź adresie email już istnieje");
            return;
        }


         if( !userRepository.changeUserCredentials(
                 name, surname, gender, username,
                 email, bio, height, weight
         ) ) userEditError.postValue("Wystąpił błąd podczas edycji konta użytkownika");
         else userEditResult.postValue("Konto użytkownika zostało poprawnie edytowane");
    }
}

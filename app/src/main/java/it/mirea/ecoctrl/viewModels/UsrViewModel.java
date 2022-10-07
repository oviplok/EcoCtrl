package it.mirea.ecoctrl.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import it.mirea.ecoctrl.models.User;
import it.mirea.ecoctrl.repositories.UserFireBaseRepository;

public class UsrViewModel extends AndroidViewModel {

    private UserFireBaseRepository userFireBaseRepository;
    public LiveData<User> userLiveData;

    public UsrViewModel(@NonNull Application application) {
        super(application);
        userFireBaseRepository = new UserFireBaseRepository();
    }
/*
    public static String back_check(String lvl) {
        if(lvl.equals("red")){
            return "red";
        }
        else if(lvl.equals("green")){
            return "green";
        }
        else{
            return "error";
        }
    }*/

    public void Check_acc(String email) {
        userLiveData = userFireBaseRepository.Check_acc(email);
    }

    public void Change_pass(String email, String oldPass, String newPass) {
        userLiveData = userFireBaseRepository.Change_pass(email,oldPass,newPass);
    }
}

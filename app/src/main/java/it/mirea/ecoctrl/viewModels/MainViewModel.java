package it.mirea.ecoctrl.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import it.mirea.ecoctrl.models.User;
import it.mirea.ecoctrl.repositories.UserFireBaseRepository;

public class MainViewModel extends AndroidViewModel {
    private UserFireBaseRepository userFireBaseRepository;
    public LiveData<User> userLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        userFireBaseRepository = new UserFireBaseRepository();
    }

    public void LogInWindow(String email,String password) {

        if(email.equals("anon")){
            userLiveData = userFireBaseRepository.AnonLogIn();
        }
        else {
            userLiveData = userFireBaseRepository.LogIn(email,password);
        }
    }

    public void RegInWindow(String email, String password, String job){
        if(email!=null && password!=null & job!=null){
            Log.e("ViewModel",email+" "+password+" "+job);
            userLiveData = userFireBaseRepository.RegIn(email,password,job);
        }
        else {
            Log.e("ViewModel","Null Elems");
        }
    }
}

package it.mirea.ecoctrl.cutContent;

import android.app.Application;
import android.content.Context;
import android.util.Log;

//import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;

import javax.annotation.Nonnull;

import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.models.UserFireBase;
import it.mirea.ecoctrl.cutContent.UserFireBaseRepository;
import it.mirea.ecoctrl.views.activities.MainActivity;

public class MainViewModel extends AndroidViewModel {


    private UserFireBaseRepository userFireBaseRepository;
    public LiveData<UserFireBase> userLiveData;

    public MainViewModel(@Nonnull Application application) {
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
/*
    public void auth (String token, MainActivity activity) {
        if (token != null) {
            if (ServiceLocator.getInstance().getUser() == null) {
                ServiceLocator.getInstance().getRepository().findUser(
                        activity.getPreferences(Context.MODE_PRIVATE).getString("email", ""),
                        activity
                ).observe(activity, (person) -> {
                    if (person != null) {
                        ServiceLocator.getInstance().setUser(person);



                      //  Navigation.findNavController(activity.mBinding.navHostFragment).navigate(R.id.action_authFragment_to_partyList);
                    } else {
                        activity.getPreferences(Context.MODE_PRIVATE).edit().remove("email").remove("token").apply();
                    }
                });
            }
        }
    }

    public LiveData<User> auth (String login, String password, MainActivity activity) {
        ServiceLocator.getInstance().getRepository().findUser(login, password, activity).observe(activity, (user) -> {
            if (user != null) {
                ServiceLocator.getInstance().setUser(user);
                activity.getPreferences(Context.MODE_PRIVATE).edit().putString("email", user.getEmail());
            }
        });
        return ServiceLocator.getInstance().getRepository().findUser(login, password, activity);
    }*/
}

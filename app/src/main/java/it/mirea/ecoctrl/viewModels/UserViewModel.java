package it.mirea.ecoctrl.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.models.UserFireBase;
import it.mirea.ecoctrl.cutContent.UserFireBaseRepository;
import it.mirea.ecoctrl.views.activities.UsrActivity;

public class UserViewModel extends AndroidViewModel {
    RepoTasks repository;
    public LiveData<User> userLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        ServiceLocator.getInstance().initBase(application);
        this.repository = ServiceLocator.getInstance().getRepository();
    }

    public void Check_acc(String email, UsrActivity activity) {
        userLiveData = repository.findUser(email, activity);
                //userFireBaseRepository.Check_acc(email);
    }

    public void Change_pass(String email, String oldPass, String newPass) {
       // userLiveData = userFireBaseRepository.Change_pass(email,oldPass,newPass);
    }
}

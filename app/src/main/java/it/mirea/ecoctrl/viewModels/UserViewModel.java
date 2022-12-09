package it.mirea.ecoctrl.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

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
    }

    public void Change_pass(String email, String id, String password, String connections, User.Role role, String phone, String first_name, String last_name) {
        User user= new User();
        if(email!=null){
            user.setEmail(email);
            user.setPassword(password);
            user.setId(id);
            user.setRole(role);
        }
        else {
            user.setFirst_name(first_name);
            user.setLast_name(last_name);
            user.setPhone(phone);
            user.setRole(role);
            user.getConnections().put("vk", connections);
            user.setPassword(password);
            user.setId(id);

        }
        ServiceLocator.getInstance().getRepository().updateUser(user);
    }
}

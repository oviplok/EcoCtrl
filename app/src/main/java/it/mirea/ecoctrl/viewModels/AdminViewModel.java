package it.mirea.ecoctrl.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.models.Place;
import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.models.UserDTO;
import it.mirea.ecoctrl.views.activities.UsrActivity;

public class AdminViewModel extends AndroidViewModel {

    RepoTasks repository;
    public LiveData<List<User>> adminLiveData;

    public AdminViewModel(@NonNull Application application) {
        super(application);
        ServiceLocator.getInstance().initBase(application);
        this.repository = ServiceLocator.getInstance().getRepository();
    }

    public  void getAllUsers(){
        adminLiveData =ServiceLocator.getInstance().getRepository().getAllUsers();
    }
    public void deleteUser(int position, List<User> user){
        ServiceLocator.getInstance().getRepository().deleteUser(user.get(position));
    }
}

package it.mirea.ecoctrl.repositories.room;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.models.PlaceDTO;
import it.mirea.ecoctrl.domain.models.Place;
import it.mirea.ecoctrl.repositories.models.UserDTO;
import it.mirea.ecoctrl.repositories.room.DAO.PlaceDAO;
import it.mirea.ecoctrl.repositories.room.DAO.UserDAO;

public class MapRoomRepository implements RepoTasks {
    private PlaceDAO placeDAO;
    private UserDAO userDAO;
    LiveData<List<UserDTO>> allUsers;
    private LiveData<List<PlaceDTO>> allPlaces;
    private LiveData<PlaceDTO> searchPlace;

    public MapRoomRepository(Application application){
        MapRoomDatabase db = MapRoomDatabase.getInstance(application);
        placeDAO= db.placeDAO();
        userDAO = db.userDAO();
        allPlaces = placeDAO.getAllPlaces();
        allUsers = userDAO.getAllPeople();
    }
    public LiveData<List<PlaceDTO>> getAllPlaces(){ return allPlaces; }


    public LiveData<PlaceDTO> findPlace(String place_name, LifecycleOwner owner) {
        searchPlace = placeDAO.getPlace(place_name);
        return searchPlace;
    }

    public void addPlace(Place place){
       PlaceDTO placeDTO = PlaceDTO.convertFromPlace(place);
       MapRoomDatabase.databaseWriteExecutor.execute(()->{
           placeDAO.addPlace(placeDTO);
        });
    }

    public  void deletePlace(Place place){
        PlaceDTO placeDTO = PlaceDTO.convertFromPlace(place);
        MapRoomDatabase.databaseWriteExecutor.execute(()->{
            placeDAO.deletePlace(placeDTO);
        });
    }

    public  void changePlace(Place place){
        PlaceDTO placeDTO = PlaceDTO.convertFromPlace(place);
        MapRoomDatabase.databaseWriteExecutor.execute(()->{
            placeDAO.changePlace(placeDTO);
        });
    }

    public  void deleteUser(User user){
        UserDTO userDTO = UserDTO.convertFromUser(user);
        MapRoomDatabase.databaseWriteExecutor.execute(()->{
            userDAO.deleteUser(userDTO);
        });
    }

    public LiveData<List<UserDTO>> getAllUsers(){ return allUsers; }

    @Override
    public void addUser(User user) {
        UserDTO userDTO = UserDTO.convertFromUser(user);
        MapRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.addUser(userDTO);
        });
    }

    @Override
    public void updateUser(User user) {
        UserDTO userDTO = UserDTO.convertFromUser(user);

        MapRoomDatabase.databaseWriteExecutor.execute(() -> {
           userDAO.updatePersonInfo(userDTO);
        });
    }

    @Override
    public LiveData<UserDTO> findUser(String email, LifecycleOwner owner) {
        MutableLiveData<UserDTO> answer = new MutableLiveData<>();

        userDAO.getUserByEmail(email).observe(owner, answer::setValue);

        return answer;
    }

    @Override
    public LiveData<UserDTO> findUser(String email, String password, LifecycleOwner owner) {
        MutableLiveData<UserDTO> answer = new MutableLiveData<>();

        userDAO.getUserByEmailAndPassword(email, password).observe(owner, answer::setValue);

        return answer;
    }

}

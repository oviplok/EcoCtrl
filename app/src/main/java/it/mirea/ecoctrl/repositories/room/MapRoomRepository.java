package it.mirea.ecoctrl.repositories.room;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.repositories.room.DAO.PlaceDAO;

public class MapRoomRepository implements RepoTasks {
    private PlaceDAO placeDAO;
    private LiveData<List<Place>> allPlaces;
    private LiveData<Place> searchPlace;

    public MapRoomRepository(Application application){
        MapRoomDatabase db = MapRoomDatabase.getInstance(application);
        placeDAO= db.placeDAO();
        allPlaces = placeDAO.getAllPlaces();
    }
    public LiveData<List<Place>> getAllPlaces(){ return allPlaces; }

    public LiveData<Place> findPlace(String place_name,LifecycleOwner owner) {
        searchPlace = placeDAO.getPlace(place_name);
       // MutableLiveData<Place> mapLiveData = new MutableLiveData<>();
        return searchPlace;
    }

    public void addPlace(PlaceF placeF){
       Place place = Place.convertFromFire(placeF);
       MapRoomDatabase.databaseWriteExecutor.execute(()->{
           placeDAO.addPlace(place);
        });
       // Log.e("AddPlaceRepo",place.getPlace_name());
    }

    public  void deletePlace(PlaceF placeF){
        Place place = Place.convertFromFire(placeF);
        MapRoomDatabase.databaseWriteExecutor.execute(()->{
            placeDAO.deletePlace(place);
        });
    }

    public  void changePlace(PlaceF placeF){
        Place place = Place.convertFromFire(placeF);
        MapRoomDatabase.databaseWriteExecutor.execute(()->{
            placeDAO.changePlace(place);
        });
    }

}

package it.mirea.ecoctrl.repositories.room;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.repositories.room.DAO.PlaceDAO;

public class MapRoomRepository {
    private PlaceDAO placeDAO;
    private LiveData<List<Place>> allPlaces;
    private LiveData<Place> searchPlace;

    public MapRoomRepository(Application application){
        MapRoomDatabase db = MapRoomDatabase.getInstance(application);
        placeDAO=db.placeDAO();
        allPlaces = placeDAO.getAllPlaces();


    }
    public void getAllPlaces(){
        Log.e("msg",allPlaces.toString());
    }


    public LiveData<Place> findPlace(String place_name,LifecycleOwner owner) {
        searchPlace = placeDAO.getPlace(place_name);
        MutableLiveData<Place> mapLiveData = new MutableLiveData<>();
        return searchPlace;
    }



    public void  AddPlace(PlaceF placeF){
       Place place = Place.convertFromFire(placeF);
       MapRoomDatabase.databaseWriteExecutor.execute(()->{
           placeDAO.addPlace(place);
        });
        Log.e("AddPlaceRepo",place.getPlace_name());
    }

    public  void ChangePlace(PlaceF placeF){
        Place place = Place.convertFromFire(placeF);
        MapRoomDatabase.databaseWriteExecutor.execute(()->{
            placeDAO.changePlace(place);
        });
    }

}

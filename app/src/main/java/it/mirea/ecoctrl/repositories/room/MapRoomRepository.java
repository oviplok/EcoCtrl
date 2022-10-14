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
        //return allPlaces;
    }
    //MutableLiveData<Place>


    public LiveData<Place> findPlace(String place_name,LifecycleOwner owner) {
        searchPlace = placeDAO.getPlace(place_name);
        MutableLiveData<Place> mapLiveData = new MutableLiveData<>();
        //MapRoomDatabase.databaseWriteExecutor.execute(() -> {
          //         placeDAO.getPlace(place_name);
        //});
       // mapLiveData.setValue(List<Place> );
       /* searchPlace.observe(owner, (Place places) -> {
            mapLiveData.setValue(places.stream()
                    .filter(place -> place_name.equals(place.getPlace_name()))
                    .findAny()
                    .orElse(null)
            );
        });*/
        //mapLiveData.setValue(placeDAO.getPlace(place_name));
        return searchPlace;
    }
       /* allPlaces.observe(owner, (List<Place> places) -> {
            mapLiveData.setValue(places.stream()
                    .filter(place -> place_name.equals(place.getPlace_name()))
                    .findAny()
                    .orElse(null)
            );
    });*/
       // mapLiveData.setValue();



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

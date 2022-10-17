package it.mirea.ecoctrl.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.Map;

import it.mirea.ecoctrl.domain.operations.PlaceOps;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.fireBase.MapFireBaseRepository;
import it.mirea.ecoctrl.domain.models.PlaceF;
import it.mirea.ecoctrl.repositories.room.MapRoomRepository;

public class MapViewModel extends AndroidViewModel {
    PlaceOps placeOps =new PlaceOps();
    private MapRoomRepository mapRoomRepository;
    private MapFireBaseRepository mapFireBaseRepository;



    public LiveData<Place> placeLiveData;
   // public LiveData<Place> placeFavLiveData;
    public LiveData<PlaceF> placeFireLiveData;

    public void showAll(){
        mapRoomRepository.getAllPlaces();
    }

    public MapViewModel(@NonNull Application application) {
        super(application);
        mapRoomRepository = new MapRoomRepository(application);
        mapFireBaseRepository = new MapFireBaseRepository();
    }

    public void showPlace(String search, String intern, LifecycleOwner owner) {

            if (intern.equals("off")) {
                placeLiveData = mapRoomRepository.findPlace(search, owner);

            } else {
                placeFireLiveData = mapFireBaseRepository.SearchPlace(search);
                placeLiveData = mapRoomRepository.findPlace(search, owner);
            }
    }


    public void changePlace(String chPlace, String chMetan,
                            String chSerd, String chAzd,String intern,boolean fav) {
        Place place=new Place();
        PlaceF placeF = PlaceOps.insertInfo(chPlace,chMetan,chSerd,chAzd,"","",fav);
        if(intern.equals("off"))
        {
            mapRoomRepository.ChangePlace(placeF);
        }
        else {
            mapRoomRepository.ChangePlace(placeF);
            Map<String, Object> InfoChangeMap = placeOps.MakeMap(chPlace,chMetan,chSerd,chAzd,"","");
            placeFireLiveData = mapFireBaseRepository.ChangePlace(chPlace, InfoChangeMap);
        }
    }

    public void AddPlace(String addPlace, String addMetan,
                         String addSerd, String addAzd, String addLng, String addLat,boolean fav, String intern) {
        Log.e("AddPlace",addPlace);
        PlaceF placeF = PlaceOps.insertInfo(addPlace,addMetan,addSerd,addAzd,addLng,addLat,fav);

        if(intern.equals("off"))
        {
            mapRoomRepository.AddPlace(placeF);

        }
        else{
            Log.e("AddPlaceIN",addPlace);
            Map<String, Object> InfoChangeMap = placeOps.MakeMap(addPlace,addMetan,addSerd,addAzd,addLng,addLat);

            mapRoomRepository.AddPlace(placeF);
            placeFireLiveData = mapFireBaseRepository.AddPlace(addPlace,InfoChangeMap);
            }
    }
}

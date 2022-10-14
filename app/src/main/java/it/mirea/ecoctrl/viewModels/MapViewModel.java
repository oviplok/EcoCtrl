package it.mirea.ecoctrl.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import it.mirea.ecoctrl.domain.PlaceOps;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.fireBase.MapFireBaseRepository;
import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.repositories.room.MapRoomRepository;

public class MapViewModel extends AndroidViewModel {
    PlaceOps placeOps =new PlaceOps();
    private MapRoomRepository mapRoomRepository;
    private MapFireBaseRepository mapFireBaseRepository;



    public LiveData<Place> placeLiveData;
    public LiveData<PlaceF> placeFireLiveData;
    //private final
  //  public LiveData<List<Place>> getPlace;
   // private final LiveData<List<Place>> mAllWords;
    private final MutableLiveData<String> toSee = new MutableLiveData<>();
    //private final LiveData<List<Place>> places = Transformations.switchMap(toSee, String See ->
    //        mapRoomRepository.findPlace(See));


    public LiveData<Place> getPlaceLiveData() {
        return placeLiveData;
    }

    public void showAll(){
        mapRoomRepository.getAllPlaces();
    }

    public MapViewModel(@NonNull Application application) {
        super(application);
        mapRoomRepository = new MapRoomRepository(application);
        mapFireBaseRepository = new MapFireBaseRepository();
        //getPlace= mapFireBaseRepository.g

    }

    public void showPlace(String search,String intern,LifecycleOwner owner) {
        if(intern.equals("off")){
            placeLiveData = mapRoomRepository.findPlace(search, owner);

        }else
        {
            placeLiveData = mapRoomRepository.findPlace(search, owner);
          //  mapRoomRepository.findPlace(search, owner);
           // placeFireLiveData = mapFireBaseRepository.SearchPlace(search);
        }
    }


    public void changePlace(String chPlace, String chMetan, String chSerd, String chAzd,String intern) {
        Place place=new Place();
        PlaceF placeF = PlaceOps.insertInfo(chPlace,chMetan,chSerd,chAzd,"","");
        if(intern.equals("off"))
        {
            //placeOp.insertInfo(chPlace,chMetan,chSerd,chAzd,"","");
            mapRoomRepository.ChangePlace(placeF);
           // placeOp.insertInfo(chPlace);
        }
        else {
           // placeOp.insertInfo(chPlace,chMetan,chSerd,chAzd,"","");
            mapRoomRepository.ChangePlace(placeF);
            Map<String, Object> InfoChangeMap = placeOps.MakeMap(chPlace,chMetan,chSerd,chAzd,"","");
            placeFireLiveData = mapFireBaseRepository.ChangePlace(chPlace, InfoChangeMap);
        }
    }

    public void AddPlace(String addPlace, String addMetan,
                         String addSerd, String addAzd, String addLng, String addLat,String intern) {
        //Place place = new Place();
        Log.e("AddPlace",addPlace);
        PlaceF placeF = PlaceOps.insertInfo(addPlace,addMetan,addSerd,addAzd,addLng,addLat);

        if(intern.equals("off"))
        {
            mapRoomRepository.AddPlace(placeF);

        }
        else{
            Log.e("AddPlaceIN",addPlace);
            Map<String, Object> InfoChangeMap = placeOps.MakeMap(addPlace,addMetan,addSerd,addAzd,addLng,addLat);

            mapRoomRepository.AddPlace(placeF);
            placeFireLiveData = mapFireBaseRepository.AddPlace(addPlace,InfoChangeMap);
          //  Log.e("AddPlaceIN","fire_oke");
            }
    }
}

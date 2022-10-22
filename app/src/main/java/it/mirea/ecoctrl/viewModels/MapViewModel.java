package it.mirea.ecoctrl.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.Map;

import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.domain.operations.PlaceOps;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.fireBase.MapFireBaseRepository;
import it.mirea.ecoctrl.repositories.models.PlaceF;

public class MapViewModel extends AndroidViewModel {
    PlaceOps placeOps =new PlaceOps();
    //private MapRoomRepository mapRoomRepository;
    RepoTasks repository;
    private MapFireBaseRepository mapFireBaseRepository;



    public LiveData<Place> placeLiveData;
    public LiveData<PlaceF> placeFireLiveData;


    public MapViewModel(@NonNull Application application) {
        super(application);
        ServiceLocator.getInstance().initBase(application);
        this.repository = ServiceLocator.getInstance().getRepository();
        //mapRoomRepository = new MapRoomRepository(application);
        mapFireBaseRepository = new MapFireBaseRepository();
    }

    public void showPlace(String search, String intern, LifecycleOwner owner) {

            if (intern.equals("off")) {
                placeLiveData = repository.findPlace(search, owner);
              //  placeLiveData = mapRoomRepository.findPlace(search, owner);

            } else {
                placeFireLiveData = mapFireBaseRepository.SearchPlace(search);
             //   placeLiveData = mapRoomRepository.findPlace(search, owner);
                placeLiveData = repository.findPlace(search, owner);
            }
    }


    public void changePlace(String chPlace, String chMetan,
                            String chSerd, String chAzd,String intern,double chLng,double chLat) {
        String Lng=""+chLng;
        String Lat=""+chLat;
        PlaceF placeF = PlaceOps.insertInfo(chPlace,chMetan,chSerd,chAzd,Lng,Lat);
        if(intern.equals("off"))
        {
            ServiceLocator.getInstance().getRepository().changePlace(placeF);
           // repository.changePlace(placeF);
           // mapRoomRepository.changePlace(placeF);
        }
        else {
            ServiceLocator.getInstance().getRepository().changePlace(placeF);
            // repository.changePlace(placeF);
           // mapRoomRepository.changePlace(placeF);
            Map<String, Object> InfoChangeMap = placeOps.MakeMap(chPlace,chMetan,chSerd,chAzd,"","");
            placeFireLiveData = mapFireBaseRepository.ChangePlace(chPlace, InfoChangeMap);
        }
    }

    public void AddPlace(String addPlace, String addMetan,
                         String addSerd, String addAzd, String addLng, String addLat,boolean fav, String intern) {
        Log.e("AddPlace",addPlace);
        PlaceF placeF = PlaceOps.insertInfo(addPlace,addMetan,addSerd,addAzd,addLng,addLat);

        if(intern.equals("off"))
        {
            ServiceLocator.getInstance().getRepository().addPlace(placeF);
             // repository.addPlace(placeF);
            //mapRoomRepository.addPlace(placeF);

        }
        else{
            Log.e("AddPlaceIN",addPlace);
            Map<String, Object> InfoChangeMap = placeOps.MakeMap(addPlace,addMetan,addSerd,addAzd,addLng,addLat);
            ServiceLocator.getInstance().getRepository().addPlace(placeF);
           // repository.addPlace(placeF);
            //mapRoomRepository.addPlace(placeF);
            placeFireLiveData = mapFireBaseRepository.AddPlace(addPlace,InfoChangeMap);
            }
    }
}

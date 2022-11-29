package it.mirea.ecoctrl.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.mirea.ecoctrl.di.ServiceLocator;
import it.mirea.ecoctrl.repositories.RepoTasks;
import it.mirea.ecoctrl.domain.models.Place;
import it.mirea.ecoctrl.repositories.room.MapRoomRepository;

public class PlistViewModel extends AndroidViewModel {
    private MapRoomRepository mapRoomRepository;
    RepoTasks repository;
    public LiveData<List<Place>> AllLiveData;


    public PlistViewModel(@NonNull Application application) {
        super(application);
        ServiceLocator.getInstance().initBase(application);
        this.repository = ServiceLocator.getInstance().getRepository();
        mapRoomRepository = new MapRoomRepository(application);
    }

    public  void getAllPlaces(){
        AllLiveData =ServiceLocator.getInstance().getRepository().getAllPlaces();
        //AllLiveData = repository.getAllPlaces();
       // AllLiveData = mapRoomRepository.getAllPlaces();
    }
    public void deletePlace(int position,List<Place> place){
        List<Place> placee = place;
        ServiceLocator.getInstance().getRepository().deletePlace(place.get(position));
        //repository.deletePlace(place.get(position));
        //mapRoomRepository.deletePlace(place.get(position));
    }

}

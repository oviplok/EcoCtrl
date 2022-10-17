package it.mirea.ecoctrl.viewModels;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.room.MapRoomRepository;

public class PlistViewModel {
    private MapRoomRepository mapRoomRepository;

    public LiveData<List<Place>> AllLiveData;

    public  void getAllPlaces(){
        AllLiveData = mapRoomRepository.getAllPlaces();
    }

}

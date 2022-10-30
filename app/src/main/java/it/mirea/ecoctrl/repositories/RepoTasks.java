package it.mirea.ecoctrl.repositories;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.mirea.ecoctrl.repositories.models.PlaceF;
import it.mirea.ecoctrl.repositories.models.Place;

public interface RepoTasks {
    <T extends Place> LiveData<List<PlaceF>>  getAllPlaces();
    void addPlace(PlaceF placeF);
    void changePlace(PlaceF placeF);
    void deletePlace(PlaceF placeF);
    <T extends  Place> LiveData<Place> findPlace(String place_name, LifecycleOwner owner);
}

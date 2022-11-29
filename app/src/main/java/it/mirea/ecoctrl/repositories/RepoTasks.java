package it.mirea.ecoctrl.repositories;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.models.PlaceDTO;
import it.mirea.ecoctrl.domain.models.Place;

public interface RepoTasks {
    <T extends PlaceDTO> LiveData<List<Place>>  getAllPlaces();
    void addPlace(Place place);
    void changePlace(Place place);
    void deletePlace(Place place);
    <T extends PlaceDTO> LiveData<PlaceDTO> findPlace(String place_name, LifecycleOwner owner);

    <T extends User> LiveData<T> findUser(String email, LifecycleOwner owner);
    <T extends User> LiveData<T> findUser(String email, String password, LifecycleOwner owner);
    void addUser(User user);
    void updateUser(User user);
}

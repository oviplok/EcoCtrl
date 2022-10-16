package it.mirea.ecoctrl.repositories.room.DAO;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.mirea.ecoctrl.repositories.models.Place;

@Dao
public interface PlaceDAO {

    @Insert
    void addPlace(Place place);

    @Update
    void changePlace(Place place);

    @Delete
    void deletePlace(Place place);

    @Query("SELECT * FROM places")
    LiveData<List<Place>> getAllPlaces();

    @Query("SELECT * FROM places WHERE place LIKE :place")
    LiveData<Place> getPlace(String place);

    //@Query("SELECT * FROM places WHERE fav LIKE :fav")
   // LiveData<List<Place>> findFav(boolean fav);

}

package it.mirea.ecoctrl.repositories.room.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.mirea.ecoctrl.repositories.models.PlaceDTO;

@Dao
public interface PlaceDAO {

    @Insert
    void addPlace(PlaceDTO placeDTO);

    @Update
    void changePlace(PlaceDTO placeDTO);

    @Delete
    void deletePlace(PlaceDTO placeDTO);

    @Query("SELECT * FROM places")
    LiveData<List<PlaceDTO>> getAllPlaces();

    @Query("SELECT * FROM places WHERE place LIKE :place")
    LiveData<PlaceDTO> getPlace(String place);

   // @Query("SELECT * FROM places WHERE fav>0")
   // LiveData<List<Place>> getFav();

}

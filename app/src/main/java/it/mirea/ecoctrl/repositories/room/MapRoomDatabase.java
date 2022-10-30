package it.mirea.ecoctrl.repositories.room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.mirea.ecoctrl.repositories.models.Place;
import it.mirea.ecoctrl.repositories.room.DAO.PlaceDAO;

@Database(entities = {Place.class}, version = 10)
public abstract class MapRoomDatabase extends RoomDatabase {
    public abstract PlaceDAO placeDAO();

    private static volatile MapRoomDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MapRoomDatabase getInstance(Context context){
        if (instance == null) {
            synchronized (MapRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MapRoomDatabase.class, "place_database")
                            //.fallbackToDestructiveMigration()
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }
}

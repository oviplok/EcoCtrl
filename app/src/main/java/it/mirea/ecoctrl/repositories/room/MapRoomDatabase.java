package it.mirea.ecoctrl.repositories.room;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.mirea.ecoctrl.domain.models.User;
import it.mirea.ecoctrl.repositories.models.PlaceDTO;
import it.mirea.ecoctrl.repositories.models.UserDTO;
import it.mirea.ecoctrl.repositories.room.DAO.PlaceDAO;
import it.mirea.ecoctrl.repositories.room.DAO.UserDAO;

@Database(entities = {PlaceDTO.class, UserDTO.class}, version = 10)
public abstract class MapRoomDatabase extends RoomDatabase {
    public abstract PlaceDAO placeDAO();
    public abstract UserDAO userDAO();

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
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);

                                    databaseWriteExecutor.execute( () -> {
                                                UserDTO admin = new UserDTO();
                                                admin.setEmail("admin@mirea.ru");
                                                admin.setPassword("admin");
                                                admin.setRole(User.Role.Admin);

                                                getInstance(context).userDAO().addUser(admin);

                                                UserDTO moder = new UserDTO();
                                                moder.setEmail("moder@mirea.ru");
                                                moder.setPassword("moder");
                                                moder.setRole(User.Role.Moder);

                                                getInstance(context).userDAO().addUser(moder);

                                                UserDTO moder2 = new UserDTO();
                                                moder2.setEmail("moder2@mirea.ru");
                                                moder2.setPassword("moder");
                                                moder2.setRole(User.Role.Moder);

                                                getInstance(context).userDAO().addUser(moder2);

                                                UserDTO user = new UserDTO();
                                                user.setEmail("egorov.n.v@edu.mirea.ru");
                                                user.setPassword("nikita");
                                                user.setRole(User.Role.User);

                                                getInstance(context).userDAO().addUser(user);

                                                UserDTO user2 = new UserDTO();
                                                user2.setEmail("user@mirea.ru");
                                                user2.setPassword("user");
                                                user2.setRole(User.Role.User);

                                                getInstance(context).userDAO().addUser(user2);


                                            }
                                    );
                                }
                            })
                            //.fallbackToDestructiveMigration()
                            //.allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }
}

package it.mirea.ecoctrl.repositories.room.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.mirea.ecoctrl.repositories.models.UserDTO;

@Dao
public interface UserDAO {

    @Insert
    void addUser(UserDTO user);

    @Delete
    void deleteUser(UserDTO user);

    @Query("SELECT * FROM user WHERE email = :email")
    LiveData<UserDTO> getUserByEmail(String email);

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    LiveData<UserDTO> getUserByEmailAndPassword(String email, String password);

    @Query("SELECT * FROM user")
    LiveData<List<UserDTO>> getAllUsers();

    @Update
    void updatePersonInfo(UserDTO user);
}

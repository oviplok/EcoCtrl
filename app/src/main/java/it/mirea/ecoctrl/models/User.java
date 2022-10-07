package it.mirea.ecoctrl.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class User implements Serializable {
    public String job, email, password, lvl;
    public static String callPath = "Users";

    @Exclude
    public boolean usrResult;

    public User(){
    }

    public User(String email,String password,String job,String lvl, boolean usrResult){
        this.email = email;
        this.password = password;
        this.job = job;
        this.lvl = lvl;
        this.usrResult = usrResult;
    }
}


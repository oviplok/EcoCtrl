package it.mirea.ecoctrl.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class User implements Serializable {
    private String job, email, password, lvl;
    private static String callPath = "Users";

    @Exclude
    private boolean usrResult;

    public User(){
    }

    public User(String email,String password,String job,String lvl, boolean usrResult){
        this.email = email;
        this.password = password;
        this.job = job;
        this.lvl = lvl;
        this.usrResult = usrResult;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLvl() {
        return lvl;
    }

    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public static String getCallPath() {
        return callPath;
    }

  //  public static void setCallPath(String callPath) {
   //     User.callPath = callPath;
   // }

    public boolean isUsrResult() {
        return usrResult;
    }

    public void setUsrResult(boolean usrResult) {
        this.usrResult = usrResult;
    }
}


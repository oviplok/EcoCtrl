package it.mirea.ecoctrl.Models;

public class User {
    private String job, email, password;

    public User() {}

    public User(String job, String email, String password) {
        this.password = password;
        this.email = email;
        this.job = job;
    }



    //получ уровень
    public String getJob() {
        return job;
    }

    //установ уровень
    public void setJob(String lvl) {
        this.job = lvl;
    }

    //получ почту
    public String getEmail() {
        return email;
    }

    //установ почту
    public void setEmail(String email) {
        this.email = email;
    }

    // получ пароль
    public String getPassword() {
        return password;
    }

    //устан пароль
    public void setPassword(String password) {
        this.password = password;
    }
}

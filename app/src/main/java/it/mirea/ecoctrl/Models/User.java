package it.mirea.ecoctrl.Models;

public class User {
    private String lvl, email, password;

    public User() {}

    public User(String name, String email, String password) {
        this.password = password;
        this.email = email;
        this.lvl = name;
    }

    //получ уровень
    public String getLvl() {
        return lvl;
    }

    //установ уровень
    public void setLvl(String lvl) {
        this.lvl = lvl;
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

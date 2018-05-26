package com.example.yash.homedrivesecond.Profile;

import java.io.Serializable;

/**
 * Created by Yash on 27-02-2018.
 */

public class UserBean implements Serializable {

    String name , email , password;

    public UserBean(){
        name = "";
        email = "";
        password = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}

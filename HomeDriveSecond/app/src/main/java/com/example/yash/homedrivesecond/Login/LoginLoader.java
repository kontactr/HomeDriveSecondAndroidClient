package com.example.yash.homedrivesecond.Login;

import android.content.Context;
import android.content.AsyncTaskLoader;

/**
 * Created by Yash on 04-02-2018.
 */

public class LoginLoader extends AsyncTaskLoader {

    String Username , Password;

    public LoginLoader(Context context , String Username , String Password) {
        super(context);
        this.Username = Username;
        this.Password = Password;
    }

    @Override
    public Object loadInBackground() {
        return UtilMethods.getLogin(Username,Password);
    }
}

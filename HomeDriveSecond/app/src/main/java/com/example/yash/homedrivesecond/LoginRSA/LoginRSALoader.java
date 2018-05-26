package com.example.yash.homedrivesecond.LoginRSA;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yash on 04-03-2018.
 */

public class LoginRSALoader extends AsyncTaskLoader {

    String password , session;

    public LoginRSALoader(Context context , String password) {
        super(context);
        this.password = password;
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",context.MODE_PRIVATE);
        session = sharedPreferences.getString("Session","");
        }

    @Override
    public Object loadInBackground() {
        return LoginRSAUtil.getRSA(session,password);

    }
}

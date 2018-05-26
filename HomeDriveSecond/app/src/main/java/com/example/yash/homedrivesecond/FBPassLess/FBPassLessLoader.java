package com.example.yash.homedrivesecond.FBPassLess;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

/**
 * Created by Yash on 02-03-2018.
 */

public class FBPassLessLoader extends AsyncTaskLoader {

    String username , password , email;


    public FBPassLessLoader(Context context, String username, String password, String email) {
        super(context);
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Object loadInBackground() {
        String session = FBPassLessUtil.getPasswordLessLogin(username,password,email);
        Log.i("Session",""+session);
        return session;
    }
}

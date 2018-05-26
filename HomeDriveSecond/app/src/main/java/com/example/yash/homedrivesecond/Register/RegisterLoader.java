package com.example.yash.homedrivesecond.Register;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Yash on 28-02-2018.
 */

public class RegisterLoader extends AsyncTaskLoader {

    String username , password , email;


    public RegisterLoader(Context context , String username , String password , String email) {
        super(context);
        this.email = email;
        this.password = password;
        this.username = username;
    }

    @Override
    public Object loadInBackground() {
        return RegisterUtil.firstStepRegister(username,password,email);
    }
}

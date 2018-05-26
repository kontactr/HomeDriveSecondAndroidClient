package com.example.yash.homedrivesecond.Profile;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Yash on 27-02-2018.
 */

public class ProfileLoader extends AsyncTaskLoader<UserBean> {

    String session;
    public ProfileLoader(Context context) {
        super(context);
    }

    @Override
    public UserBean loadInBackground() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("pref",getContext().MODE_PRIVATE);
        session = sharedPreferences.getString("Session","");
        Log.i("session",session);
        return ProfileUtil.getProfile(session);

    }
}

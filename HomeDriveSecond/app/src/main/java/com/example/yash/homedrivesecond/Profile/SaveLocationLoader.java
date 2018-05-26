package com.example.yash.homedrivesecond.Profile;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yash on 01-03-2018.
 */

public class SaveLocationLoader extends AsyncTaskLoader<Object> {

    Double lat;
    Double logn;
    String session;

    public SaveLocationLoader(Context context , Double lat , Double logn) {
        super(context);
        this.lat = lat;
        this.logn = logn;
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",context.MODE_PRIVATE);
        session = sharedPreferences.getString("Session","");
    }

    @Override
    public Object loadInBackground() {
        SaveLocationUtil.saveLocationPost(session,lat,logn);
        return null;
    }
}

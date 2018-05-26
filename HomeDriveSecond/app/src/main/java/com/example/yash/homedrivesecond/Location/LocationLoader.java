package com.example.yash.homedrivesecond.Location;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Yash on 28-02-2018.
 */

public class LocationLoader extends AsyncTaskLoader<ArrayList<LatLng>> {

    String session;
    public LocationLoader(Context context) {
        super(context);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("pref", getContext().MODE_PRIVATE);
        session = sharedPreferences.getString("Session", "");
    }

    @Override
    public ArrayList<LatLng> loadInBackground() {
        return LocationUtil.getLocationView(session);
    }
}

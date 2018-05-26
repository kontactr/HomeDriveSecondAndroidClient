package com.example.yash.homedrivesecond.Profile;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;

/**
 * Created by Yash on 01-03-2018.
 */

public class GoogleLocation  {


    private LocationManager mlocationManager;
    private String provider;
    private Activity dashBoardActivity;

    final LoaderManager.LoaderCallbacks locationLoaderCallbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            Double lat = bundle.getDouble("lat");
            Double logn = bundle.getDouble("logn");
            return new SaveLocationLoader(dashBoardActivity,lat,logn);
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Toast.makeText(dashBoardActivity,"Your Location Is Saved. ",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };







    final android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mlocationManager.removeUpdates(this);
            Double lat =  (location.getLatitude());
            Double lng =  (location.getLongitude());
            Toast.makeText(dashBoardActivity,""+lat+" "+lng,Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putDouble("lat",lat);
            bundle.putDouble("logn",lng);
            dashBoardActivity.getLoaderManager().restartLoader(22,bundle,locationLoaderCallbacks).forceLoad();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(dashBoardActivity, "Enabled new provider " + provider,
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(dashBoardActivity, "Disabled provider " + provider,
                    Toast.LENGTH_SHORT).show();
        }
    };


    public GoogleLocation(Activity DashBoardActivity, LocationManager locationManager){

        dashBoardActivity = DashBoardActivity;
        mlocationManager = locationManager;
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(dashBoardActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(dashBoardActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("Goo","Permission - 1");
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.i("Google","Provider " + provider + " has been selected.");
            locationListener.onLocationChanged(location);
        } else {
            Toast.makeText(dashBoardActivity,"Location Unknown",Toast.LENGTH_SHORT).show();
        }
    }

    public void onLocationCallbackstart(){

        if (ActivityCompat.checkSelfPermission(dashBoardActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(dashBoardActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mlocationManager.requestLocationUpdates(provider, 5000, 1, locationListener);


    }


}

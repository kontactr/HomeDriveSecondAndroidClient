package com.example.yash.homedrivesecond;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.yash.homedrivesecond.Location.LocationLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocationViewActivity extends Activity implements OnMapReadyCallback {

    GoogleMap mMap;
    boolean mapReady = false;
    ArrayList<LatLng> arrayList ;
    Integer start = 0  , i= start , end;


    final LoaderManager.LoaderCallbacks loaderCallbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            return new LocationLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Toast.makeText(getApplicationContext(),"Move Around In Map So Tiles Will Cache Before Location",Toast.LENGTH_LONG).show();
            arrayList = (ArrayList<LatLng>) o ;
            end=arrayList.size() - 1;
            if(arrayList.size() > 0){
                findViewById(R.id.btn_location).setVisibility(View.VISIBLE);
                for(LatLng latLng : arrayList){
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.person));
                    mMap.addMarker(markerOptions);
                }

            }else{
                findViewById(R.id.btn_location).setVisibility(View.GONE);

            }

        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);

        findViewById(R.id.btn_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapReady)
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                else
                    Toast.makeText(getApplicationContext(),"Map is not ready",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_satelite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapReady)
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                else
                    Toast.makeText(getApplicationContext(),"Map is not ready",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_hybrid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapReady)
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                else
                    Toast.makeText(getApplicationContext(),"Map is not ready",Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer temp  = getIndex();
                Log.i("Loc",i.toString()+" "+arrayList.get(temp).toString());
                if(mapReady)
                    flyTo(CameraPosition.builder().target(arrayList.get(temp)).zoom(21).bearing(0).tilt(45).build());

            }


        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationViewActivity.this.getLoaderManager().restartLoader(17,null,loaderCallbacks).forceLoad();




    }

    private void flyTo(CameraPosition build) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(build),5000,null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMap = googleMap;
        LatLng newYork = new LatLng(40.7484,-73.9857);
        CameraPosition target = CameraPosition.builder().target(newYork).zoom(20).build();


        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }

    private Integer getIndex(){
        if(i > end)
            i = 0;
        return i++;

    }
}

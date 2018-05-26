package com.example.yash.homedrivesecond.DownloadFile;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

/**
 * Created by Yash on 05-02-2018.
 */

public class DownloadFileLoader extends AsyncTaskLoader {

    private String currentPath;
    HashSet<String> hashSet;
    String session ;
    String type;

    public DownloadFileLoader(Context context , String currentPath , HashSet<String> hashSet , String type) {
        super(context);
        this.currentPath = currentPath;
        this.hashSet = hashSet;
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",context.MODE_PRIVATE);
        session = sharedPreferences.getString("Session","");
        this.type = type;
    }

    @Override
    public Object loadInBackground() {
        DownloadFileUtils.postDownloadFiles(session,currentPath,hashSet,type);
        return null;
    }
}

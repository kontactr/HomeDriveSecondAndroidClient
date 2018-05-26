package com.example.yash.homedrivesecond.DeleteFile;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;

/**
 * Created by Yash on 05-02-2018.
 */

public class DeleteFileLoader extends AsyncTaskLoader {

    private String currentPath;
    HashSet<String> hashSet;
    String session ;


    public DeleteFileLoader(Context context , HashSet<String> hashSet , String currentPath) {
        super(context);
        this.hashSet = hashSet;
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",context.MODE_PRIVATE);
        session = sharedPreferences.getString("Session","");
        this.currentPath = currentPath;
    }

    @Override
    public Object loadInBackground() {



        DeleteFileUtils.postDeleteFiles(session,currentPath,hashSet);
        return null;
    }
}

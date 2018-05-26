package com.example.yash.homedrivesecond.FileUpload;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

/**
 * Created by Yash on 06-02-2018.
 */

public class FileUploadLoader extends AsyncTaskLoader {

    HashSet<String> hashSet;
    String currentPathAndroid;
    String currentPath;
    String session;

    public FileUploadLoader(Context context, HashSet<String> hashSet, String currentPathAndroid, String currentPath) {
        super(context);
        this.hashSet = hashSet;
        this.currentPathAndroid = currentPathAndroid;
        this.currentPath = currentPath;
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",context.MODE_PRIVATE);
        session = sharedPreferences.getString("Session","");

    }

    @Override
    public Object loadInBackground() {
        FileUploadUtils.uploadFiles(hashSet,currentPathAndroid , currentPath, session);
        return null;
    }
}

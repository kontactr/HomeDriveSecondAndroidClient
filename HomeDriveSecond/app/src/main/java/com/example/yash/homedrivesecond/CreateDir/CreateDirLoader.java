package com.example.yash.homedrivesecond.CreateDir;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yash on 05-02-2018.
 */

public class CreateDirLoader extends AsyncTaskLoader {
    private  String dirName;
    private  String currentPath;
    private String session;

    public CreateDirLoader(Context context, String dirName, String currentPath) {
        super(context);
        this.dirName = dirName;
        this.currentPath = currentPath;
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",context.MODE_PRIVATE);
        session = sharedPreferences.getString("Session","");
    }

    @Override
    public Object loadInBackground() {
        CreateDirUtils.postCreateDir(dirName,currentPath,session);
        return null;
    }
}

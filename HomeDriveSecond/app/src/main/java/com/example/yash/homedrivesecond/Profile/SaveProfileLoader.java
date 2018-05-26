package com.example.yash.homedrivesecond.Profile;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yash on 27-02-2018.
 */

public class SaveProfileLoader extends AsyncTaskLoader {

    UserBean userBean;
    String session;

    public SaveProfileLoader(Context context, UserBean userBean) {
        super(context);
        this.userBean = userBean;
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",context.MODE_PRIVATE);
        session = sharedPreferences.getString("Session","");
    }

    @Override
    public Object loadInBackground() {
         SaveProfileUtil.saveProfile(userBean,session);
         return null;
    }
}

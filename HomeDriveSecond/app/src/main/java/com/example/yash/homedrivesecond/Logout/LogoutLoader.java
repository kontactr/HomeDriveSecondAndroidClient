package com.example.yash.homedrivesecond.Logout;

import android.content.Context;
import android.content.AsyncTaskLoader;

/**
 * Created by Yash on 05-02-2018.
 */

public class LogoutLoader extends AsyncTaskLoader {


    String session;

    public LogoutLoader(Context context , String session) {
        super(context);
        this.session = session;
    }

    @Override
    public Object loadInBackground() {
        LogoutUtil.postLogout(session);
        return null;
    }
}

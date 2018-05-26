package com.example.yash.homedrivesecond.Register;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Yash on 28-02-2018.
 */

public class ResendLoader extends AsyncTaskLoader {

    String session;

    public ResendLoader(Context context, String session) {
        super(context);
        this.session = session;
    }

    @Override
    public Object loadInBackground() {
        ResendUtil.getResendCode(session);
        return null;
    }
}


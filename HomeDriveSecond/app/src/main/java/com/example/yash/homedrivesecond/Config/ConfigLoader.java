package com.example.yash.homedrivesecond.Config;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Yash on 05-02-2018.
 */

public class ConfigLoader extends AsyncTaskLoader {

    String config;
    String session;

    public ConfigLoader(Context context , String config , String session) {
        super(context);
        this.config = config;
        this.session = session;
    }

    @Override
    public Object loadInBackground() {
        ConfigUtil.postSubmit(config,session);
        return null;
    }
}

package com.example.yash.homedrivesecond.Register;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Yash on 28-02-2018.
 */

public class FinalRegister extends AsyncTaskLoader {

    private final String code;
    private final String session;

    public FinalRegister(Context context, String code, String session) {
        super(context);
        this.code = code;
        this.session = session;
    }

    @Override
    public Object loadInBackground() {
        return FinalRegisterUtil.postFinalRegister(code,session);
    }
}

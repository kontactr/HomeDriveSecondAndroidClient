package com.example.yash.homedrivesecond.LoginRSA;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Yash on 04-03-2018.
 */

public class SignInByRSALoader extends AsyncTaskLoader<String> {

    String usernameRSA , keyRSA;

    public SignInByRSALoader(Context context, String usernameRSA, String keyRSA) {
        super(context);
        this.usernameRSA = usernameRSA;
        this.keyRSA = keyRSA;

    }

    @Override
    public String loadInBackground() {
        return SignInByRSAUtil.postSignInByRSA(usernameRSA,keyRSA);
    }
}

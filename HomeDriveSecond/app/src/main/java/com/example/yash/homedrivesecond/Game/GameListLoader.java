package com.example.yash.homedrivesecond.Game;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Yash on 14-03-2018.
 */

public class GameListLoader extends AsyncTaskLoader<ArrayList<String>> {
    public GameListLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<String> loadInBackground() {
        return GameListUtil.postGameList();
    }
}

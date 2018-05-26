package com.example.yash.homedrivesecond.Game;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Yash on 14-03-2018.
 */

public class GameDownloaderLoader extends AsyncTaskLoader<String> {

    String gameFileName = "";

    public GameDownloaderLoader(Context context , String gameFileName) {
        super(context);
        this.gameFileName = gameFileName;
    }

    @Override
    public String loadInBackground() {
        return GameDownloaderUtil.getGame(gameFileName);
    }
}

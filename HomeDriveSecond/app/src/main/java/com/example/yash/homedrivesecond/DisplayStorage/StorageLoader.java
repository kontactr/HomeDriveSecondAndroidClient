package com.example.yash.homedrivesecond.DisplayStorage;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Yash on 04-02-2018.
 */

public class StorageLoader  extends AsyncTaskLoader{

    String url_attach;

    public StorageLoader(Context context , String url_attach) {
        super(context);
        this.url_attach = url_attach;
    }

    @Override
    public Object loadInBackground() {
        String htmlPage =  StorageUtil.getStorage(getContext(),url_attach);
        ArrayList<FileBean> arrayList = StorageUtil.getListFromHTML(htmlPage);
        return arrayList;
    }
}

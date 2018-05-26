package com.example.yash.homedrivesecond.DisplayStorage;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yash.homedrivesecond.R;
import com.example.yash.homedrivesecond.StorageActivity;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Yash on 04-02-2018.
 */

public class StorageAdapter extends ArrayAdapter<FileBean> implements CheckBox.OnClickListener{


    private final StorageActivity storageActivity;
    private final HashSet<String> hashSet;

    public StorageAdapter(@NonNull Context context, @NonNull List<FileBean> objects, StorageActivity storageActivity,
                          HashSet<String> hashSet
                          ) {
        super(context, 0, objects);
        this.storageActivity = storageActivity;
        this.hashSet = hashSet;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        RelativeLayout relativeLayout = (RelativeLayout) convertView;

        if(relativeLayout == null  ){

           relativeLayout =  (RelativeLayout)   LayoutInflater.from(getContext()).inflate(R.layout.storage_list_item,null);

        }

        final FileBean fileBean = getItem(position);

         CheckBox checkBox = (CheckBox) relativeLayout.findViewById(R.id.list_item);

         checkBox.setText(fileBean.getText());

         checkBox.setOnClickListener(this);


         if(fileBean.isFile()){
             relativeLayout.findViewById(R.id.storage_goto).setVisibility(View.INVISIBLE);
         }else{
             relativeLayout.findViewById(R.id.storage_goto).setVisibility(View.VISIBLE);
                relativeLayout.findViewById(R.id.storage_goto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url" ,fileBean.getUrl() );

                         storageActivity.getLoaderManager().restartLoader(1,bundle,storageActivity).forceLoad();
                    }
                });
         }






        return relativeLayout;

    }

    @Override
    public void onClick(View view) {
        CheckBox checkBox = (CheckBox) view;
        String checkboxText = checkBox.getText().toString().trim();
        if(checkBox.isChecked()){
            hashSet.add(checkboxText);
        }else{
            if(hashSet.contains(checkboxText))
                hashSet.remove(checkboxText);
        }

    }
}

package com.example.yash.homedrivesecond.Game;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.example.yash.homedrivesecond.R;

import java.io.File;
import java.util.List;

/**
 * Created by Yash on 14-03-2018.
 */

public class GameAdapter extends ArrayAdapter<String> {

    View.OnClickListener gameClickListener;
    public GameAdapter(@NonNull Context context, @NonNull List<String> objects, View.OnClickListener gameClickListener) {
        super(context, 0, objects);
        this.gameClickListener = gameClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RadioButton radioButton = (RadioButton) convertView;

        String item = getItem(position);

        if(radioButton == null){

            radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.rsa_files_selector,null);

        }

        radioButton.setText(item.split(".apk")[0]);
        radioButton.setOnClickListener(gameClickListener);

        return radioButton;
    }
}

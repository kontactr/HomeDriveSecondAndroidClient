package com.example.yash.homedrivesecond.LoginRSA;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yash.homedrivesecond.R;
import com.example.yash.homedrivesecond.StorageActivity;

import java.io.File;
import java.util.List;

/**
 * Created by Yash on 04-03-2018.
 */

public class RSAAdapter extends ArrayAdapter<File> {

    View.OnClickListener rsaClickListener;


    public RSAAdapter(@NonNull Context context, @NonNull List<File> objects, View.OnClickListener rsaClickListener) {
        super(context, 0, objects);
        this.rsaClickListener = rsaClickListener;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RadioButton radioButton = (RadioButton) convertView;

        File item = getItem(position);

        if(radioButton == null){

            radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.rsa_files_selector,null);


        }



        radioButton.setText(item.getName().split(".rsa")[0]);
        radioButton.setOnClickListener(rsaClickListener);

        return radioButton;
    }
}

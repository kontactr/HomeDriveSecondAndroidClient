package com.example.yash.homedrivesecond.FileUpload;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yash.homedrivesecond.MainActivity;
import com.example.yash.homedrivesecond.R;
import com.example.yash.homedrivesecond.StorageActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Yash on 05-02-2018.
 */

public class FileBuilderAdapter extends ArrayAdapter<String>  {

    Context context;

   public static String currentPath;
    StorageActivity mainActivity;
    int size;

     public FileBuilderAdapter(Context context , ArrayList<String> arrayList , String currentPath, StorageActivity mainActivity){
            super(context,0,arrayList);
         this.context = context;
         size = arrayList.size();
         Log.i("Count",""+getCount());
         this.currentPath = currentPath;
         this.mainActivity = mainActivity;

    }





    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        RelativeLayout relativeLayout = (RelativeLayout) view;

        String item = getItem(i);

        if(relativeLayout == null ){

            relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.list_item_values,null);

        }

        CheckBox checkBox = relativeLayout.findViewById(R.id.list_item_checkbox);
        TextView textView = relativeLayout.findViewById(R.id.list_item_tv);

        File file = new File(currentPath+"/"+item);

        if(file.isFile()){
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnClickListener(StorageActivity.onClickListenerCheckBox);
            textView.setVisibility(View.GONE);
            checkBox.setText(item);
            relativeLayout.setOnClickListener(null);
        }else{
            checkBox.setVisibility(View.GONE);
            checkBox.setOnClickListener(null);
            textView.setVisibility(View.VISIBLE);
            relativeLayout.setOnClickListener(mainActivity);
            textView.setText(item);

        }

        return relativeLayout;

    }

    @Override

    public int getViewTypeCount() {

        return (int)((8 * (getCount() / 8))+1) * 8 ;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


}

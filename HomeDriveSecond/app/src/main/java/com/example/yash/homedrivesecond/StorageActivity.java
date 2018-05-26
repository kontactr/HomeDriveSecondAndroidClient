package com.example.yash.homedrivesecond;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yash.homedrivesecond.CreateDir.CreateDirLoader;
import com.example.yash.homedrivesecond.DeleteFile.DeleteFileLoader;
import com.example.yash.homedrivesecond.DisplayStorage.FileBean;
import com.example.yash.homedrivesecond.DisplayStorage.StorageAdapter;
import com.example.yash.homedrivesecond.DisplayStorage.StorageLoader;
import com.example.yash.homedrivesecond.DownloadFile.DownloadFileLoader;
import com.example.yash.homedrivesecond.FileUpload.FileBuilderAdapter;
import com.example.yash.homedrivesecond.FileUpload.FileUploadLoader;
import com.example.yash.homedrivesecond.Logout.LogoutLoader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class StorageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks , Button.OnClickListener {

    ListView listView;
    StorageAdapter storageAdapter;
    String currentPath ;
    String currentPathAndroid;
    HashSet<String> hashSet;

    FileBuilderAdapter fileBuilderAdapter;
    static HashSet<String> hashSet_android = new HashSet<>();


    public static final CheckBox.OnClickListener onClickListenerCheckBox = new CheckBox.OnClickListener() {
        @Override
        public void onClick(View view) {
            CheckBox cb = (CheckBox) view;
            String checkbox_text = cb.getText().toString();
            if(cb.isChecked())
                hashSet_android.add(checkbox_text);
            else
            if (hashSet_android.contains(checkbox_text))
                hashSet_android.remove(checkbox_text);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        hashSet = new HashSet<String >();
        listView = (ListView) findViewById(R.id.storage_lv);
        storageAdapter = new StorageAdapter(getApplicationContext(),new ArrayList<FileBean>(),StorageActivity.this,
                hashSet
                );


        SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String config = sharedPreferences.getString("Config","C:\\");

        if(config.equalsIgnoreCase(""))
            config = "C:\\";

        currentPath = "/viewdata/"+config;

        Bundle bundle = new Bundle();
        bundle.putString("url" ,"/viewdata/"+config );

        getLoaderManager().restartLoader(1,bundle,StorageActivity.this).forceLoad();



    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        currentPath = bundle.getString("url");
        return new StorageLoader(getApplicationContext() , bundle.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {

       ArrayList<FileBean> arrayList = (ArrayList<FileBean>) o;

       storageAdapter.clear();
       storageAdapter.addAll(arrayList);

       listView.setAdapter(storageAdapter);

        Toast.makeText(getApplicationContext(),"Finished",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){



            case R.id.sto_menu_cre_dir:{


                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(StorageActivity.this);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                final View dialogView = inflater.inflate(R.layout.dialog_layout, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.select_dialog_edittext);

                dialogBuilder.setTitle("Custom dialog");
                dialogBuilder.setMessage("New Directory Name");

                final LoaderManager.LoaderCallbacks loaderCallbacks = new LoaderManager.LoaderCallbacks() {
                    @Override
                    public Loader onCreateLoader(int i, Bundle bundle) {
                        String text = bundle.getString("DirName");
                        return new CreateDirLoader(getApplicationContext(),text,currentPath);
                    }

                    @Override
                    public void onLoadFinished(Loader loader, Object o) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url" ,currentPath );

                        getLoaderManager().restartLoader(1,bundle,StorageActivity.this).forceLoad();

                    }

                    @Override
                    public void onLoaderReset(Loader loader) {

                    }
                };


                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Activity activity = (Activity) StorageActivity.this;

                        Bundle bundle = new Bundle();
                        bundle.putString("DirName",edt.getText().toString());

                        activity.getLoaderManager().restartLoader(
                                4,bundle,loaderCallbacks

                        ).forceLoad();

                    }
                });


                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                android.support.v7.app.AlertDialog b = dialogBuilder.create();
                b.show();

                break;

            }

            ///////////////////////////////////////////////////////// Another Menu///////////////////



            case R.id.sto_menu_dele_file:{

                LoaderManager.LoaderCallbacks loaderCallbacks = new LoaderManager.LoaderCallbacks() {
                    @Override
                    public Loader onCreateLoader(int i, Bundle bundle) {
                        return new DeleteFileLoader(getApplicationContext(),hashSet,currentPath);
                    }

                    @Override
                    public void onLoadFinished(Loader loader, Object o) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url" ,currentPath );
                        getLoaderManager().restartLoader(1,bundle,StorageActivity.this).forceLoad();
                        hashSet.clear();

                    }

                    @Override
                    public void onLoaderReset(Loader loader) {

                    }
                };
                Activity activity = (Activity) StorageActivity.this;
                activity.getLoaderManager().restartLoader(
                        5,null,loaderCallbacks

                ).forceLoad();
                    break;
            }

            case R.id.sto_menu_dow_file:{

                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(StorageActivity.this);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                final View dialogView = inflater.inflate(R.layout.dialog_layout_checkbox, null);
                dialogBuilder.setView(dialogView);

                final RadioButton abstractButton = (RadioButton) dialogView.findViewById(R.id.rb_abstract);
                final RadioButton deepButton = (RadioButton) dialogView.findViewById(R.id.rb_deep);

                dialogBuilder.setTitle("Custom dialog");
                dialogBuilder.setMessage("Compression Type");

                String type = "Abstract Compress";

                if(abstractButton.isChecked())
                    type = "Abstract Compress";
                if(deepButton.isChecked())
                    type = "Deep Compress";

                final String finalType = type;

                final LoaderManager.LoaderCallbacks loaderCallbacks = new LoaderManager.LoaderCallbacks() {
                    @Override
                    public Loader onCreateLoader(int i, Bundle bundle) {
                        return new DownloadFileLoader(getApplicationContext(),currentPath,hashSet,bundle.getString("Type"));
                    }

                    @Override
                    public void onLoadFinished(Loader loader, Object o) {
                        Toast.makeText(getApplicationContext(),"Downnload Completed",Toast.LENGTH_SHORT).show();
                        hashSet.clear();

                    }

                    @Override
                    public void onLoaderReset(Loader loader) {

                    }
                };

                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        Activity activity = (Activity) StorageActivity.this;

                        Bundle bundle = new Bundle();
                        bundle.putString("Type", finalType);

                        activity.getLoaderManager().restartLoader(
                                6,bundle,loaderCallbacks

                        ).forceLoad();

                    }
                });


                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                android.support.v7.app.AlertDialog b = dialogBuilder.create();
                b.show();

                break;


            }

            case R.id.sto_menu_up_file:
            {

                final LoaderManager.LoaderCallbacks loaderCallbacksUpload = new LoaderManager.LoaderCallbacks() {
                    @Override
                    public Loader onCreateLoader(int i, Bundle bundle) {
                        CustomSerial customSerial = (CustomSerial) bundle.getSerializable("HashSet");
                        //Log.i("HashSet",customSerial.getHashSet().toString());
                        return new FileUploadLoader(getApplicationContext() ,customSerial.getHashSet() , FileBuilderAdapter.currentPath , currentPath);
                    }

                    @Override
                    public void onLoadFinished(Loader loader, Object o) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url" ,currentPath );
                        getLoaderManager().restartLoader(1,bundle,StorageActivity.this).forceLoad();
                        hashSet_android.clear();

                    }

                    @Override
                    public void onLoaderReset(Loader loader) {

                    }
                };


                currentPathAndroid = Environment.getExternalStorageDirectory().toString();

                Log.i("cp",currentPathAndroid);

                final ArrayList<String> arrayList = new ArrayList<String>();


                //String path = Environment.getExternalStorageDirectory().toString();
                File directory = new File(currentPathAndroid);
                arrayList.clear();
                File[] files = directory.listFiles();

                for(File file : files)
                    arrayList.add(file.getName().trim());


                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(StorageActivity.this);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                ListView listView = (ListView) inflater.inflate(R.layout.list_view, null);


                fileBuilderAdapter = new FileBuilderAdapter(getApplicationContext(),arrayList,currentPathAndroid,StorageActivity.this);

                listView.setAdapter(fileBuilderAdapter);

                dialogBuilder.setView(listView);



                Log.i("Dir",""+ Environment.getRootDirectory());


                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashSet<String> hashSetAndroid1 = new HashSet<String>();
                        HashSet<String> hashSetAndroid2 = new HashSet<String >();
                        Boolean flag = true;

                        for(String fileNames : hashSet_android){
                            if(flag){
                                hashSetAndroid1.add(fileNames);
                                flag = false;
                            }else{
                                hashSetAndroid2.add(fileNames);
                                flag = true;
                            }
                        }


                        Toast.makeText(getApplicationContext(),""+hashSet_android.toString(),Toast.LENGTH_SHORT).show();

                        if(hashSetAndroid1.size() > 0){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("HashSet",new CustomSerial(hashSetAndroid1));
                            getLoaderManager().restartLoader(7,bundle,loaderCallbacksUpload).forceLoad();
                        }
                        if(hashSetAndroid2.size() > 0){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("HashSet",new CustomSerial(hashSetAndroid2));
                            getLoaderManager().restartLoader(8,bundle,loaderCallbacksUpload).forceLoad();

                        }





                    }});


                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                android.support.v7.app.AlertDialog b = dialogBuilder.create();
                b.show();
                hashSet_android.clear();

                break;
            }








        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.storage_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {

        RelativeLayout relativeLayout = (RelativeLayout) view;

        hashSet_android.clear();

        String text = ((TextView)relativeLayout.findViewById(R.id.list_item_tv)).getText().toString();

        FileBuilderAdapter.currentPath = FileBuilderAdapter.currentPath  +"/" +text;

        Log.i("path",FileBuilderAdapter.currentPath);

        File[] files =  new File( new File(FileBuilderAdapter.currentPath).getAbsolutePath() ).listFiles();

        Log.i("path",""+files.length);

        ArrayList<String> arrayList = new ArrayList<>();
        for(File file : files)
            arrayList.add(file.getName());

        fileBuilderAdapter.clear();
        fileBuilderAdapter.addAll(arrayList);



    }

        private class CustomSerial implements Serializable{

            HashSet<String> hashSet ;

            CustomSerial(HashSet<String> hashSet){
                this.hashSet = hashSet;
            }

            public HashSet<String> getHashSet() {
                return hashSet;
            }

            public void setHashSet(HashSet<String> hashSet) {
                this.hashSet = hashSet;
            }
        }



}

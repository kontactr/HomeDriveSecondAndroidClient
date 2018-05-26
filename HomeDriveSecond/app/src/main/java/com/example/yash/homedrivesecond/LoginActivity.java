package com.example.yash.homedrivesecond;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yash.homedrivesecond.Login.LoginLoader;

import java.security.Permission;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){



        switch(permsRequestCode){

            case 200:
                if(grantResults.length > 0) {
                    boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                }
                break;

        }

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1){

            if(! hasPermission("android.permission.WRITE_EXTERNAL_STORAGE")){
                String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.ACCESS_COARSE_LOCATION"
                };

                requestPermissions(perms, 124);
            }

        }



        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });

        final EditText username = (EditText) findViewById(R.id.login_et_username);
        final EditText password = (EditText) findViewById(R.id.login_et_pass);
        Button submit = (Button) findViewById(R.id.login_button_login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Username",username.getText().toString());
                bundle.putString("Password",password.getText().toString());

                getLoaderManager().restartLoader(0,bundle,LoginActivity.this).forceLoad();
            }
        });


        findViewById(R.id.adv_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdvanceFunc.class));
            }
        });




    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new LoginLoader(getApplicationContext(),
                bundle.getString("Username"),
                bundle.getString("Password")
                );
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {

        String str = (String) o;

        if (str.equalsIgnoreCase("Wrong")){

            Toast.makeText(getApplicationContext(),"Password or Username are Invalid",Toast.LENGTH_SHORT).show();

        }else {
            //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
            //Log.i("key-final",new String(String.valueOf(o)));
            SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Session", String.valueOf(o));
            editor.putInt("Session-FB",0);
            editor.apply();



            Intent i = new Intent(getApplicationContext(),DashBoardActivity.class);

            startActivity(i);


        }
        //SharedPreferences sharedPreferences1 = getSharedPreferences("pref",Context.MODE_PRIVATE);
        //String session = sharedPreferences1.getString("Session","");
        //Log.i("Session",session);
       // putString("Session",new String(String.valueOf(o))).apply();

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private boolean shouldAskPermission(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String permission){

        return(checkSelfPermission(permission)==PackageManager.PERMISSION_GRANTED);


    }


}





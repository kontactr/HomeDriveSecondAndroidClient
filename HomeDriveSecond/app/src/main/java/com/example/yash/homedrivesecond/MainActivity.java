package com.example.yash.homedrivesecond;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences sharedPreferences = getSharedPreferences("pref",Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("Session","");
        //Toast.makeText(getApplicationContext(),""+session,Toast.LENGTH_SHORT).show();
        if (session == ""){

            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);

        }else{

            Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(),"Session Available",Toast.LENGTH_SHORT).show();
        }




        setContentView(R.layout.activity_main);
    }
}

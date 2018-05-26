package com.example.yash.homedrivesecond;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.accountkit.AccountKit;

public class FaceBookTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_book_test);
    }

    public void logout(View view){
        AccountKit.logOut();
        finish();
    }


}

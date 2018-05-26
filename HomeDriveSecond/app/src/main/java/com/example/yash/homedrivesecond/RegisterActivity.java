package com.example.yash.homedrivesecond;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.yash.homedrivesecond.Register.FinalRegister;
import com.example.yash.homedrivesecond.Register.RegisterLoader;
import com.example.yash.homedrivesecond.Register.ResendLoader;

import java.util.Map;

import static android.text.InputType.TYPE_CLASS_TEXT;

public class RegisterActivity extends AppCompatActivity {

    EditText username , email , password;

    final LoaderManager.LoaderCallbacks firstStepLoaderCallback = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            String email = bundle.getString("email");
            return new RegisterLoader(getApplicationContext(),username,password,email);
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Map<String,String> Resultmap = (Map<String,String>) o ;
            if(new Boolean(Resultmap.get("flag"))){


                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(RegisterActivity.this);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                final View dialogView = inflater.inflate(R.layout.dialog_layout_regi_vali, null);
                dialogBuilder.setView(dialogView);

                final EditText vCode = (EditText) dialogView.findViewById(R.id.regi_et_vali);
                final Button resendCode = (Button) dialogView.findViewById(R.id.regi_resend_btn_vali);

                final String session = Resultmap.get("Set-Cookie");

                resendCode.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Set-Cookie",session);
                        RegisterActivity.this.getLoaderManager().restartLoader(21,bundle,resendLoaderCallbacks).forceLoad();
                    }
                });

                dialogBuilder.setTitle("Second Step");
                dialogBuilder.setMessage("Enter your validation code");


                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String code = vCode.getText().toString();
                        if(code.length() != 0)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("code",code);
                            bundle.putString("session",session);
                            RegisterActivity.this.getLoaderManager().restartLoader(21,bundle,finalRegisterLoaderCallbacks).forceLoad();
                        }
                    }
                });



                android.support.v7.app.AlertDialog b = dialogBuilder.create();
                b.show();

            }

            else
                Toast.makeText(getApplicationContext(),"Try later or use different username",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };

    final LoaderManager.LoaderCallbacks finalRegisterLoaderCallbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            String code = bundle.getString("code");
            String session = bundle.getString("session");
            return new FinalRegister(getApplicationContext(),code,session);
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Boolean flag = (Boolean) o;
            if(flag)
            {
                Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                RegisterActivity.this.finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"Not Registered",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };




    final LoaderManager.LoaderCallbacks resendLoaderCallbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            String session = bundle.getString("Set-Cookie");
            return new ResendLoader(getApplicationContext(),session);
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Toast.makeText(getApplicationContext(),"We mailed your new code",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username =  (EditText) findViewById(R.id.register_et_username);
        password = (EditText) findViewById(R.id.register_et_password);
        email = (EditText) findViewById(R.id.register_et_email);

        findViewById(R.id.register_btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("username",username.getText().toString());
                bundle.putString("password",username.getText().toString());
                bundle.putString("email",email.getText().toString());

                RegisterActivity.this.getLoaderManager().restartLoader(20,bundle,firstStepLoaderCallback).forceLoad();
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.register_btn_view);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Type", String.valueOf(password.getInputType()));
                if(password.getInputType() == 129) {
                    Log.i("Type - if", String.valueOf(password.getInputType()));
                    password.setInputType(TYPE_CLASS_TEXT);
                }
                else {
                    password.setInputType(129);
                    Log.i("Type - else", String.valueOf(password.getInputType()));
                }

            }
        });




    }
}

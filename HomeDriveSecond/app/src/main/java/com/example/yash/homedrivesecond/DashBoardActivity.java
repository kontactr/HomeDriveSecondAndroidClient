package com.example.yash.homedrivesecond;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.yash.homedrivesecond.Config.ConfigLoader;
import com.example.yash.homedrivesecond.LoginRSA.LoginRSALoader;
import com.example.yash.homedrivesecond.Logout.LogoutLoader;
import com.example.yash.homedrivesecond.Profile.GoogleLocation;
import com.example.yash.homedrivesecond.Profile.ProfileLoader;
import com.example.yash.homedrivesecond.Profile.SaveProfileLoader;
import com.example.yash.homedrivesecond.Profile.UserBean;
import com.facebook.accountkit.AccountKit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class DashBoardActivity extends AppCompatActivity {

    final LoaderManager.LoaderCallbacks saveProfileLoaderCallbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            UserBean userBean = (UserBean) bundle.getSerializable("UserBean");
            return new SaveProfileLoader(getApplicationContext() , userBean);
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };

    final LoaderManager.LoaderCallbacks rsaProfileLoaderCallbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            String password = bundle.getString("RSA");
            return new LoginRSALoader(getApplicationContext(),password);
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            String rsaKey = (String)o;
            if(rsaKey.equalsIgnoreCase("Failed To Create"))
                return;
            String[] arr = rsaKey.split(":");
            //Toast.makeText(getApplicationContext(),rsaKey,Toast.LENGTH_SHORT).show();
            FileOutputStream outputStream = null;
            try {
                File file = new File(getApplicationContext().getFilesDir(),arr[0].trim()+".rsa");
                if(!file.exists())
                    file.createNewFile();
                outputStream = openFileOutput(arr[0].trim()+".rsa", Context.MODE_PRIVATE);
                outputStream.write(arr[1].trim().getBytes());
                outputStream.close();
                Toast.makeText(getApplicationContext(),"Sucessfully Generated",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };


    final LoaderManager.LoaderCallbacks profileLoaderCallbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            return new ProfileLoader(getApplicationContext());
        }


        @Override
        public void onLoadFinished(Loader loader, Object o) {
            final UserBean userBean = (UserBean) o ;
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashBoardActivity.this);
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final View dialogView = inflater.inflate(R.layout.dialog_layout_profile, null);
            dialogBuilder.setView(dialogView);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref",
                    getApplicationContext().MODE_PRIVATE
            );

            Integer FB_SESSION = sharedPreferences.getInt("Session-FB",0);

            dialogBuilder.setTitle("Profile Change");

            final EditText name =  dialogView.findViewById(R.id.profile_name);
            final EditText email = dialogView.findViewById(R.id.profile_email);
            final EditText password = dialogView.findViewById(R.id.profile_password);
            ImageButton imageButton = dialogView.findViewById(R.id.profile_btn_view);
            ImageButton rsaButton = dialogView.findViewById(R.id.profile_btn_gen_rsa);
            if(FB_SESSION > 0) {
                dialogView.findViewById(R.id.profile_tv_name).setVisibility(View.GONE);
                dialogView.findViewById(R.id.profile_tv_password).setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                imageButton.setVisibility(View.GONE);
                rsaButton.setVisibility(View.GONE);

            }else{
                name.setHint(userBean.getName());
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
            email.setHint(userBean.getEmail());


            rsaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder dialogBuilderRSA = new AlertDialog.Builder(DashBoardActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    final View dialogViewRSA = inflater.inflate(R.layout.dialog_layout_gen_rsa, null);
                    dialogBuilderRSA.setView(dialogViewRSA);
                    dialogBuilderRSA.setTitle("Enter Your Password");

                    dialogBuilderRSA.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(getApplicationContext(),"Getting",Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                EditText editTextRSA = (EditText) dialogViewRSA.findViewById(R.id.select_dialog_rsa_passtext);
                                bundle.putString("RSA",editTextRSA.getText().toString());
                                DashBoardActivity.this.getLoaderManager().restartLoader(27,bundle,rsaProfileLoaderCallbacks).forceLoad();
                        }
                    });

                    dialogBuilderRSA.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog RSA = dialogBuilderRSA.create();
                    RSA.show();

                }
            });



            ImageButton imageButton1 = dialogView.findViewById(R.id.profile_btn_loc);
            imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),LocationViewActivity.class);
                    startActivity(i);
                }
            });


             dialogView.findViewById(R.id.profile_btn_save_loc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    GoogleLocation googleLocation =   new GoogleLocation(DashBoardActivity.this , locationManager);
                    googleLocation.onLocationCallbackstart();
                }
            });


            dialogBuilder.setPositiveButton("Process", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userBean.setEmail(email.getText().toString());
                    userBean.setName(name.getText().toString());
                    userBean.setPassword(password.getText().toString());
                    Bundle b = new Bundle();
                    b.putSerializable("UserBean",userBean);

                    DashBoardActivity.this.getLoaderManager().restartLoader(5,b,saveProfileLoaderCallbacks).forceLoad();

                }
            });


            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });




            AlertDialog b = dialogBuilder.create();
            b.show();

        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };



    @Override
    public void onBackPressed() {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);




        findViewById(R.id.dash_tv_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StorageActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.dash_tv_config).setOnClickListener(new View.OnClickListener() {

            private String m_text = "";

            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DashBoardActivity.this);
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                final View dialogView = inflater.inflate(R.layout.dialog_layout, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.select_dialog_edittext);

                dialogBuilder.setTitle("Custom dialog");
                dialogBuilder.setMessage("Enter text below");

                final LoaderManager.LoaderCallbacks loaderCallbacks = new LoaderManager.LoaderCallbacks() {
                    @Override
                    public Loader onCreateLoader(int i, Bundle bundle) {
                        String config = bundle.getString("Config");
                        String session = bundle.getString("Session");
                        return new ConfigLoader(getApplicationContext(),config,session);
                    }

                    @Override
                    public void onLoadFinished(Loader loader, Object o) {
                        Toast.makeText(getApplicationContext(),"Finished",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoaderReset(Loader loader) {

                    }
                };



                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener()

                {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String text = edt.getText().toString();

                      SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("pref",
                              getApplicationContext().MODE_PRIVATE
                              );
                        SharedPreferences.Editor  editor = sharedPreferences.edit();

                        editor.putString("Config",text);
                        editor.apply();

                        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("pref",
                                getApplicationContext().MODE_PRIVATE
                                );

                        String session = sharedPreferences1.getString("Session","");


                        Bundle bundle = new Bundle();
                        bundle.putString("Config",text);
                        bundle.putString("Session",session);

                       Activity activity = (Activity) DashBoardActivity.this;



                       activity.getLoaderManager().restartLoader(
                                2,bundle,loaderCallbacks

                        ).forceLoad();


                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });


        findViewById(R.id.dash_tv_logout).setOnClickListener(new View.OnClickListener() {



             LoaderManager.LoaderCallbacks loaderCallbacks = new LoaderManager.LoaderCallbacks() {
                @Override
                public Loader onCreateLoader(int i, Bundle bundle) {
                    String session = bundle.getString("Session");
                    return new LogoutLoader(getApplicationContext(),session);
                }

                @Override
                public void onLoadFinished(Loader loader, Object o) {

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref",
                            getApplicationContext().MODE_PRIVATE
                            );

                    Integer FB_SESSION = sharedPreferences.getInt("Session-FB",0);


                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.remove("Session");

                    if(FB_SESSION > 0) {
                        editor.remove("Session-FB");
                        Toast.makeText(getApplicationContext(),"FB CLose",Toast.LENGTH_SHORT).show();
                        AccountKit.logOut();

                    }

                    editor.apply();



                    Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();

                    DashBoardActivity.this.finish();

                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));


                    //Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    //startActivity(intent);

                }

                @Override
                public void onLoaderReset(Loader loader) {

                }
            };

            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pref",
                        getApplicationContext().MODE_PRIVATE
                        );

                String session = sharedPreferences.getString("Session" , "");

                Bundle bundle = new Bundle();
                bundle.putString("Session" , session);

                DashBoardActivity.this.getLoaderManager().restartLoader(2,bundle,loaderCallbacks ).forceLoad();


            }
        });


        findViewById(R.id.profile_tv_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DashBoardActivity.this.getLoaderManager().restartLoader(2,null,profileLoaderCallbacks).forceLoad();

            }
        });


        findViewById(R.id.dash_tv_other_storage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });



    }



}

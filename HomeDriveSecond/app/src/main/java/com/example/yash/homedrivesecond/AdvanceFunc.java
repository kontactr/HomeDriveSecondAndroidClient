package com.example.yash.homedrivesecond;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.yash.homedrivesecond.FBPassLess.FBPassLessLoader;
import com.example.yash.homedrivesecond.Game.GameAdapter;
import com.example.yash.homedrivesecond.Game.GameDownloaderLoader;
import com.example.yash.homedrivesecond.Game.GameListLoader;
import com.example.yash.homedrivesecond.LoginRSA.RSAAdapter;
import com.example.yash.homedrivesecond.LoginRSA.SignInByRSALoader;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;


import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;


public class AdvanceFunc extends Activity {

    String userName , passWord , email;
     public static String rsaFile = "";
     public static String gameFile = "";
    ListView rsaListView = null;
    ListView gameListView = null;
      RadioButton radioButtonRSA = null;
      RadioButton radioButtonGame = null;


      final LoaderManager.LoaderCallbacks gameListLoaderCallback = new LoaderManager.LoaderCallbacks() {
          @Override
          public Loader onCreateLoader(int i, Bundle bundle) {
              return new GameListLoader(getApplicationContext());
          }

          @Override
          public void onLoadFinished(Loader loader, Object o) {
              ArrayList<String> gameListArray = (ArrayList<String>) o;
              final AlertDialog.Builder dialogBuilderGame = new AlertDialog.Builder(AdvanceFunc.this);
              final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
              final View dialogViewGame = inflater.inflate(R.layout.list_view_radio, null);
              dialogBuilderGame.setView(dialogViewGame);
              dialogBuilderGame.setTitle("Select Your Game");
              gameListView = dialogViewGame.findViewById(R.id.list_view);
              GameAdapter gameAdapter = new GameAdapter(getApplicationContext(),gameListArray,gameClickListener);
              gameListView.setAdapter(gameAdapter);


              dialogBuilderGame.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {

                      Toast.makeText(getApplicationContext(),gameFile,Toast.LENGTH_SHORT).show();
                      Bundle bundle = new Bundle();
                      bundle.putString("Game",gameFile);
                      AdvanceFunc.this.getLoaderManager().restartLoader(35,bundle,gameDownloaderLoaderCallbacks).forceLoad();
                      gameListView = null;
                      radioButtonGame = null;
                  }
              });


              dialogBuilderGame.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      dialogInterface.cancel();
                  }
              });

              AlertDialog Game = dialogBuilderGame.create();
              Game.show();


          }

          @Override
          public void onLoaderReset(Loader loader) {

          }
      };



     final LoaderManager.LoaderCallbacks signByRSALoaderCallback = new LoaderManager.LoaderCallbacks() {
         @Override
         public Loader onCreateLoader(int i, Bundle bundle) {

             String usernameRSA = bundle.getString("username");
             String keyRSA = bundle.getString("key");
             return new SignInByRSALoader(getApplicationContext(),usernameRSA,keyRSA);
         }

         @Override
         public void onLoadFinished(Loader loader, Object o) {
             String messageRSA = (String)o;
             if(messageRSA.equalsIgnoreCase("Wrong")){
                 Toast.makeText(getApplicationContext(),"Error To LogIn",Toast.LENGTH_SHORT).show();
             }else{

                 SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.putString("Session", messageRSA);
                 editor.putInt("Session-FB", 0);
                 editor.apply();
                 Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
                 startActivity(i);
                 finish();
             }


         }

         @Override
         public void onLoaderReset(Loader loader) {

         }
     };





     RadioButton.OnClickListener rsaClickListener = new RadioButton.OnClickListener() {
         @Override
         public void onClick(View view) {



             if(radioButtonRSA == null)
                 radioButtonRSA = (RadioButton) view;
             else
             {
                 radioButtonRSA.setChecked(false);
                 radioButtonRSA =  (RadioButton)view;
                 Log.i("Button",radioButtonRSA.getText().toString());
             }


             rsaFile = ((RadioButton)view).getText().toString();
         }
     };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_func);
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if(accessToken != null){
            Log.i("Info","Info-0");
            AccountKit.logOut();

        }

        findViewById(R.id.login_rsa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File dir = getFilesDir();
                File[] subFiles = dir.listFiles();
                ArrayList<File> rsaFiles = new ArrayList<>();
                for (File file : subFiles)
                    if(file.getName().endsWith(".rsa"))
                        rsaFiles.add(file);
                subFiles = null;
                if(rsaFiles.size() > 0){
                    final AlertDialog.Builder dialogBuilderRSA = new AlertDialog.Builder(AdvanceFunc.this);
                    final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    final View dialogViewRSA = inflater.inflate(R.layout.list_view_radio, null);
                    dialogBuilderRSA.setView(dialogViewRSA);
                    dialogBuilderRSA.setTitle("Select Your RSAs Key");
                    rsaListView = dialogViewRSA.findViewById(R.id.list_view);

                    RSAAdapter rsaAdapter = new RSAAdapter(getApplicationContext(),rsaFiles,rsaClickListener);
                    rsaListView.setAdapter(rsaAdapter);

                    dialogBuilderRSA.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                FileInputStream fileInputStream = openFileInput(rsaFile+".rsa");
                                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                StringBuilder fileData = new StringBuilder();
                                String oneLine = "";
                                while ((oneLine = bufferedReader.readLine()) != null)
                                    fileData.append(oneLine);
                                bufferedReader.close();

                                Bundle bundle = new Bundle();
                                bundle.putString("username",rsaFile);
                                bundle.putString("key",fileData.toString());
                                AdvanceFunc.this.getLoaderManager().restartLoader(30,bundle,signByRSALoaderCallback).forceLoad();

                                //fileInputStream.close();
                                //inputStreamReader.close();
                                rsaListView = null;
                                radioButtonRSA = null;
                            } catch (java.io.IOException e) {
                                e.printStackTrace();
                            }


                            //Toast.makeText(getApplicationContext(),rsaFile,Toast.LENGTH_SHORT).show();

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



                }else{
                    Toast.makeText(getApplicationContext(),"You Have No PreStored RSA Keys",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.play_game).setOnClickListener(new View.OnClickListener() {

            private Class<?>  apkUtils;
            String path = getFilesDir().toString();
            @Override

            public void onClick(View view) {








                    //Log.i("dir",""+new File("/data/app/"+getPackageName().toString()+"-"+BuildConfig.VERSION_CODE+"/").exists() );

                try {



                    apkUtils = new DexClassLoader( "/data/app/com.example.yash.homedrivesecond-2"+":sampleloader.apk",
                            getApplicationContext().getCacheDir().toString(),
                            null,
                            getClassLoader().getParent()

                    ).loadClass("com.example.yash.sampleloader.Hello");
                    Log.i("apk",apkUtils.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }



            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    final LoaderManager.LoaderCallbacks fbPassLessLogin = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            String username = bundle.getString("username");
            String password = bundle.getString("password");
            String email = bundle.getString("email");
            return new FBPassLessLoader(getApplicationContext(),username , password , email);
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {

            if(o != null) {

                String session = (String) o;
                SharedPreferences sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Session", session);
                editor.putInt("Session-FB", 1);
                editor.apply();
                Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
                startActivity(i);
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"Error Login",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };


    private static final Integer APP_REQUEST_CODE = 1 ;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APP_REQUEST_CODE){
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(loginResult.getError() != null){
                String message = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
            }else if(loginResult.getAccessToken() != null){
                launchAccountActivity();
            }
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {


                    email =  account.getEmail();
                    passWord =   account.getPhoneNumber().toString();
                    userName =  account.getId();

                    if(email == null)
                        email = "";
                    Bundle bundle = new Bundle();



                    bundle.putString("username",userName);
                    bundle.putString("password",passWord);
                    bundle.putString("email",email);

                    AdvanceFunc.this.getLoaderManager().restartLoader(26,bundle,fbPassLessLogin).forceLoad();



                }

                @Override
                public void onError(AccountKitError accountKitError) {

                }
            });


        }
    }




    private void onLogin(final LoginType loginType){

        final Intent intent = new Intent(this, AccountKitActivity.class);

        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder= new
                AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,AccountKitActivity.ResponseType.TOKEN);

        final AccountKitConfiguration accountKitConfiguration = configurationBuilder.build();


        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, accountKitConfiguration);

        startActivityForResult(intent,APP_REQUEST_CODE);



    }


    public void onPhoneLogIn(View view){
        onLogin(LoginType.PHONE);
    }

    private void launchAccountActivity(){
        //Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
        //startActivity(intent);
        //finish();
    }

    public void gameList(View v){
        AdvanceFunc.this.getLoaderManager().restartLoader(31,null,gameListLoaderCallback).forceLoad();
    }

    RadioButton.OnClickListener gameClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {



            if(radioButtonGame == null)
                radioButtonGame = (RadioButton) view;
            else
            {
                radioButtonGame.setChecked(false);
                radioButtonGame =  (RadioButton)view;
                Log.i("Button",radioButtonGame.getText().toString());
            }


            gameFile = ((RadioButton)view).getText().toString();
        }
    };

    final LoaderManager.LoaderCallbacks gameDownloaderLoaderCallbacks = new LoaderManager.LoaderCallbacks() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            String gameFile = bundle.getString("Game");
            return new GameDownloaderLoader(getApplicationContext(),gameFile);
        }

        @Override
        public void onLoadFinished(Loader loader, Object o) {
            String gameFileData = (String)o;




            FileOutputStream outputStream = null;
            try {
               // Log.i("Dir",Environment.getDataDirectory().toString());
               // File file = new File(Environment.getDataDirectory(), gameFile.trim()+".apk");
               // if(!file.exists())
                //    file.createNewFile();
                outputStream = new FileOutputStream( getFilesDir()+"/"+gameFile.trim()+".apk" );
                outputStream.write(gameFileData.getBytes());
                outputStream.close();
                Toast.makeText(getApplicationContext(),"Sucessfully Downloaded",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(String s: getFilesDir().list())
                Log.i("Dir",s);
            File file = new File(gameFile.trim()+".apk");
            Toast.makeText(getApplicationContext(),""+file.exists(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoaderReset(Loader loader) {

        }
    };

    public void playGame(View v) {



    }

}



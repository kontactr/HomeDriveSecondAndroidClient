package com.example.yash.homedrivesecond.Register;

import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.yash.homedrivesecond.Login.LoginBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.DUMMY_VERIFIER;
import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.localTrustmanager;

/**
 * Created by Yash on 28-02-2018.
 */

public class ResendUtil {

    public static void getResendCode(String session){

        HttpsURLConnection httpURLConnection = null;

        try {
            SSLContext sslc = SSLContext.getInstance("TLS");
            sslc.init(null, new TrustManager[] { localTrustmanager },
                    new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslc
                    .getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }



        try {
            URL url = new URL("https://192.168.225.28:5050/register/validate?code=&send=Resend+Code");



            //List<LoginBean> params = new LinkedList<LoginBean>();
           // params.add(new LoginBean("code",""));
           // params.add(new LoginBean("send","Resend Code"));


            httpURLConnection= (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            Log.i("key-1",httpURLConnection.getRequestMethod());
            httpURLConnection.setHostnameVerifier(DUMMY_VERIFIER);


            Log.i("Info","Info1");


            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);

            httpURLConnection.setRequestProperty("Cookie",session);


            httpURLConnection.connect();



            InputStream inputStream;
            StringBuilder sb = new StringBuilder();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp = bufferedReader.readLine();
            while (temp != null) {
                sb.append(temp);
                temp = bufferedReader.readLine();
            }

            Log.i("Button",sb.toString());

            Map<String,List<String>> map = httpURLConnection.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : map.entrySet()){
                String key = entry.getKey();
                List<String> value = entry.getValue();
                Log.i("Key-1",key+"  "+value);

            }


        } catch (MalformedURLException e) {
            Log.i("ex","1");
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.i("ex","2");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("ex","3");
            e.printStackTrace();
        }


    }

    }





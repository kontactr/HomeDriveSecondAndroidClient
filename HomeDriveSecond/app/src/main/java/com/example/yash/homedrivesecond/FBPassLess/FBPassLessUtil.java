package com.example.yash.homedrivesecond.FBPassLess;

import android.util.Log;

import com.example.yash.homedrivesecond.Login.LoginBean;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.DUMMY_VERIFIER;
import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.localTrustmanager;

/**
 * Created by Yash on 02-03-2018.
 */

public class FBPassLessUtil {

    public static String getPasswordLessLogin(String Username , String Password , String Email){

        String session = null;
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
            URL url = new URL("https://192.168.225.28:5050/fbpasswordless");
            httpURLConnection= (HttpsURLConnection) url.openConnection();
            httpURLConnection.setHostnameVerifier(DUMMY_VERIFIER);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Log.i("Info","Info1");


            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);


            Log.i("Info","Info2");

            List<LoginBean> params = new ArrayList<LoginBean>();
            params.add(new LoginBean("platform","Android"));
            params.add(new LoginBean("username", Username));
            params.add(new LoginBean("password", Password));
            params.add(new LoginBean("email",Email));


            Log.i("Info","Info3");

            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
            Log.i("FB-LESS","FB_LESS");

            httpURLConnection.connect();


            Log.i("Error","Error1");
            Map<String,List<String>> map = httpURLConnection.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : map.entrySet()){
                String key = entry.getKey();
                List<String> value = entry.getValue();
                Log.i("Key-FB",key+"  "+value);

            }

            if (httpURLConnection.getResponseCode() == 302 || httpURLConnection.getResponseCode() == 200){

                if (map.get("Location").get(0).equalsIgnoreCase("https://192.168.225.28:5050/login")){
                    session = "Wrong";
                }else {
                    session = httpURLConnection.getHeaderField("Set-Cookie");
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }finally {

            httpURLConnection.disconnect();
            return session;

        }



    }


    private static String getQuery(List<LoginBean> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (LoginBean pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }



}

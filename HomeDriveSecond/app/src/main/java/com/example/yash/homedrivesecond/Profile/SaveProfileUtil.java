package com.example.yash.homedrivesecond.Profile;

import android.util.Log;

import com.example.yash.homedrivesecond.Login.LoginBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * Created by Yash on 27-02-2018.
 */

public class SaveProfileUtil {

    public static void saveProfile(UserBean userBean , String session) {


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
            URL url = new URL("https://192.168.225.28:5050/profile");
            httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setHostnameVerifier(DUMMY_VERIFIER);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Log.i("Info", "Info1");


            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);

            httpURLConnection.setRequestProperty("Cookie",session);


            List<LoginBean> params = new ArrayList<LoginBean>();
            params.add(new LoginBean("name", userBean.getName()));
            params.add(new LoginBean("email", userBean.getEmail()));
            params.add(new LoginBean("password", userBean.getPassword()));
            params.add(new LoginBean("cpassword", userBean.getPassword()));


            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();


            httpURLConnection.connect();

            InputStream inputStream;
            StringBuilder sb = new StringBuilder();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp = bufferedReader.readLine();
            while (temp != null){
                sb.append(temp);
                temp = bufferedReader.readLine();
            }

            Log.i("ProfileMsg",sb.toString());


            Map<String, List<String>> map = httpURLConnection.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                Log.i("Key", key + "  " + value);

            }

            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

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

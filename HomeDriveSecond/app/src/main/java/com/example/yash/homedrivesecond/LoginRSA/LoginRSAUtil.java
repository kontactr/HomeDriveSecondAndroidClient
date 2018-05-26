package com.example.yash.homedrivesecond.LoginRSA;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.DUMMY_VERIFIER;

/**
 * Created by Yash on 04-03-2018.
 */

public class LoginRSAUtil {

    public static String getRSA(String session , String password){

        HttpsURLConnection httpURLConnection = null;

      /*  try {
            SSLContext sslc = SSLContext.getInstance("TLS");
            sslc.init(null, new TrustManager[] { localTrustmanager },
                    new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslc
                    .getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }*/

        try {
            URL url = new URL("https://192.168.225.28:5050/genRSAkey");

            httpURLConnection= (HttpsURLConnection) url.openConnection();
            httpURLConnection.setHostnameVerifier(DUMMY_VERIFIER);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Log.i("Info","Info1");


            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);


            httpURLConnection.setRequestProperty("Cookie",session);


            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write("platform=Android&");
            writer.write("password="+password);
            writer.flush();
            writer.close();
            os.close();


            httpURLConnection.connect();



            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp = bufferedReader.readLine();
            while (temp != null){
                sb.append(temp);
                temp = bufferedReader.readLine();
            }


            Map<String,List<String>> map = httpURLConnection.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : map.entrySet()){
                String key = entry.getKey();
                List<String> value = entry.getValue();
                Log.i("Key-1",key+"  "+value);

            }

            return sb.toString();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        return null;
    }

}

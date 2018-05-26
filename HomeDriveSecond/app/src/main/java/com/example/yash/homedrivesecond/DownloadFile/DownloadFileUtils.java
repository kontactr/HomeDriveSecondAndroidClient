package com.example.yash.homedrivesecond.DownloadFile;

import android.os.Environment;
import android.util.Log;

import com.example.yash.homedrivesecond.DisplayStorage.FileBean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.DUMMY_VERIFIER;
import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.localTrustmanager;

/**
 * Created by Yash on 05-02-2018.
 */

public class DownloadFileUtils {

    public static void postDownloadFiles(String session , String currentPath , HashSet<String> fileset , String type){

        HttpsURLConnection httpURLConnection = null;

/*        try {
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
            URL url = new URL("https://192.168.225.28:5050/download/"+currentPath.substring(10));

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
            writer.write(getQuery(fileset));
            writer.write("&radio="+type);
            writer.flush();
            writer.close();
            os.close();


            httpURLConnection.connect();

            Map<String,List<String>> map = httpURLConnection.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : map.entrySet()){
                String key = entry.getKey();
                List<String> value = entry.getValue();
                Log.i("Headers",key+"  "+value);

            }

            Log.i("Headers-Content",httpURLConnection.getContent().toString());

            String headerContent =  map.get("Content-Disposition").get(0).trim();
            String fileName =  headerContent.substring(headerContent.indexOf("=")+1,headerContent.length());
            Log.i("fileName",fileName);

            File file_dir = null;
            FileWriter fileWriter = null;


            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                file_dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
                Log.i("Dir-CF",""+    file_dir.createNewFile());

                Log.i("Dir",""+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                Log.i("Dir",""+Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
                Log.i("Dir",""+Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY));

                fileWriter = new FileWriter(file_dir);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


                String temp = bufferedReader.readLine();
                while (temp != null) {
                    bufferedWriter.write(temp);
                    temp = bufferedReader.readLine();
                }
                bufferedWriter.close();
            }catch (IOException e){
                e.printStackTrace();
                Log.i("Exception",e.toString());

            }

            bufferedReader.close();


            File file_new = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
            Log.i("Dir-exists",""+file_new.exists());


            //Log.i("Reply",sb.toString());






        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private static String getQuery(HashSet<String> hashSet) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String str : hashSet)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode("sh_fi", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(str, "UTF-8"));
        }

        return result.toString();
    }



}

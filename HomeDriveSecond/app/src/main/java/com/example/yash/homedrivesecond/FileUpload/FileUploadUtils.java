package com.example.yash.homedrivesecond.FileUpload;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
 * Created by Yash on 06-02-2018.
 */

public class FileUploadUtils {

    public static void uploadFiles(HashSet<String> hashSet, String currentPathAndroid, String currentPath, String session){

        Log.i("Path - Current",currentPath);
        Log.i("Path - Android",currentPathAndroid);
        Log.i("Path - HashSet",hashSet.toString());

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";



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
        for (String temp : hashSet) {

            try {


                HttpsURLConnection httpURLConnection = null;

                String attachmentName = temp;
                String attachmentFileName = temp;


                URL url = new URL("https://192.168.225.28:5050/uploaddata/" + currentPath.substring(10));

                httpURLConnection = (HttpsURLConnection) url.openConnection();
                httpURLConnection.setHostnameVerifier(DUMMY_VERIFIER);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                Log.i("Info", "Info1");


                httpURLConnection.setInstanceFollowRedirects(false);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);


                httpURLConnection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                httpURLConnection.setRequestProperty("Cookie", session);

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));

                bufferedWriter.write(twoHyphens + boundary + crlf);
                bufferedWriter.write("Content-Disposition: form-data; name=\"" + "file" +
                        "\";filename=\"" +
                        attachmentFileName + "\"" + crlf);
                bufferedWriter.write(crlf);


                File file = new File(currentPathAndroid + "/" + temp);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader1 = new BufferedReader(fileReader);

                String fileContent = bufferedReader1.readLine();

                while (fileContent != null) {

                    Log.i("Request-Data-New", fileContent);
                    bufferedWriter.write(fileContent);
                    fileContent = bufferedReader1.readLine();
                }

                bufferedReader1.close();

                bufferedWriter.write(crlf);
                bufferedWriter.write(twoHyphens + boundary +
                        twoHyphens + crlf);


                bufferedWriter.flush();
                bufferedWriter.close();

                httpURLConnection.connect();


                /**********************Read Data **********************/

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp_new = bufferedReader.readLine();
                while (temp_new != null) {
                    Log.i("Result-Data-New", temp_new);
                    sb.append(temp_new);
                    temp_new = bufferedReader.readLine();
                }

                Log.i("Result", sb.toString());


                Map<String, List<String>> map = httpURLConnection.getHeaderFields();

                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    String key = entry.getKey();
                    List<String> value = entry.getValue();
                    Log.i("Key-1", key + "  " + value);

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }




}

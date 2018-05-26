package com.example.yash.homedrivesecond.Profile;

import android.util.Log;

import com.example.yash.homedrivesecond.DisplayStorage.FileBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class ProfileUtil {

    public static UserBean getProfile(String session){

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



            httpURLConnection= (HttpsURLConnection) url.openConnection();
            httpURLConnection.setHostnameVerifier(DUMMY_VERIFIER);
            httpURLConnection.setRequestMethod("GET");
            Log.i("key-1",httpURLConnection.getRequestMethod());



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



            return parseUser( sb);

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

            return null;
    }

    private static UserBean parseUser(StringBuilder sb) {
        Log.i("Temp",sb.toString());

        Document document = Jsoup.parse(sb.toString());

        Elements elements = document.select("table");

        elements = elements.select("tr");


        elements = elements.select("td");

        Log.i("element",""+elements.size());

        UserBean userBean = new UserBean();

        userBean.setName(elements.get(1).select("input").attr("placeholder"));

        userBean.setEmail(elements.get(3).select("input").attr("placeholder"));

        return userBean;
    }
}

package com.example.yash.homedrivesecond.Location;

import android.content.SharedPreferences;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.DUMMY_VERIFIER;
import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.localTrustmanager;

/**
 * Created by Yash on 28-02-2018.
 */

public class LocationUtil {

    public static ArrayList<LatLng> getLocationView(String session){

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

        HttpsURLConnection httpURLConnection = null;

        try {
            URL url = new URL("https://192.168.225.28:5050/get_google_map");

            httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setHostnameVerifier(DUMMY_VERIFIER);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Log.i("Info", "Info1");


            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);




            httpURLConnection.setRequestProperty("Cookie", session);

            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write("platform=Android");
            writer.flush();
            writer.close();
            os.close();



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




            httpURLConnection.disconnect();

            //SpannableString contentText = new SpannableString(sb.toString());
            //String htmlEncodedString = Html.toHtml(contentText);

            return getLatLang(sb.toString());


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


       return null;

    }

    private static ArrayList<LatLng> getLatLang(String s) {
        ArrayList<LatLng> arrayList = new ArrayList<>();

        Document document = Jsoup.parse(s);

        Elements elements = document.select("table");

        elements = elements.select("tr");

        elements = elements.select("td");

        for (int i=0;i<elements.size();i+=2){

            arrayList.add(new LatLng( new Double(elements.get(i).text()) , new Double( elements.get(i+1).text() )));
        }


        return arrayList;

    }

}

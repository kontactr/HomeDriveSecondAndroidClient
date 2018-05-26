package com.example.yash.homedrivesecond.DisplayStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.DUMMY_VERIFIER;
import static com.example.yash.homedrivesecond.SSLCertificate.ByPassCertificate.localTrustmanager;

/**
 * Created by Yash on 04-02-2018.
 */

public class StorageUtil {

    static Context context;


    public static String getStorage(Context c , String url_attach){

           context = c;

        HttpsURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        StringBuilder sb = new StringBuilder();

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
            URL url = new URL("https://192.168.225.28:5050"+url_attach);
            httpURLConnection= (HttpsURLConnection) url.openConnection();
            httpURLConnection.setHostnameVerifier(DUMMY_VERIFIER);
            httpURLConnection.setRequestMethod("GET");
            //httpURLConnection.setDoInput(true);
            //httpURLConnection.setDoOutput(true);

            Log.i("Info","Info1");


            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);

            SharedPreferences sharedPreferences1 = c.getSharedPreferences("pref",Context.MODE_PRIVATE);
            String session = sharedPreferences1.getString("Session","");

            httpURLConnection.setRequestProperty("Cookie",session);


            //List<LoginBean> params = new ArrayList<LoginBean>();
            //params.add(new LoginBean("username", Username));
            //params.add(new LoginBean("password", Password));


            //OutputStream os = httpURLConnection.getOutputStream();
            //BufferedWriter writer = new BufferedWriter(
           //         new OutputStreamWriter(os, "UTF-8"));
           // writer.write(getQuery(params));
           // writer.flush();
           // writer.close();
           // os.close();


            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp = bufferedReader.readLine();
            while (temp != null){
                sb.append(temp);
                temp = bufferedReader.readLine();
            }



            //Log.i("Answer",sb.toString());
            //getListFromHTML(sb.toString());
            Map<String,List<String>> map = httpURLConnection.getHeaderFields();

            for (Map.Entry<String, List<String>> entry : map.entrySet()){
                String key = entry.getKey();
                List<String> value = entry.getValue();
                Log.i("Key-1",key+"  "+value);

            }

            /*if (httpURLConnection.getResponseCode() == 302 || httpURLConnection.getResponseCode() == 200){

                if (map.get("Location").get(0).equalsIgnoreCase("http://192.168.0.101:5000/login")){
                    session = "Wrong";
                }else {
                    session = httpURLConnection.getHeaderField("Set-Cookie");
                }

            }*/



        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }finally {

            httpURLConnection.disconnect();
            return sb.toString();

        }


    }

    public static ArrayList<FileBean> getListFromHTML(String html){

        ArrayList arrayList = new ArrayList();

        Document document = Jsoup.parse(html);

        Elements elements = document.select("table");

        elements = elements.select("tr");


        elements = elements.select("td");

       // Log.i("element",""+elements.size());

        for (int i=0;i<elements.size();i+=3){

            String file = elements.get(i).text();
            String type = elements.get(i+1).text();


            if(type.equalsIgnoreCase("Directory")){

               arrayList.add(new FileBean(elements.get(i).select("a").attr("href"),
                        file,false,""
                       ));

                Log.i("link: ",""+   elements.get(i).select("a").attr("href"));
            }else{

                arrayList.add(new FileBean("",file,true,type));

            }


        }

        return arrayList;



    }





}

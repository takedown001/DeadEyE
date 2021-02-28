package com.Gcc.Deadeye;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class JSONParserString {

    private static InputStream is = null;
    private static String  jObj = null;
    private static String json = "";

    private Integer status = 0;



    // function get json from urlref
    // by making HTTP POST or GET mehtod
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String makeHttpRequest(String url,
                                  Map<String, String> params) throws KeyStoreException, IOException {

        //for builing a parameter
        StringBuilder result = new StringBuilder();
        boolean first = true;

        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0) {
                    result.append("&");
                }
                result.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        System.out.println("string" + result.toString());


        try {


            // check for request method
            // request method is POST
            // defaultHttpClient
            URL urlr = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) urlr.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            /* for Get request */
            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // You need to set it to true if you want to send (output) a request body,
            //for example with POST or PUT requests.
            //Sending the request body itself is done via the connection's output stream
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(result.toString());
            writer.flush();
            writer.close();
            os.close();

            int statusCode = conn.getResponseCode();
            /* 200 represents HTTP OK */
            if (statusCode == 200) {

                status = 1; // Successful

            } else {
                status = 0; //"Failed to fetch data!";
            }

            conn.connect();
            is = conn.getInputStream();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new String(json);
        } catch (Exception e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }
}
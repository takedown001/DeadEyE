package mobisocial.arcade;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * A complete Java class that demonstrates how to read content (text) from a URL
 * using the Java URL and URLConnection classes.
 * @author alvin alexander, alvinalexander.com
 */
public class JavaUrlConnectionReader
{
  public JavaUrlConnectionReader() {

  }

    public  String getUrlContents(String theUrl, HashMap<String, String> postDataParams) {

            StringBuilder content = new StringBuilder();

            // many of these calls can throw exceptions, so i've just
            // wrapped them all in one try/catch statement.
            try {
                // create a url object
                URL url = new URL(theUrl);

                // create a urlconnection object
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = urlConnection.getResponseCode();//end of Posting params

                // Reading Page Content
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // wrap the urlconnection in a bufferedreader
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    String line;

                    // read from the urlconnection via the bufferedreader
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line + "\n");
                    }
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        private String getPostDataString (HashMap < String, String > params) throws
        UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

}
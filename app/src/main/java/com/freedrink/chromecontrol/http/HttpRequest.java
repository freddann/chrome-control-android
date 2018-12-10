package com.freedrink.chromecontrol.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest extends AsyncTask<String, Integer, String> {
    String server_response;
    private final String host;
    private final ResponseCallback callback;

    public HttpRequest(String host, ResponseCallback callback){
        this.host = host;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Log.d("HttpRequest",  strings[0] + " to " + strings[1]);
            URL url = new URL(host + strings[1]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(strings[0]);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                server_response = readStream(urlConnection.getInputStream());
            } else {
                Log.e("HttpResponse", String.valueOf(responseCode) + " Unable to connect");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.call(server_response);
    }

    public void execute(String method, String path){
        super.execute(method, path);
    }

    // Convert InputStream to String
    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}

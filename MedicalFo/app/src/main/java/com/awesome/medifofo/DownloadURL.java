package com.awesome.medifofo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eunsik on 2017-06-19.
 */

class DownloadURL {

    public String readUrl(String param_url) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(param_url);

            connection = (HttpURLConnection) url.openConnection();

            inputStream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            data = builder.toString();
            Log.d("downloadURL", data);
            bufferedReader.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            inputStream.close();
            connection.disconnect();
        }
        return data;
    }
}

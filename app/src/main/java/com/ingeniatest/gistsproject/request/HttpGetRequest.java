package com.ingeniatest.gistsproject.request;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ingeniatest.gistsproject.model.Files;
import com.ingeniatest.gistsproject.model.Gist;
import com.ingeniatest.gistsproject.utilities.FilesDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.google.gson.FieldNamingPolicy.UPPER_CAMEL_CASE;

public class HttpGetRequest extends AsyncTask<String, Void, ArrayList<Gist>> {

    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected ArrayList<Gist> doInBackground(String... params){

        String stringUrl = params[0];
        ArrayList<Gist> results;
        String inputLine;

        try {
            URL myUrl = new URL(stringUrl);

            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();

            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();


            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();

            GsonBuilder builder = new GsonBuilder();

            builder.registerTypeAdapter(Files.class, new FilesDeserializer());
            Type collectionType
                    = new TypeToken<ArrayList<Gist>>(){}.getType();

            Gson gson = builder.setFieldNamingPolicy(UPPER_CAMEL_CASE).create();
            results = gson.fromJson(stringBuilder.toString(), collectionType);
        }
        catch(IOException e){
            e.printStackTrace();
            results = null;
        }

        return results;
    }
    protected void onPostExecute(ArrayList<Gist> results){
        super.onPostExecute(results);
    }
}
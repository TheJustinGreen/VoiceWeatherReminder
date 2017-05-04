package com.example.justin.myweatherapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by justin on 2017/05/01.
 */

public class DownloadTask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;


        try {
            url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while(data != -1)
            {
                char current = (char) data;
                result += current;

                data = reader.read();

            }
            return result;

        } catch (Exception e) {
            Log.e("URL ERROR", e.toString());
            e.printStackTrace();
        }


        //7a098453824ba957655b4d3391c4b3c9


        //http://api.openweathermap.org/data/2.5/weather?lat=-33.9303267&lon=18.6350331&appid=7a098453824ba957655b4d3391c4b3c9
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);

        try {
            JSONObject jsonObject = new JSONObject(result);

            String weatherInfo = jsonObject.getString("weather");

            Log.i("weatherInfo", weatherInfo.toString());

            //Log.i("weatherDescription", weatherInfo.getString("description"));
            JSONObject weatherDates = new JSONObject(jsonObject.getString("main"));
            Log.i("weatherDates", weatherDates.toString());

            double tempInt = Double.parseDouble(weatherDates.getString("temp"));

            //Change for calculus
            int tempIn = (int) (tempInt  - 271.15);

            JSONArray jsonArray = new JSONArray(weatherInfo);

            JSONObject jsonPart = jsonArray.getJSONObject(0);

            Log.i("Description", jsonPart.getString("description"));
            MainActivity.descrpition = jsonPart.getString("description");

            MainActivity.currTemperature = String.valueOf(tempIn) + "°C";



            MainActivity.currLocation =  jsonObject.getString("name");



/*
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonPart = jsonArray.getJSONObject(i);

        //        jsonPart.getString("description");
                JSONObject weatherData = new JSONObject(jsonObject.getString("main"));

//                double temperature = Double.parseDouble(weatherData.getString("temp"));

  //              int tempFar = (int) (temperature * 1.8 - 459.67);

                String weatherDescription = jsonObject.getString("description");


            }
*/

        }
        catch (Exception e)
        {
            Log.e("JSON ERROR",e.toString());
        }
    }



}

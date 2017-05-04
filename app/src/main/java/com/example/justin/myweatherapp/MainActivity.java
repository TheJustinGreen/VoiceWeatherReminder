package com.example.justin.myweatherapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static TextView temperatureText,nameText;
    static String descrpition,currTemperature,currLocation;
    private TrackGPS gps;
    double longitude;
    double latitude;

    public static TextToSpeech toSpeech;
    public int result;
    public ArrayList<String> text;
    String name = "";
    public static  MainActivity activity;


    protected static final int RESULT_SPEECH = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        temperatureText = (TextView) findViewById(R.id.placeTemprature);
        nameText = (TextView) findViewById(R.id.placeLocation);


        gps = new TrackGPS(MainActivity.this);


        if(gps.canGetLocation()){


            longitude = gps.getLongitude();
            latitude = gps .getLatitude();

            Log.i("LATLONG", latitude + "," + longitude);
            DownloadTask task = new DownloadTask();

            task.execute("http://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(latitude) +"&lon=" + String.valueOf(longitude) + "&appid=7a098453824ba957655b4d3391c4b3c9");


//            Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude), Toast.LENGTH_SHORT).show();
        }
        else
        {

            gps.showSettingsAlert();
        }



        toSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if(i == TextToSpeech.SUCCESS)
                {
                    result = toSpeech.setLanguage(Locale.ENGLISH);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Feature not supported in your deavice", Toast.LENGTH_LONG).show();
                }


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get users input for time and data for weather updates/

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY,01);
                calendar.set(Calendar.MINUTE,29);
                calendar.set(Calendar.SECOND,30);

                Intent intent = new Intent(getApplicationContext(), Notifiction_reciever.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);


                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //Notiffy even if phones asleep//
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                //toSpeech.speak("Notification",TextToSpeech.QUEUE_FLUSH,null);




            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        else if (id == R.id.btnSpeak) {

            try {
                startActivityForResult(intent, RESULT_SPEECH);
            } catch (ActivityNotFoundException a) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Opps! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT);
                t.show();
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    //  THIS TAKES THE SPEECH AND PUTS IT IN A ARRAY LIST AND THEN DISPLAYS IT IN A TOAST FOR NOW//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                        Toast.makeText(getApplicationContext(), "Feature not supported in your deavice", Toast.LENGTH_LONG).show();
                    else {

                        if (text.get(0).equals("weather please")) {


//                            Voice brazil = toSpeech.getVoices().;
// es-ES-SMTf00
//                            toSpeech.setVoice(toSpeech.getVoice("pt-BR-SMTf00"));
                            toSpeech.setSpeechRate((float)0.8);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Log.i("voices", toSpeech.getVoices().toString());
                            }
                            toSpeech.speak("Good morning the Temperature in" + currLocation + "is" + currTemperature + "with " + descrpition, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else
                            toSpeech.speak("I only tell the weather",TextToSpeech.QUEUE_FLUSH,null);

                    }
                }
                else
                {
                   toSpeech.speak("Sorry I could not hear you",TextToSpeech.QUEUE_FLUSH,null);
                }
                break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.stopUsingGPS();
    }



}


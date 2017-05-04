package com.example.justin.myweatherapp;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static com.example.justin.myweatherapp.MainActivity.toSpeech;

/**
 * Created by justin on 2017/05/03.
 */

public class Repeating_activity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeating_activity_layout);

//        toSpeech.speak("Notification", TextToSpeech.QUEUE_FLUSH,null);
    }

}


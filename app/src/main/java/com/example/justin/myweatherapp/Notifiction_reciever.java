package com.example.justin.myweatherapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;

import static com.example.justin.myweatherapp.MainActivity.toSpeech;

/**
 * Created by justin on 2017/05/03.
 */

public class Notifiction_reciever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        /*

        Intent repeating_intent = new Intent(context,Repeating_activity.class);

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100, repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_mic_white_24dp)
                .setContentTitle("Weather")
                .setContentText("The Current weather")
                .setAutoCancel(true);

        notificationManager.notify(100,builder.build());
*/
    }


}

package com.colony.helper;

import com.colony.activity.MainActivity;
import com.google.firebase.messaging.RemoteMessage;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    String title, message, date, number;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("contentTitle"));
        title = remoteMessage.getData().get("contentTitle");
        message = remoteMessage.getData().get("message");
        date = remoteMessage.getData().get("date").toString();
        number = remoteMessage.getData().get("tickerText");


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String log_user = preferences.getString(Contract.Shared_User_Number, "");

        if (!log_user.equals("")) {
            retrieveMessage();
        }


    }

    private void sendNotification(String messageBody, String contentTitle) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.btn_default)
                .setContentTitle(contentTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void retrieveMessage() {
        Intent intent = new Intent();
        intent.setAction(Contract.ACTION_Message_CHANGED);
        intent.putExtra(Contract.EXTRA_Chat_Message, message);
        intent.putExtra(Contract.EXTRA_Chat_Name, title);
        intent.putExtra(Contract.EXTRA_Chat_Date, date);
        intent.putExtra(Contract.EXTRA_Chat_Number, number);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
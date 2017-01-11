package com.colony.helper;

import com.colony.activity.ChatActivity;
import com.colony.activity.MainActivity;
import com.colony.model.Message;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.util.ArrayList;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    String title, message, date, number,isGroup ,senderNumber,userNumberApp;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userNumberApp = preferences.getString(Contract.Shared_User_Number, "");
        senderNumber = remoteMessage.getData().get("data");
       /*if(senderNumber.equals(userNumberApp))
        {
            return;
        }*/

        title = remoteMessage.getData().get("contentTitle");
        message = remoteMessage.getData().get("message");
        date = remoteMessage.getData().get("date").toString();
        number = remoteMessage.getData().get("tickerText");
        isGroup = remoteMessage.getData().get("condition");




        sendNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("contentTitle"));





        if (!userNumberApp.equals("")) {
            retrieveMessage();
        }



    }

    private void sendNotification(String messageBody, String contentTitle) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Contract.EXTRA_Chat_Message, message);
        intent.putExtra(Contract.EXTRA_Chat_Name, title);
        intent.putExtra(Contract.EXTRA_Chat_Date, date);
        intent.putExtra(Contract.EXTRA_Chat_Number, number);
        intent.putExtra(Contract.EXTRA_Chat_IsGroup ,isGroup);
        intent.putExtra(Contract.EXTRA_Chat_SenderNumber, senderNumber);

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
        intent.putExtra(Contract.EXTRA_Chat_IsGroup ,isGroup);
        intent.putExtra(Contract.EXTRA_Chat_SenderNumber, senderNumber);
        createMessage();
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void createMessage()
    {
       FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference  databaseReference = database.getReferenceFromUrl("https://colonly-1325.firebaseio.com/Users/" +
                userNumberApp + "/Messages");
        String stringUserNumber;
        if (Boolean.valueOf(isGroup))
        {
            stringUserNumber =  senderNumber;
            title = senderNumber;
        }
        else
        {
            stringUserNumber = number;
        }

        databaseReference.child(number).push().setValue(new Message(stringUserNumber, message, date, title));

    }

}
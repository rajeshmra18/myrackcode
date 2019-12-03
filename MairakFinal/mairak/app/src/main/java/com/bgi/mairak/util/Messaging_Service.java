package com.bgi.mairak.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import com.bgi.mairak.mairak.NotificationList;
import com.bgi.mairak.mairak.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class Messaging_Service extends FirebaseMessagingService {


    DatabaseHelper Dhb;
    private SharedPreferences prefs;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        prefs = getSharedPreferences("miarak", MODE_PRIVATE);

        if (remoteMessage.getNotification().getBody()!= null){
            System.out.println("IMMHEreeeeeee" + remoteMessage.getNotification().getBody() + " :::::::: " + remoteMessage.getNotification().getTitle());
        }




        if (prefs.contains("loggedIn") && prefs.getString("loggedIn", "").equals("y")) {
            showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        }else if (prefs.contains("register") && prefs.getString("register", "").equals("y")) {
            showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        }






    }


    public void showNotification(String message, String title) {




        String tt ="";

        if (title.equals("")){

            tt="Mairak";

        }else {
            tt=title;

        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {


        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent
                (this, NotificationList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);




        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = null;

            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.mairak_logo)
                    .setContentTitle(tt)
                    .setSound(defaultSoundUri)
                    .setContentText(message)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();





        Dhb = new DatabaseHelper(Messaging_Service.this);
        Dhb.addToNotificationList(tt , message);




        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

        }


    }


}

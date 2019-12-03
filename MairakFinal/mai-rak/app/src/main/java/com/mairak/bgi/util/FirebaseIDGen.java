package com.mairak.bgi.util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by USER on 6/14/2018.
 */

public class FirebaseIDGen extends FirebaseInstanceIdService {
   // private static final String TAG = FirebaseIDGen.class.getSimpleName();

    @Override
    public void onTokenRefresh() {



        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        storeRegIdInPref(refreshedToken);
       // sendRegistrationToServer(refreshedToken);

        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void storeRegIdInPref(String token) {



        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("fcm_token", token);
        editor.apply();




    }




    public class Config {

        // global topic to receive app wide push notifications
        public static final String TOPIC_GLOBAL = "global";

        // broadcast receiver intent filters
        public static final String REGISTRATION_COMPLETE = "registrationComplete";
        public static final String PUSH_NOTIFICATION = "pushNotification";

        // id to handle the notification in the notification tray
        public static final int NOTIFICATION_ID = 100;
        public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

        public static final String SHARED_PREF = "ah_firebase";
    }
}




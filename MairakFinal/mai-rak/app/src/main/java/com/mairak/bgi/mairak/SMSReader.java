package com.mairak.bgi.mairak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

public class SMSReader extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsMsg = null;
        String smsStr ="";
        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsMsg = new SmsMessage[pdus.length];
            for (int i=0; i<smsMsg.length; i++){
                smsMsg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                smsStr = smsMsg[i].getMessageBody().toString();

                String Sender = smsMsg[i].getOriginatingAddress();
                //Check here sender is yours

                if (Sender.equals("Maira")){
                    Intent smsIntent = new Intent("otp");
                    smsIntent.putExtra("message",smsStr);
                    smsIntent.putExtra("Sender",Sender);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                }


            }
        }
    }





    }
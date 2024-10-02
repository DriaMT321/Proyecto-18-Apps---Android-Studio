package com.example.proyectobroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            if (pdus != null) {
                StringBuilder messageBody = new StringBuilder();
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    messageBody.append(smsMessage.getMessageBody());
                }

                Intent broadcastIntent = new Intent("SMS_RECEIVED_ACTION");
                broadcastIntent.putExtra("message", messageBody.toString());
                context.sendBroadcast(broadcastIntent);
            }
        }
    }
}

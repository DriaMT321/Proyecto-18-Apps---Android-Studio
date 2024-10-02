package com.example.broadcastreceiverapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;

public class BatteryLowReceiver extends BroadcastReceiver {
    private TextView batteryPercentageText;
    private static final String CHANNEL_ID = "BatteryAlertChannel";
    private static final int NOTIFICATION_ID = 1;

    public BatteryLowReceiver(TextView batteryPercentageText) {
        this.batteryPercentageText = batteryPercentageText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra("level", 0);
        batteryPercentageText.setText("Porcentaje de batería: " + level + "%");

        if (level <= 15) {
            showLowBatteryNotification(context, level);
        }
    }

    private void showLowBatteryNotification(Context context, int batteryLevel) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Battery Alerts", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Alerta de Batería Baja")
                .setContentText("El nivel de batería es " + batteryLevel + "%. Por favor, conecte el cargador.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
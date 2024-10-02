package com.example.downloadmanager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.intent.action.DOWNLOAD_COMPLETE")) {
            // Crear la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "download_channel")
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Descarga completada")
                    .setContentText("Tu archivo ha sido descargado exitosamente.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            // Intent que se activa cuando el usuario selecciona la notificación
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            builder.setContentIntent(contentIntent);

            // Mostrar la notificación
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());
        }
    }
}

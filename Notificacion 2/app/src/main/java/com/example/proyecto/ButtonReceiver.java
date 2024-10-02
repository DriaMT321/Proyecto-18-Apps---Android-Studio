package com.example.proyecto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class ButtonReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "button_channel";
    private static final String TAG = "ButtonReceiver";  // Definir un tag para el Logcat

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast recibido");  // Registrar cuando se reciba el Broadcast

        // Obtener el NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Crear un NotificationChannel en dispositivos Android 8.0 o superior
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Button Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for Button Press notifications");
            notificationManager.createNotificationChannel(channel);
        }

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Asegúrate de que este recurso exista
                .setContentTitle("Notificación")
                .setContentText("Botón presionado")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Mostrar la notificación
        notificationManager.notify(0, builder.build());
    }
}

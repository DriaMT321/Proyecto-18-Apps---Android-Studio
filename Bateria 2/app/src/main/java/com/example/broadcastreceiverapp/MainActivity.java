package com.example.broadcastreceiverapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_POST_NOTIFICATIONS = 1;
    private TextView batteryPercentageText;
    private BatteryLowReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryPercentageText = findViewById(R.id.txtBateria);

        // Verificar y solicitar permiso para notificaciones si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicitar permiso para enviar notificaciones
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_POST_NOTIFICATIONS);
            }
        }

        // Inicializar y registrar el BroadcastReceiver en código Java
        batteryReceiver = new BatteryLowReceiver(batteryPercentageText);
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistrar el BroadcastReceiver cuando la actividad se destruya
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }

    // Manejar el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
            } else {
                // Permiso denegado, manejar el caso según sea necesario
            }
        }
    }

    // BroadcastReceiver para monitorear el nivel de batería
    public static class BatteryLowReceiver extends BroadcastReceiver {
        private static final String CHANNEL_ID = "battery_alert_channel";
        private TextView batteryPercentageText;

        public BatteryLowReceiver(TextView batteryPercentageText) {
            this.batteryPercentageText = batteryPercentageText;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            batteryPercentageText.setText("Porcentaje de batería: " + level + "%");

            // Si el nivel de la batería es menor o igual a 15, mostrar una notificación
            if (level > 0 && level <= 15) {
                showNotification(context);
            }
        }

        // Método para mostrar la notificación
        private void showNotification(Context context) {
            // Crear el canal de notificación si es necesario (Android O o superior)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Alerta de Batería";
                String description = "Notifica cuando la batería está por debajo del 15%";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // Crear la intención para abrir la MainActivity al tocar la notificación
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            // Crear la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_low_battery)
                    .setContentTitle("Alerta de Batería Baja")
                    .setContentText("El nivel de batería es menor al 15%")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // Mostrar la notificación
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(1, builder.build());
            }
        }
    }
}

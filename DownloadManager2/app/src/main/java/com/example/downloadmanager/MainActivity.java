package com.example.downloadmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private DownloadReceiver downloadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crear el canal de notificación (para Android 8.0 y superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "download_channel",
                    "Descargas Simuladas",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Canal para notificaciones de descargas simuladas");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Inicializar y registrar dinámicamente el BroadcastReceiver con su filtro de intent
        downloadReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE");
        registerReceiver(downloadReceiver, intentFilter);
    }

    // Método simulado para "descargar" la imagen Tilin.jpg
    public void startDownload(View view) {
        // Simular el guardado de la imagen Tilin.jpg en Pictures
        saveImageToPictures();

        // Simular el envío del intent de "descarga completa"
        Intent intent = new Intent("android.intent.action.DOWNLOAD_COMPLETE");
        sendBroadcast(intent);
    }

    // Guardar la imagen Tilin.jpg en la carpeta Pictures
    private void saveImageToPictures() {
        InputStream inputStream = getResources().openRawResource(R.raw.tilin); // Acceder a Tilin.jpg en res/raw

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Usar MediaStore para Android 10 y superior
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, "Tilin.jpg");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    copyStream(inputStream, outputStream);
                    Toast.makeText(this, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Android 9 y versiones anteriores
                File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File imageFile = new File(picturesDirectory, "Tilin.jpg");

                OutputStream outputStream = new FileOutputStream(imageFile);
                copyStream(inputStream, outputStream);
                Toast.makeText(this, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show();

                // Registrar en la galería
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(imageFile);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error guardando la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    // Copiar el contenido del InputStream al OutputStream
    private void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();
        inputStream.close();
    }

    // Definir el BroadcastReceiver directamente en la clase
    public class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Aquí manejamos la lógica cuando la descarga está completa
            if (intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
                showDownloadCompletedNotification();
            }
        }
    }

    // Método que muestra la notificación
    private void showDownloadCompletedNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "download_channel")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Descarga simulada completada")
                .setContentText("La imagen Tilin.jpg ha sido guardada en la galería.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Anular el registro del receptor cuando se destruye la actividad
        unregisterReceiver(downloadReceiver);
    }
}

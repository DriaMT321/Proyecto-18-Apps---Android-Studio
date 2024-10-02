package com.example.proyectobroadcast;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_RECEIVE_SMS = 1;
    private TextView smsTextView;

    // Declara el receiver para recibir SMS
    private final BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Obtén los datos del SMS recibido
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender = smsMessage.getDisplayOriginatingAddress();
                        String messageBody = smsMessage.getMessageBody();

                        // Muestra el mensaje en el TextView
                        smsTextView.setText("De: " + sender + "\n" + messageBody);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsTextView = findViewById(R.id.smsTextView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    PERMISSIONS_REQUEST_RECEIVE_SMS);
        } else {
            registerSmsReceiver();
        }
    }

    private void registerSmsReceiver() {
        // Crea y registra el filtro de intent para la acción de recibir SMS
        IntentFilter smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, smsFilter);  // Registra el BroadcastReceiver para SMS
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegúrate de anular el registro del receiver al destruir la actividad
        unregisterReceiver(smsReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_RECEIVE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerSmsReceiver();
            } else {
                Toast.makeText(this, "Se requiere permiso para recibir SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

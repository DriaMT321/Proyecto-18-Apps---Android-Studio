package com.example.broadcastreceiverapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends AppCompatActivity {

    private BatteryLowReceiver batteryReceiver;
    private TextView batteryPercentageText;
    private Button checkBatteryButton;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryPercentageText = findViewById(R.id.txtBateria);
        checkBatteryButton = findViewById(R.id.button);

        // Inicializar el BroadcastReceiver con el TextView
        batteryReceiver = new BatteryLowReceiver(batteryPercentageText);

        // Configuración del botón
        checkBatteryButton.setOnClickListener(v -> checkBatteryStatus());

        // Solicitar permiso de notificación si es necesario
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }

        // Registrar el receptor de batería
        registerBatteryReceiver();
    }

    private void checkBatteryStatus() {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;
        int batteryPercentage = (int) (batteryPct * 100);

        batteryPercentageText.setText("Porcentaje de batería: " + batteryPercentage + "%");

        // Simular nivel de batería bajo para probar la notificación
        if (batteryPercentage > 15) {
            batteryReceiver.onReceive(this, new Intent().putExtra("level", 15));
        }
    }

    private void registerBatteryReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }
}
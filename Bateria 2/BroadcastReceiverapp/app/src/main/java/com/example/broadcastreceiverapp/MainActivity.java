package com.example.broadcastreceiverapp;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private BatteryLowReceiver batteryReceiver;
    private TextView batteryPercentageText;
    private Button checkBatteryButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        batteryPercentageText = findViewById(R.id.txtBateria);
        checkBatteryButton = findViewById(R.id.button);
        // Inicializar el BroadcastReceiver con el TextView
        batteryReceiver = new BatteryLowReceiver(batteryPercentageText);

        // Configuración del botón
        checkBatteryButton.setOnClickListener(v -> registerBatteryReceiver());
    }
    private void registerBatteryReceiver() {
        // Registrar el BroadcastReceiver para escuchar los cambios en la batería
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desregistrar el BroadcastReceiver cuando la actividad se destruya
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }
}

package com.example.broadcastnivelmanifest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static TextView estadoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        estadoTextView = findViewById(R.id.estado); // Inicializar el TextView
    }

    // Método para que el BroadcastReceiver pueda actualizar el TextView
    public static void updateWifiStatus(String estado) {
        estadoTextView.setText(estado);
    }
}
package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    public Context context;
    private static final String TAG = "MainActivity";  // Definir un tag para el Logcat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enviar el broadcast
                Intent intent = new Intent(MainActivity.this,ButtonReceiver.class);
                Log.d(TAG, "Enviando Broadcast...");  // Registrar el envío del Broadcast
                sendBroadcast(intent);
            }
        });
    }
}

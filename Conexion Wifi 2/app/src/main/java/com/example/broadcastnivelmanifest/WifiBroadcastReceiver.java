package com.example.broadcastnivelmanifest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        String estado;
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI&&networkInfo.isConnected()) {
            estado = "Conectado a WiFi";
        } else {
            estado = "Desconectado de WiFi";
        }

        // Actualiza el estado en MainActivity
        MainActivity.updateWifiStatus(estado);
    }
}

package com.example.conexionwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        String estado;
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            if (networkInfo.isConnected()) {
                estado = "Conectado a WiFi";
            } else {
                estado = "Desconectado de WiFi";
            }
        } else {
            estado = "Desconectado de WiFi";
        }

        // Actualizar el TextView
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            TextView estadoTextView = mainActivity.findViewById(R.id.estado);
            estadoTextView.setText(estado);
        }
    }
}

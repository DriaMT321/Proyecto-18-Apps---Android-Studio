package com.example.broadcastreceiverapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryLowReceiver extends BroadcastReceiver {
    private TextView batteryPercentageText;

    public BatteryLowReceiver(TextView batteryPercentageText) {
        this.batteryPercentageText = batteryPercentageText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra("level", 0);
        batteryPercentageText.setText("Porcentaje de batería: " + level + "%");
    }
}


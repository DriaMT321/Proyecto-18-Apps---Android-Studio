package com.example.blutu;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;

public class BluetoothConnectionReceiver extends BroadcastReceiver {

    private MediaPlayer connectSound;
    private MediaPlayer disconnectSound;

    public BluetoothConnectionReceiver(Context context) {
        connectSound = MediaPlayer.create(context, R.raw.waza); // Replace with your sound file
        disconnectSound = MediaPlayer.create(context, R.raw.dun_dun); // Replace with your sound file
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.ERROR);
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                connectSound.start();
            } else if (state == BluetoothAdapter.STATE_DISCONNECTED) {
                disconnectSound.start();
            }
        }
    }
}
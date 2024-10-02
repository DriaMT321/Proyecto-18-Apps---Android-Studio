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

    @Override
    public void onReceive(Context context, Intent intent) {
        if (connectSound == null) {
            connectSound = MediaPlayer.create(context, R.raw.waza);
        }
        if (disconnectSound == null) {
            disconnectSound = MediaPlayer.create(context, R.raw.dun_dun);
        }

        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            if (state == BluetoothAdapter.STATE_ON) {
                connectSound.start();
            } else if (state == BluetoothAdapter.STATE_OFF) {
                disconnectSound.start();
            }
        }
    }
}
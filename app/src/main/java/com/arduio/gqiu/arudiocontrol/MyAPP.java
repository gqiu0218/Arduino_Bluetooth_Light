package com.arduio.gqiu.arudiocontrol;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

public class MyAPP extends Application {

    private BluetoothSocket mBluetoothSocket;


    public BluetoothSocket getBluetoothSocket() {
        return mBluetoothSocket;
    }

    public void setBluetoothSocket(BluetoothSocket mBluetoothSocket) {
        this.mBluetoothSocket = mBluetoothSocket;
    }


}

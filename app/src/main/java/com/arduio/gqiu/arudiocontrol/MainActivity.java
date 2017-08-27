package com.arduio.gqiu.arudiocontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements MainAdapter.BluetoothConnectionListener {
    private static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private ListView mListView;
    private BluetoothAdapter mBluetoothAdapter;
    private MainAdapter mAdapter;
    private MyAPP mApp;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(MainActivity.this, "开始连接", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    jumpToSendData();
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        init();
        openBluetooth();
    }


    private void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mAdapter = new MainAdapter(this, this);
        mListView.setAdapter(mAdapter);
        mApp = (MyAPP) getApplication();
    }

    private void openBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            //弹出对话框提示用户是否打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, 1);
        } else {
            showBluetoothes();
        }
    }


    private void showBluetoothes() {
        //获取已配对蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        mAdapter.setData(devices);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        showBluetoothes();
    }

    @Override
    public void connection(final BluetoothDevice device) {
        new Thread() {
            @Override
            public void run() {
                connect(device);
            }
        }.start();

    }

    private void connect(BluetoothDevice btDev) {
        try {
            //通过和服务器协商的uuid来进行连接
            BluetoothSocket mBluetoothSocket = btDev.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            mApp.setBluetoothSocket(mBluetoothSocket);
            Log.e("gqiu", "开始连接...");
            mHandler.sendEmptyMessage(1);
            //如果当前socket处于非连接状态则调用连接
            if (!mBluetoothSocket.isConnected()) {
                mBluetoothSocket.connect();
            }
            Log.e("gqiu", "已经连接");
            mHandler.sendEmptyMessage(2);
        } catch (Exception e) {
            Log.e("gqiu", "连接失败");
            mHandler.sendEmptyMessage(3);
            try {
                mApp.getBluetoothSocket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void jumpToSendData() {
        Intent intent = new Intent(this, BluetoothSendDataActivity.class);
        startActivity(intent);
    }

}

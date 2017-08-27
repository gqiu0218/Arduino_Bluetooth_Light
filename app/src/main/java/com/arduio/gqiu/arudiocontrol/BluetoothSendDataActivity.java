package com.arduio.gqiu.arudiocontrol;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.xcolorpicker.android.OnColorSelectListener;
import com.xcolorpicker.android.XColorPicker;

import java.io.OutputStream;

/**
 * 发送数据
 * Created by gqiu on 2017/7/30.
 */

public class BluetoothSendDataActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, OnColorSelectListener {
    private MyAPP app;
    private Switch switchBtn;
    private ImageView ivShowColor;
    private int mColor;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(BluetoothSendDataActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(BluetoothSendDataActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        switchBtn = (Switch) findViewById(R.id.switchColor);
        XColorPicker picker = (XColorPicker) findViewById(R.id.color_picker);
        picker.setOnColorSelectListener(this);
        ivShowColor = (ImageView) findViewById(R.id.iv_show_color);
        findViewById(R.id.btn_submit).setOnClickListener(this);

        switchBtn.setOnCheckedChangeListener(this);
        app = (MyAPP) getApplication();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            send("on");
        } else {
            send("off");
        }
    }


    private void send(final String txt) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BluetoothSocket socket = app.getBluetoothSocket();
                    OutputStream stream = socket.getOutputStream();
                    stream.write(txt.getBytes());
                    stream.flush();
                    mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(1);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (mColor == 0 || switchBtn.isChecked()) {
            return;
        }

        int R = (mColor & 0xFF0000) >> 16;
        int G = (mColor & 0x00FF00) >> 8;
        int B = mColor & 0x0000FF;
        send(R + "_" + G + "_" + B);
    }

    @Override
    public void onColorSelected(int newColor, int oldColor) {
        mColor = newColor;
        ivShowColor.setBackgroundColor(newColor);
    }
}

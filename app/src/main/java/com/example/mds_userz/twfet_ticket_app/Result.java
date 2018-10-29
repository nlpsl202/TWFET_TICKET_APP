package com.example.mds_userz.twfet_ticket_app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import HPRTAndroidSDK.HPRTPrinterHelper;

public class Result extends Activity {
    private TextView tvResult, tvTotalPriceResult,tvPayWay;
    private Button btNext, btLast;
    private String result, resultEN, strTotalPrice,payWay,payWayEN;
    private int totalPrice;

    //SQLite
    private MyDBHelper mydbHelper;

    //印表機
    private HPRTPrinterHelper mHPRTPrinterHelper;

    //藍牙
    public static List<BluetoothDevice> connectedBluetoothDevices = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.result);

        //取得上個頁面傳來的值
        Intent intent = getIntent();
        payWay = intent.getStringExtra("PayWay");
        if(payWay.equals("現金")){
            payWayEN="Cash";
        }else{
            payWayEN="Credit Card";
        }

        mydbHelper = new MyDBHelper(this);

        mHPRTPrinterHelper = new HPRTPrinterHelper(this, "MPT-II");

        tvResult = findViewById(R.id.tvResult);
        tvTotalPriceResult = findViewById(R.id.tvTotalPriceResult);
        tvPayWay=findViewById(R.id.tvPayWay);
        btNext = findViewById(R.id.btNext);
        btLast = findViewById(R.id.btLast);

        if (connectedBluetoothDevices.size() < 1) {
            findBT();
            if (connectedBluetoothDevices.size() < 1) {
                mydbHelper.deleteTicket();
                Result.this.finish();
            }
        }

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mHPRTPrinterHelper.PrintText("\n\n2018 TAICHUNG WORLD FLORA\nEXPOSITION\n", 1, 2, 85);
                    mHPRTPrinterHelper.PrintText("PayWay : "+payWayEN+"\n", 0, 0, 85);
                    mHPRTPrinterHelper.PrintText(resultEN, 0, 0, 85);
                    mHPRTPrinterHelper.PrintText("Total : $" + strTotalPrice + "\n\n\n", 2, 2, 85);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydbHelper.deleteTicket();
                Result.this.finish();
            }
        });

        result = "";
        resultEN = "";
        DecimalFormat mDecimalFormat = new DecimalFormat("#,###");
        String TK_NAME;
        String TK_NAME_EN;
        Cursor cursor = mydbHelper.getTicket();
        while (cursor.moveToNext()) {
            TK_NAME = cursor.getString(cursor.getColumnIndex("TK_NAME"));
            TK_NAME_EN = cursor.getString(cursor.getColumnIndex("TK_NAME_EN"));
            while (TK_NAME.length() < 5) {
                TK_NAME = TK_NAME + "　";
            }
            int TK_PRICE = cursor.getInt(cursor.getColumnIndex("TK_PRICE"));
            int TK_COUNT = cursor.getInt(cursor.getColumnIndex("TK_COUNT"));
            totalPrice = totalPrice + TK_PRICE * TK_COUNT;
            strTotalPrice = mDecimalFormat.format((double) totalPrice);
            result = result + TK_NAME + " * " + mDecimalFormat.format((double) TK_COUNT) + " 張 = " + mDecimalFormat.format((double) TK_PRICE * TK_COUNT) + " 元\n";
            resultEN = resultEN + TK_NAME_EN + " * " + mDecimalFormat.format((double) TK_COUNT) + " = $" + mDecimalFormat.format((double) TK_PRICE * TK_COUNT) + "\n\n";
        }
        cursor.close();

        tvResult.setText(result);
        tvTotalPriceResult.setText("總計 " + strTotalPrice + " 元");
        tvPayWay.setText("付款方式  "+payWay);

        mydbHelper.deleteTicket();

        //監聽藍芽狀態
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, filter);
    }

    //按返回鍵視同按上一步
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            mydbHelper.deleteTicket();
            Result.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    void findBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(Result.this, "無藍牙功能", Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBluetooth);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("MPT-II")) {
                    try {
                        if (mHPRTPrinterHelper.PortOpen("Bluetooth," + device.getAddress()) == 0) {
                            Toast.makeText(Result.this, "藍牙連接成功", Toast.LENGTH_SHORT).show();
                            connectedBluetoothDevices.add(device);
                        } else {
                            Toast.makeText(Result.this, "藍牙連接失敗", Toast.LENGTH_SHORT).show();
                            connectedBluetoothDevices.clear();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Result.this, "藍牙連接失敗", Toast.LENGTH_SHORT).show();
                        connectedBluetoothDevices.clear();
                    }
                }
            }
        }
    }

    //監聽藍牙連接狀態的廣播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                connectedBluetoothDevices.clear();
                mydbHelper.deleteTicket();
                Result.this.finish();
            }
        }
    };
}

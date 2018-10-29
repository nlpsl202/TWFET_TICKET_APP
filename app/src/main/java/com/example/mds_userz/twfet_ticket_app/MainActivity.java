package com.example.mds_userz.twfet_ticket_app;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private String payWay;
    private RadioGroup radioGroup;
    private RadioButton cashRBtn;
    private CheckBox cbOA, cbOB, cbOC, cbOD, cbOE, cbOF, cbOG, cbOH, cbOJ;
    private EditText etOA, etOB, etOC, etOD, etOE, etOF, etOG, etOH, etOJ;
    private Button btNext;
    private List<CheckBox> allCheckBoxes = new ArrayList<CheckBox>();
    private List<EditText> allEditTexts = new ArrayList<EditText>();

    //SQLite
    private MyDBHelper mydbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mydbHelper = new MyDBHelper(this);

        cbOA = findViewById(R.id.cbOA);
        cbOB = findViewById(R.id.cbOB);
        cbOC = findViewById(R.id.cbOC);
        cbOD = findViewById(R.id.cbOD);
        cbOE = findViewById(R.id.cbOE);
        cbOF = findViewById(R.id.cbOF);
        cbOG = findViewById(R.id.cbOG);
        cbOH = findViewById(R.id.cbOH);
        cbOJ = findViewById(R.id.cbOJ);
        etOA = findViewById(R.id.etOA);
        etOB = findViewById(R.id.etOB);
        etOC = findViewById(R.id.etOC);
        etOD = findViewById(R.id.etOD);
        etOE = findViewById(R.id.etOE);
        etOF = findViewById(R.id.etOF);
        etOG = findViewById(R.id.etOG);
        etOH = findViewById(R.id.etOH);
        etOJ = findViewById(R.id.etOJ);
        btNext = findViewById(R.id.btNext);
        radioGroup = findViewById(R.id.InOutRG);
        cashRBtn = (RadioButton) findViewById(R.id.cashRBtn);

        cashRBtn.setChecked(true);
        payWay="現金";

        allCheckBoxes.add(cbOA);
        allCheckBoxes.add(cbOB);
        allCheckBoxes.add(cbOC);
        allCheckBoxes.add(cbOD);
        allCheckBoxes.add(cbOE);
        allCheckBoxes.add(cbOF);
        allCheckBoxes.add(cbOG);
        allCheckBoxes.add(cbOH);
        allCheckBoxes.add(cbOJ);
        allEditTexts.add(etOA);
        allEditTexts.add(etOB);
        allEditTexts.add(etOC);
        allEditTexts.add(etOD);
        allEditTexts.add(etOE);
        allEditTexts.add(etOF);
        allEditTexts.add(etOG);
        allEditTexts.add(etOH);
        allEditTexts.add(etOJ);

        for (CheckBox cb : allCheckBoxes) {
            cb.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (CheckBox cb : allCheckBoxes) {
                        if (cb.isChecked()) {
                            String TK_NAME = "";
                            String TK_NAME_EN = "";
                            String TK_CODE = "";
                            int TK_PRICE = 0;
                            int TK_COUNT = 0;
                            switch (cb.getId()) {
                                case R.id.cbOA:
                                    TK_NAME = "全票";
                                    TK_NAME_EN = "FULL TICKET";
                                    TK_CODE = "OA";
                                    TK_PRICE = 350;
                                    TK_COUNT = Integer.parseInt(etOA.getText().toString().trim());
                                    break;
                                case R.id.cbOB:
                                    TK_NAME = "學生票";
                                    TK_NAME_EN = "STUDENT TICKET";
                                    TK_CODE = "OB";
                                    TK_PRICE = 250;
                                    TK_COUNT = Integer.parseInt(etOB.getText().toString().trim());
                                    break;
                                case R.id.cbOC:
                                    TK_NAME = "優待票";
                                    TK_NAME_EN = "DISCOUNTED TICKET";
                                    TK_CODE = "OC";
                                    TK_PRICE = 175;
                                    TK_COUNT = Integer.parseInt(etOC.getText().toString().trim());
                                    break;
                                case R.id.cbOD:
                                    TK_NAME = "星光票";
                                    TK_NAME_EN = "EVENING TICKET";
                                    TK_CODE = "OD";
                                    TK_PRICE = 150;
                                    TK_COUNT = Integer.parseInt(etOD.getText().toString().trim());
                                    break;
                                case R.id.cbOE:
                                    TK_NAME = "三日票";
                                    TK_NAME_EN = "THREE-DAY PASS";
                                    TK_CODE = "OE";
                                    TK_PRICE = 650;
                                    TK_COUNT = Integer.parseInt(etOE.getText().toString().trim());
                                    break;
                                case R.id.cbOF:
                                    TK_NAME = "全期間票";
                                    TK_NAME_EN = "FLORA EXPO PASS";
                                    TK_CODE = "OF";
                                    TK_PRICE = 2500;
                                    TK_COUNT = Integer.parseInt(etOF.getText().toString().trim());
                                    break;
                                case R.id.cbOG:
                                    TK_NAME = "團體票";
                                    TK_NAME_EN = "GROUP TICKET";
                                    TK_CODE = "OG";
                                    TK_PRICE = 230;
                                    TK_COUNT = Integer.parseInt(etOG.getText().toString().trim());
                                    break;
                                case R.id.cbOH:
                                    TK_NAME = "二日票";
                                    TK_NAME_EN = "TWO-DAY PASS";
                                    TK_CODE = "OH";
                                    TK_PRICE = 450;
                                    TK_COUNT = Integer.parseInt(etOH.getText().toString().trim());
                                    break;
                                case R.id.cbOJ:
                                    TK_NAME = "學校團體票";
                                    TK_NAME_EN = "STUDENT GROUP TICKET";
                                    TK_CODE = "OJ";
                                    TK_PRICE = 175;
                                    TK_COUNT = Integer.parseInt(etOJ.getText().toString().trim());
                                    break;
                            }

                            if (TK_COUNT <= 0) {
                                Toast.makeText(MainActivity.this, "票券數量必須大於0", Toast.LENGTH_SHORT).show();
                                mydbHelper.deleteTicket();
                                return;
                            }
                            mydbHelper.InsertToTicket(TK_CODE, TK_NAME, TK_NAME_EN, TK_PRICE, TK_COUNT);
                            //mydbHelper.InsertToTicket(TK_CODE, cb.getText().toString().trim().substring(0, cb.getText().toString().trim().indexOf('票') + 1), TK_PRICE, TK_COUNT);
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                    mydbHelper.deleteTicket();
                    return;
                }


                if (cbOG.isChecked() && Integer.parseInt(etOG.getText().toString().trim()) < 20) {
                    Toast.makeText(MainActivity.this, "團體票必須大於20張", Toast.LENGTH_SHORT).show();
                    mydbHelper.deleteTicket();
                    return;
                }

                Cursor cursor = mydbHelper.getTicket();
                if (!cursor.moveToNext()) {
                    Toast.makeText(MainActivity.this, "請選擇票種與數量", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor.close();

                Intent callSub = new Intent();
                callSub.setClass(MainActivity.this, Result.class);
                callSub.putExtra("PayWay", payWay);
                startActivity(callSub);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cashRBtn) {
                    payWay = "現金";
                } else {
                    payWay = "信用卡";
                }
            }
        });

        //監聽藍芽狀態
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mydbHelper.deleteTicket();
    }

    //打勾就可以輸入數量，沒打勾無法輸入
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.cbOA:
                    if (isChecked) {
                        etOA.setEnabled(true);
                        etOA.setFocusableInTouchMode(true);
                    } else {
                        etOA.setEnabled(false);
                        etOA.setFocusable(false);
                        etOA.setText("0");
                    }
                    break;
                case R.id.cbOB:
                    if (isChecked) {
                        etOB.setEnabled(true);
                        etOB.setFocusableInTouchMode(true);
                    } else {
                        etOB.setEnabled(false);
                        etOB.setFocusable(false);
                        etOB.setText("0");
                    }
                    break;
                case R.id.cbOC:
                    if (isChecked) {
                        etOC.setEnabled(true);
                        etOC.setFocusableInTouchMode(true);
                    } else {
                        etOC.setEnabled(false);
                        etOC.setFocusable(false);
                        etOC.setText("0");
                    }
                    break;
                case R.id.cbOD:
                    if (isChecked) {
                        etOD.setEnabled(true);
                        etOD.setFocusableInTouchMode(true);
                    } else {
                        etOD.setEnabled(false);
                        etOD.setFocusable(false);
                        etOD.setText("0");
                    }
                    break;
                case R.id.cbOE:
                    if (isChecked) {
                        etOE.setEnabled(true);
                        etOE.setFocusableInTouchMode(true);
                    } else {
                        etOE.setEnabled(false);
                        etOE.setFocusable(false);
                        etOE.setText("0");
                    }
                    break;
                case R.id.cbOF:
                    if (isChecked) {
                        etOF.setEnabled(true);
                        etOF.setFocusableInTouchMode(true);
                    } else {
                        etOF.setEnabled(false);
                        etOF.setFocusable(false);
                        etOF.setText("0");
                    }
                    break;
                case R.id.cbOG:
                    if (isChecked) {
                        etOG.setEnabled(true);
                        etOG.setFocusableInTouchMode(true);
                    } else {
                        etOG.setEnabled(false);
                        etOG.setFocusable(false);
                        etOG.setText("0");
                    }
                    break;
                case R.id.cbOH:
                    if (isChecked) {
                        etOH.setEnabled(true);
                        etOH.setFocusableInTouchMode(true);
                    } else {
                        etOH.setEnabled(false);
                        etOH.setFocusable(false);
                        etOH.setText("0");
                    }
                    break;
                case R.id.cbOJ:
                    if (isChecked) {
                        etOJ.setEnabled(true);
                        etOJ.setFocusableInTouchMode(true);
                    } else {
                        etOJ.setEnabled(false);
                        etOJ.setFocusable(false);
                        etOJ.setText("0");
                    }
                    break;
            }
        }
    };

    //監聽藍芽連接狀態的廣播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                Result.connectedBluetoothDevices.clear();
                Toast.makeText(MainActivity.this, "藍牙連線關閉", Toast.LENGTH_SHORT).show();
            }
        }
    };
}

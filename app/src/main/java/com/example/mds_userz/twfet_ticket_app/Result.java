package com.example.mds_userz.twfet_ticket_app;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

public class Result extends Activity {
    private TextView tvResult, tvTotalPriceResult;
    private String result;
    private int totalPrice;

    //SQLite
    private MyDBHelper mydbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.result);

        mydbHelper = new MyDBHelper(this);

        tvResult = (TextView) findViewById(R.id.tvResult);
        tvTotalPriceResult = (TextView) findViewById(R.id.tvTotalPriceResult);

        result = "";
        Cursor cursor = mydbHelper.getTicket();
        while (cursor.moveToNext()) {
            String TK_NAME = cursor.getString(cursor.getColumnIndex("TK_NAME"));
            while (TK_NAME.length() < 5) {
                TK_NAME = TK_NAME + "　";
            }
            int TK_PRICE = cursor.getInt(cursor.getColumnIndex("TK_PRICE"));
            int TK_COUNT = cursor.getInt(cursor.getColumnIndex("TK_COUNT"));
            totalPrice = totalPrice + TK_PRICE * TK_COUNT;
            result = result + TK_NAME + "  *  " + TK_COUNT + "  張  =  " + TK_PRICE * TK_COUNT + "  元\n";
        }
        cursor.close();

        tvResult.setText(result);
        tvTotalPriceResult.setText("總計  " + totalPrice + "  元");

        mydbHelper.deleteTicket();
    }

    //於登入頁面按下返回鍵後跳出確認視窗
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Result.this.finish();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

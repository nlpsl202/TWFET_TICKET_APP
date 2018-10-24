package com.example.mds_userz.twfet_ticket_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    protected String CREATE_TICKET = "create table if not exists Ticket(TK_CODE text NOT NULL, TK_NAME text NOT NULL, TK_PRICE int NOT NULL,TK_COUNT int NOT NULL,PRIMARY KEY (TK_CODE))";

    private final static String DATABASE_NAME = "mydata.db";
    private final static int DATABASE_VERSION = 1;

    public MyDBHelper(Context context) {
        //create database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase Database) {
        // TODO Auto-generated method stub
        //create table
        Database.execSQL(CREATE_TICKET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    public boolean InsertToTicket(String TK_CODE,String TK_NAME,int TK_PRICE,int TK_COUNT) {
        try {
            String statement = "insert into Ticket (TK_CODE,TK_NAME,TK_PRICE,TK_COUNT) values('" + TK_CODE + "','" + TK_NAME + "'," + TK_PRICE + "," + TK_COUNT + ")";
            super.getWritableDatabase().execSQL(statement);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Cursor getTicket() {
        Cursor cursor = super.getReadableDatabase().rawQuery("select * from Ticket", null);
        return cursor;
    }

    public void deleteTicket() {
        super.getWritableDatabase().execSQL("delete from Ticket");
        super.close();
    }
}

package com.binh.qrcode.history;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binh on 11/11/2017.
 * handle scanned item in local database
 */

public class HistoryManager {
    private Context context;

    public HistoryManager(Context context) {
        this.context = context;
    }

    public void clearHistory() {
        SQLiteOpenHelper   helper=new DBHelper(context);
        SQLiteDatabase db=null;
        try{
            db=helper.getWritableDatabase();
            db.delete(DBHelper.TABLE_NAME,null,null);
        }finally {
            close(null,db);
        }
    }

    public void addItem(HistoryItem item) {

    }

    public void getItem(int id) {

    }

    public void deleteItem(int id) {

    }

    public List<HistoryItem> getAll() {

        return new ArrayList<>();
    }
    private static void close(Cursor cursor, SQLiteDatabase database) {
        if (cursor != null) {
            cursor.close();
        }
        if (database != null) {
            database.close();
        }
    }
}

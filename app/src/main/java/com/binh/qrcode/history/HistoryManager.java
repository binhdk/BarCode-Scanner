package com.binh.qrcode.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

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
        SQLiteOpenHelper helper = new DBHelper(context);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(DBHelper.TABLE_NAME, null, null);
        } finally {
            close(null, db);
        }
    }

    public void addItem(HistoryItem item) {
        SQLiteOpenHelper helper = new DBHelper(context);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.TEXT_COL, item.getResult().getText());
            values.put(DBHelper.FORMAT_COL, item.getResult().getBarcodeFormat().name());
            values.put(DBHelper.DISPLAY_COL, item.getDisplayAndDetails());
            values.put(DBHelper.TIMESTAMP_COL, System.currentTimeMillis());
            db.insert(DBHelper.TABLE_NAME, null, values);
        } finally {
            close(null, db);
        }
    }

    public HistoryItem getItem(int id) {
        SQLiteOpenHelper helper = new DBHelper(context);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        HistoryItem historyItem = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_NAME, null, DBHelper.ID_COL, new String[]{String.valueOf(id)}, null, null, null);
            while (cursor.moveToNext()) {
                int itemId = cursor.getInt(0);
                String text = cursor.getString(1);
                String format = cursor.getString(2);
                String display = cursor.getString(3);
                long timestamp = cursor.getLong(4);
                Result result = new Result(text, null, null, BarcodeFormat.valueOf(format), timestamp);
                historyItem = new HistoryItem(itemId, result, display);
            }

        } catch (Exception e) {
            //
        } finally {
            close(cursor, db);
        }
        return historyItem;
    }

    public void deleteItem(int id) {
        SQLiteOpenHelper helper = new DBHelper(context);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(DBHelper.TABLE_NAME, DBHelper.ID_COL + '=' + id, null);
        } catch (Exception e) {
            //
        } finally {
            close(null, db);
        }
    }

    public List<HistoryItem> getAll() {
        List<HistoryItem> list = new ArrayList<>();
        SQLiteOpenHelper helper = new DBHelper(context);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String text = cursor.getString(1);
                String format = cursor.getString(2);
                String display = cursor.getString(3);
                long timestamp = cursor.getLong(4);
                Result result = new Result(text, null, null, BarcodeFormat.valueOf(format), timestamp);
                list.add(new HistoryItem(id, result, display));
            }

        } catch (Exception e) {
            Log.d("TAG",e.getMessage());
        } finally {
            close(cursor, db);
        }
        return list;
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

package com.goose.androidtest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(Problem item) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TITLE, item.getTitle());
        contentValue.put(DatabaseHelper.DESC, item.getDescription());
        contentValue.put(DatabaseHelper.EXP_DATE, item.getExpDate());
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public ArrayList<Problem> fetch() {
        ArrayList<Problem> results = new ArrayList<>();
        String[] columns = new String[] { DatabaseHelper.ID, DatabaseHelper.TITLE, DatabaseHelper.DESC, DatabaseHelper.EXP_DATE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TITLE));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DESC));
                    String expDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EXP_DATE));
                    Problem item = new Problem();
                    item.setId(id);
                    item.setTitle(title);
                    item.setDescription(desc);
                    item.setExpDate(expDate);
                    results.add(item);
                }
                while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) cursor.close();

        }
        return results;
    }

    public void delete(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + id, null);
    }

}
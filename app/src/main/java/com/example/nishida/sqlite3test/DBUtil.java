package com.example.nishida.sqlite3test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBUtil {

    public static void writeDB(String info, SQLiteDatabase db) throws Exception {
        ContentValues values = new ContentValues();
        values.put("info", info);
        db.insert(Common.DB_TABLE, null, values);
    }

    public static ArrayList<AdapterItem> readDB(ArrayList<AdapterItem> dbitems, SQLiteDatabase db) throws Exception {
        dbitems = new ArrayList<AdapterItem>();
        Cursor c = db.query(Common.DB_TABLE, new String[]{"id", "info"},
                null, null, null, null, "id desc");
        if (c.moveToFirst()) {
            do {
                Log.v("sqltest", Integer.toString(c.getInt(0)));
                Log.v("sqltest", c.getString(1));
                AdapterItem item = new AdapterItem();
                item.id = Integer.toString(c.getInt(0));
                item.text = c.getString(1);
                dbitems.add(item);
            } while (c.moveToNext());
        }
        c.close();
        return dbitems;
    }

}

package com.example.nishida.sqlite3test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


    private final static String DB_NAME    = "test.db";
    private final static String DB_TABLE   = "test";
    private final static int    DB_VERSION = 1;

    private EditText       editText;
    private SQLiteDatabase db;

    private ArrayList<AdapterItem> dbitems;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        try{
            readDB();
        } catch (Exception e){
            e.printStackTrace();
        }

        ListView lv = (ListView)findViewById(R.id.listItems);
        lv.setAdapter(new MyDbAdapter());

    }

    private class MyDbAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dbitems.size();
        }

        @Override
        public AdapterItem getItem(int pos) {
            return dbitems.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            Context context = MainActivity.this;
            AdapterItem item = dbitems.get(pos);

            if (view == null) {
                LinearLayout layout = new LinearLayout(context);
                layout.setPadding(10, 10, 10, 10);
                layout.setGravity(Gravity.CENTER_VERTICAL);
                view = layout;

                TextView textView = new TextView(context);
                textView.setTag("text");
                textView.setTextColor(Color.BLACK);
                textView.setPadding(10, 20, 10, 20);
                layout.addView(textView);
            }

            TextView textView = (TextView)view.findViewWithTag("text");
            textView.setText(item.text);
            return view;
        }
    }

    public void onClickWrite(View v) {
        try {
            editText = findViewById(R.id.editText);
            writeDB(editText.getText().toString());
            readDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeDB(String info) throws Exception {
        ContentValues values = new ContentValues();
        values.put("info", info);
        db.insert(DB_TABLE, null, values);
    }

    private void readDB() throws Exception {
        dbitems = new ArrayList<AdapterItem>();
        Cursor c = db.query(DB_TABLE, new String[]{"id", "info"},
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
        ListView lv = (ListView)findViewById(R.id.listItems);
        lv.setAdapter(new MyDbAdapter());
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists "+
                    DB_TABLE+"(id integer primary key autoincrement,info text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            db.execSQL("drop table if exists "+DB_TABLE);
            onCreate(db);
        }
    }
}

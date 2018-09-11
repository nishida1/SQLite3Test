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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private final static int MP = LinearLayout.LayoutParams.MATCH_PARENT;
    private final static String TAG_WRITE  = "write";
    private final static String TAG_READ   = "read";
    private final static String DB_NAME    = "test.db";
    private final static String DB_TABLE   = "test";
    private final static int    DB_VERSION = 1;

    private EditText       editText;
    private SQLiteDatabase db;

    private ArrayList<AdapterItem> items;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        editText = new EditText(this);
        editText.setText("");
        editText.setLayoutParams(new LinearLayout.LayoutParams(MP, WC));
        layout.addView(editText);

        layout.addView(makeButton("書き込み", TAG_WRITE));
        layout.addView(makeButton("読み込み", TAG_READ));

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        items = new ArrayList<AdapterItem>();
        for (int i = 0; i < 30; i++) {
            AdapterItem item = new AdapterItem();
            item.text = "項目"+(i+1);
            items.add(item);
        }
        ListView listView = new ListView(this);
        listView.setScrollingCacheEnabled(false);
        listView.setAdapter(new MyAdapter());
        layout.addView(listView);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public AdapterItem getItem(int pos) {
            return items.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            Context context = MainActivity.this;
            AdapterItem item = items.get(pos);

            if (view == null) {
                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.WHITE);
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

    private Button makeButton(String text, String tag) {
        Button button = new Button(this);
        button.setText(text);
        button.setTag(tag);
        button.setOnClickListener(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
        return button;
    }

    public void onClick(View v) {
        String tag = (String)v.getTag();
        if (TAG_WRITE.equals(tag)) {
            try {
                String str = editText.getText().toString();
                writeDB(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (TAG_READ.equals(tag)) {
            try {
                String str = readDB();
                editText.setText(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void writeDB(String info) throws Exception {
        ContentValues values = new ContentValues();
        //values.put("id", "0");
        values.put("info", info);
        //int colNum = db.update(DB_TABLE, values, null, null);
        //if (colNum == 0) {
            db.insert(DB_TABLE, null, values);
        //}
    }

    private String readDB() throws Exception {

        /*
        Cursor c = db.query(DB_TABLE, new String[]{"id", "info"},
                "id='0'", null, null, null, null);
        if (c.getCount() == 0) throw new Exception();
        c.moveToFirst();
        String str = c.getString(1);
        c.close();
        */

        //test start
        Cursor c = db.query(DB_TABLE, new String[]{"id", "info"},
                null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                //Log.v("sqltest", Integer.toString(c.getInt(0)));
                Log.v("sqltest", c.getString(1));
            } while (c.moveToNext());
        }
        c.close();
        String str = "";
        //test end

        return str;
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

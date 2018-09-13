package com.example.nishida.sqlite3test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private EditText       editText;
    private SQLiteDatabase db;

    private ArrayList<AdapterItem> dbitems;
    ArrayAdapter<String> adapter;

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

        setList();
    }

    private void setList() {
        ListView lv = (ListView)findViewById(R.id.listItems);
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < dbitems.size(); i++){
            AdapterItem item = dbitems.get(i);
            arrayList.add(item.text);
        }

        adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                arrayList);
        lv.setAdapter(adapter);
    }

    public void onClickWrite(View v) {
        try {
            editText = findViewById(R.id.editText);
            writeDB(editText.getText().toString());
            readDB();
            setList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeDB(String info) throws Exception {
        ContentValues values = new ContentValues();
        values.put("info", info);
        db.insert(Common.DB_TABLE, null, values);
    }

    private void readDB() throws Exception {
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
    }

}

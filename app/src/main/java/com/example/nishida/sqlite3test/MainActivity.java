package com.example.nishida.sqlite3test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
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
            dbitems = DBUtil.readDB(dbitems, db);
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
            DBUtil.writeDB(editText.getText().toString(), db);
            dbitems = DBUtil.readDB(dbitems, db);
            setList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.example.bitsattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {

    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList<String> listItems = new ArrayList();
    private long batch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        batch_id = getIntent().getLongExtra("batch_id",-1);

        loadListItems();

        sheetList = findViewById(R.id.sheetList);
        adapter = new ArrayAdapter(this,R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));
    }

    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollnumberArray = getIntent().getIntArrayExtra("rollnumberArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");

        Intent intent = new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollnumberArray",rollnumberArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("month",listItems.get(position));

        startActivity(intent);
    }

    private void loadListItems() {
        Cursor cursor = new DBHelper(this).getDistinctMonths(batch_id);

        while(cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_KEY));
            listItems.add(date.substring(3));
        }
    }
}
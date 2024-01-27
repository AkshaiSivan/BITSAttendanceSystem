package com.example.bitsattendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        showTable();
    }

    private void showTable() {

        DBHelper dbHelper = new DBHelper(this);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollnumberArray = getIntent().getIntArrayExtra("rollnumberArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        String month = getIntent().getStringExtra("month");

        int DAY_IN_MONTH = getDayInMonth(month);

        //Table ROW setup
        int rowSize = idArray.length+1;

        TableRow[] rows = new TableRow[rowSize];
        TextView[] rollNumber_textview = new TextView[rowSize];
        TextView[] name_textview = new TextView[rowSize];
        TextView[][] status_textview = new TextView[rowSize][DAY_IN_MONTH+1];

        for (int i=0; i<rowSize; i++){
            rollNumber_textview[i] = new TextView(this);
            name_textview[i] = new TextView(this);
            for (int j=1; j<=DAY_IN_MONTH; j++){
                status_textview[i][j] = new TextView(this);
            }
        }

        //Table HEADER
        rollNumber_textview[0].setText("Roll\nNumber");
        rollNumber_textview[0].setTypeface(rollNumber_textview[0].getTypeface(), Typeface.BOLD);
        name_textview[0].setText("Name");
        name_textview[0].setTypeface(name_textview[0].getTypeface(), Typeface.BOLD);

        for (int i=1; i<=DAY_IN_MONTH; i++){
            status_textview[0][i].setText(String.valueOf(i));
            status_textview[0][i].setTypeface(status_textview[0][i].getTypeface(), Typeface.BOLD);
        }

        for (int i=1; i<rowSize; i++){
            rollNumber_textview[i].setText(String.valueOf(rollnumberArray[i-1]));
            name_textview[i].setText(nameArray[i-1]);

            for (int j=1; j<=DAY_IN_MONTH; j++){
                String day = String.valueOf(j);
                if (day.length()==1) day = "0"+day;
                String date = day+"."+month;
                String status = dbHelper.getStatus(idArray[i-1],date);
                status_textview[i][j].setText(status);
            }
        }

        //Table ROW
        for (int i=0; i<rowSize; i++){
            rows[i] = new TableRow(this);

            if(i%2 == 0)
                rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            else rows[i].setBackgroundColor(Color.parseColor("#E4E4E4"));

            rollNumber_textview[i].setPadding(16,16,16,16);
            name_textview[i].setPadding(16,16,16,16);

            rows[i].addView(rollNumber_textview[i]);
            rows[i].addView(name_textview[i]);

            for (int j=1; j<=DAY_IN_MONTH; j++){
                status_textview[i][j].setPadding(16,16,16,16);

                // Add condition for changing background color based on student's status
                if (status_textview[i][j].getText().toString().equals("P")) {
                    status_textview[i][j].setBackgroundColor(Color.parseColor("#79F56C"));//Green
                } else if (status_textview[i][j].getText().toString().equals("A")) {
                    status_textview[i][j].setBackgroundColor(Color.parseColor("#F79797"));//Red
                } else {
                    status_textview[i][j].setBackgroundColor(Color.parseColor("#E4E4E4"));//Gray
                }

                rows[i].addView(status_textview[i][j]);
            }
            tableLayout.addView(rows[i]);
        }
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);

    }

    private int getDayInMonth(String month) {
        int monthIndex = Integer.valueOf(month.substring(0,2))-1;
        int year = Integer.parseInt(month.substring(3));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthIndex);
        calendar.set(Calendar.YEAR,year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
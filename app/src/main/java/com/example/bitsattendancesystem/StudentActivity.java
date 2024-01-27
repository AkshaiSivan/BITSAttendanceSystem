package com.example.bitsattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;

public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String batchName;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private DBHelper dbHelper;
    private long batch_id;
    private MyCalendar calendar;
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        calendar = new MyCalendar();

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        batchName = intent.getStringExtra("batchName");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position",-1);
        batch_id = intent.getLongExtra("batch_id",-1);

        setToolbar();
        loadData();

        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> changeStatus(position));

        loadStatusData();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(batch_id);
        studentItems.clear();
        while (cursor.moveToNext()){
            long student_id = cursor.getLong(cursor.getColumnIndex(DBHelper.STUDENT_ID));
            int rollnumber = cursor.getInt(cursor.getColumnIndex(DBHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(student_id,rollnumber,name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();

        if(status.equals("P")) status = "A";
        else status = "P";

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        subtitle = toolbar.findViewById(R.id.toolbar_subtitle);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        save.setOnClickListener(v->saveStatus());

        title.setText(batchName);
        subtitle.setText(subjectName+" | "+calendar.getDate());

        back.setOnClickListener(v->onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));
    }

    private void saveStatus() {
        for (StudentItem studentItem : studentItems){
            String status = studentItem.getStatus();
            if(status != "P") status = "A";
            long value = dbHelper.addStatus(studentItem.getStudent_id(),batch_id,calendar.getDate(),status);

            if(value == -1)dbHelper.updateStatus(studentItem.getStudent_id(),calendar.getDate(),status);
        }
    }

    private void loadStatusData(){
        for (StudentItem studentItem : studentItems){
            String status = dbHelper.getStatus(studentItem.getStudent_id(),calendar.getDate());
            if(status != null) studentItem.setStatus(status);
            else studentItem.setStatus("");
        }
        adapter.notifyDataSetChanged();
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }
        else if (menuItem.getItemId() == R.id.show_Calendar) {
            showCalendar();
        }
        else if (menuItem.getItemId() == R.id.show_attendance_sheet) {
            openSheetList();
        }
        return true;
    }

    private void openSheetList() {
        long[] idArray = new long[studentItems.size()];
        for (int i=0; i<idArray.length; i++)
            idArray[i] = studentItems.get(i).getStudent_id();

        int[] rollnumberArray = new int[studentItems.size()];
        for (int i=0; i<rollnumberArray.length; i++)
            rollnumberArray[i] = studentItems.get(i).getRoll_number();

        String[] nameArray = new String[studentItems.size()];
        for (int i=0; i<nameArray.length; i++)
            nameArray[i] = studentItems.get(i).getName();

        Intent intent = new Intent(this,SheetListActivity.class);
        intent.putExtra("batch_id",batch_id);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollnumberArray",rollnumberArray);
        intent.putExtra("nameArray",nameArray);
        startActivity(intent);
    }

    private void showCalendar() {
        calendar.show(getSupportFragmentManager(),"");
        calendar.setOnCalendarOkClickListener(this::onCalendarokClicked);
    }

    private void onCalendarokClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        subtitle.setText(subjectName+" | "+calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListner((roll_number,name)->addStudent(roll_number,name));
    }

    private void addStudent(String roll_number_string, String name) {
        int roll_number = Integer.parseInt(roll_number_string);
        long student_id = dbHelper.addStudent(batch_id,roll_number,name);
        StudentItem studentItem = new StudentItem(student_id,roll_number,name);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRoll_number(),studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListner((roll_number_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.updateStudent(studentItems.get(position).getStudent_id(),name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getStudent_id());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}
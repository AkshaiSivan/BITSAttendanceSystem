package com.example.bitsattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    Toolbar toolbar;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog());

        loadData();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        classAdapter = new ClassAdapter(this,classItems);
        recyclerView.setAdapter(classAdapter);

        classAdapter.setOnItemClickListner(position -> gotoItemActivity(position));

        setToolbar();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getBatchTable();

        classItems.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.BATCH_ID));
            String batchName = cursor.getString(cursor.getColumnIndex(DBHelper.BATCH_NAME_KEY));
            String subjectName = cursor.getString(cursor.getColumnIndex(DBHelper.SUBJECT_NAME_KEY));

            classItems.add(new ClassItem(id,batchName,subjectName));
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        TextView subtitle = toolbar.findViewById(R.id.toolbar_subtitle);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("BITS Attendance System");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class);

        intent.putExtra("batchName",classItems.get(position).getBatchName());
        intent.putExtra("subjectName",classItems.get(position).getSubjectName());
        intent.putExtra("position",position);
        intent.putExtra("batch_id",classItems.get(position).getBatch_id());
        startActivity(intent);
    }

    private void showDialog(){
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        dialog.setListner((className,subjectName)->addClass(className,subjectName));
    }

    private void addClass(String className,String subjectName) {
        long batch_id = dbHelper.addBatch(className,subjectName);
        ClassItem classItem = new ClassItem(batch_id,className,subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setListner((className,subjectName)->updateClass(position,className,subjectName));
    }

    private void updateClass(int position, String className, String subjectName) {
        dbHelper.updateBatch(classItems.get(position).getBatch_id(),className,subjectName);
        classItems.get(position).setBatchName(className);
        classItems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getBatch_id());
        classItems.remove(position);
        classAdapter.notifyItemRemoved(position);
    }
}
package com.example.bitsattendancesystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String CLASS_UPDATE_DIALOG = "updateClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";
    private OnClickListner listner;
    private int roll_number;
    private String name;

    public MyDialog(int roll_number, String name) {

        this.roll_number = roll_number;
        this.name = name;
    }

    public MyDialog() {

    }

    public interface OnClickListner{
        void onClick(String text1, String text2);
    }

    public void setListner(OnClickListner listner) {
        this.listner = listner;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = null;
        if(getTag().equals(CLASS_ADD_DIALOG))dialog = getAddClassDialog();
        if(getTag().equals(STUDENT_ADD_DIALOG))dialog = getAddStudentDialog();
        if(getTag().equals(CLASS_UPDATE_DIALOG))dialog = getUpdateClassDialog();
        if(getTag().equals(STUDENT_UPDATE_DIALOG))dialog = getUpdateStudentDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//for transparent background on dialog box
        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Student");

        EditText roll_number_edt = view.findViewById(R.id.batch_edit);
        EditText name_edt = view.findViewById(R.id.subject_edit);

        roll_number_edt.setHint("Roll Number");
        name_edt.setHint("Student Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");
        roll_number_edt.setText(roll_number+"");
        roll_number_edt.setEnabled(false);
        name_edt.setText(name);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll_number = roll_number_edt.getText().toString();
            String name = name_edt.getText().toString();
            listner.onClick(roll_number,name);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Batch");

        EditText class_edt = view.findViewById(R.id.batch_edit);
        EditText subject_edt = view.findViewById(R.id.subject_edit);

        class_edt.setHint("Batch Name");
        subject_edt.setHint("Subject Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String className = class_edt.getText().toString();
            String subjectName = subject_edt.getText().toString();

            listner.onClick(className,subjectName);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Student");

        EditText roll_number_edt = view.findViewById(R.id.batch_edit);
        EditText name_edt = view.findViewById(R.id.subject_edit);

        roll_number_edt.setHint("Roll Number");
        name_edt.setHint("Student Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String roll_number = roll_number_edt.getText().toString();
            String name = name_edt.getText().toString();
            roll_number_edt.setText(String.valueOf(Integer.parseInt(roll_number)+1));
            name_edt.setText("");
            listner.onClick(roll_number,name);
        });
        return builder.create();
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Batch");

        EditText class_edt = view.findViewById(R.id.batch_edit);
        EditText subject_edt = view.findViewById(R.id.subject_edit);

        class_edt.setHint("Batch Name");
        subject_edt.setHint("Subject Name");
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dismiss());
        add.setOnClickListener(v -> {
            String className = class_edt.getText().toString();
            String subjectName = subject_edt.getText().toString();

            listner.onClick(className,subjectName);
            dismiss();
        });
        return builder.create();
    }
}

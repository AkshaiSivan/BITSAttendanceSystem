package com.example.bitsattendancesystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyCalendar extends DialogFragment {
    Calendar calendar = Calendar.getInstance();
    public interface OnCalendarOkClickListener{
        void onClick(int year,int month,int day);
    }

    public OnCalendarOkClickListener onCalendarOkClickListener;

    public void setOnCalendarOkClickListener(OnCalendarOkClickListener onCalendarOkClickListener) {
        this.onCalendarOkClickListener = onCalendarOkClickListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),((view, year, month, dayOfMonth) -> {
            onCalendarOkClickListener.onClick(year,month,dayOfMonth);
        }),calendar.get(calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(calendar.DAY_OF_MONTH));
    }

    void setDate(int year,int month,int day){
        calendar.set(calendar.YEAR,year);
        calendar.set(calendar.MONTH,month);
        calendar.set(calendar.DAY_OF_MONTH,day);
    }

    String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(calendar.getTime());
    }
}

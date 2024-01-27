package com.example.bitsattendancesystem;

public class StudentItem {
    private int roll_number;
    private String name;
    private String status;
    private long student_id;

    public StudentItem(long student_id,int roll_number, String name) {
        this.student_id = student_id;
        this.roll_number = roll_number;
        this.name = name;
        status="";
    }

    public int getRoll_number() {
        return roll_number;
    }

    public void setRoll_number(int roll_number) {
        this.roll_number = roll_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }
}

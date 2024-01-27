package com.example.bitsattendancesystem;

public class ClassItem {
    private long batch_id;

    public ClassItem(long batch_id, String batchName, String subjectName) {
        this.batch_id = batch_id;
        this.batchName = batchName;
        this.subjectName = subjectName;
    }
    private String batchName;
    private String subjectName;

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public ClassItem(String batchName, String subjectName){
        this.batchName = batchName;
        this.subjectName = subjectName;
    }

    public long getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(int batch_id) {
        this.batch_id = batch_id;
    }
}

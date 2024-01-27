package com.example.bitsattendancesystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    //BATCH table
    private static final String BATCH_TABLE_NAME = "BATCH_TABLE";
    public static final String BATCH_ID = "_BID";
    public static final String BATCH_NAME_KEY = "BATCH_NAME";
    public static final String SUBJECT_NAME_KEY = "SUBJECT_NAME";
    private static final String CREATE_BATCH_TABLE =
            "CREATE TABLE " + BATCH_TABLE_NAME +
                    "(" +
                    BATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    BATCH_NAME_KEY + " TEXT NOT NULL, " +
                    SUBJECT_NAME_KEY + " TEXT NOT NULL, " +
                    "UNIQUE (" + BATCH_NAME_KEY + "," + SUBJECT_NAME_KEY + ")"+
                    ");";
    private static final String DROP_BATCH_TABLE = "DROP TABLE IF EXISTS "+BATCH_TABLE_NAME;
    private static final String SELECT_BATCH_TABLE = "SELECT * FROM "+BATCH_TABLE_NAME;

    //STUDENT table
    private static final String STUDENT_TABLE_NAME = "STUDENT_TABLE";
    public static final String STUDENT_ID = "_SID";
    public static final String STUDENT_NAME_KEY = "STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY = "ROLL_NUMBER";
    private static final String CREATE_STUDENT_TABLE =
            "CREATE TABLE " + STUDENT_TABLE_NAME +
                    "(" +
                    STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    BATCH_ID + " INTEGER NOT NULL, "+
                    STUDENT_NAME_KEY + " TEXT NOT NULL, " +
                    STUDENT_ROLL_KEY + " INTEGER, " +
                    "FOREIGN KEY (" + BATCH_ID +") REFERENCES "+ BATCH_TABLE_NAME + "("+BATCH_ID+")"+
                    ");";
    private static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS "+STUDENT_TABLE_NAME;
    private static final String SELECT_STUDENT_TABLE = "SELECT * FROM "+STUDENT_TABLE_NAME;

    //STATUS table
    private static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "_STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";
    private static final String CREATE_STATUS_TABLE =
            "CREATE TABLE " + STATUS_TABLE_NAME +
                    "(" +
                    STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    STUDENT_ID + " INTEGER NOT NULL, "+
                    BATCH_ID + " INTEGER NOT NULL, "+
                    DATE_KEY + " DATE NOT NULL, " +
                    STATUS_KEY + " TEXT NOT NULL, " +
                    "UNIQUE ("+ STUDENT_ID + "," +DATE_KEY+"),"+
                    "FOREIGN KEY (" + STUDENT_ID +") REFERENCES "+ STUDENT_TABLE_NAME + "("+STUDENT_ID+"),"+
                    "FOREIGN KEY (" + BATCH_ID +") REFERENCES "+ BATCH_TABLE_NAME + "("+BATCH_ID+")"+
                    ");";
    private static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS "+STATUS_TABLE_NAME;
    private static final String SELECT_STATUS_TABLE = "SELECT * FROM "+STATUS_TABLE_NAME;

    public DBHelper(@Nullable Context context) {
        super(context, "Attendance.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BATCH_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_BATCH_TABLE);
            db.execSQL(DROP_STUDENT_TABLE);
            db.execSQL(DROP_STATUS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //BATCH table activities
    long addBatch(String batchName,String subjectName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BATCH_NAME_KEY,batchName);
        values.put(SUBJECT_NAME_KEY,subjectName);

        return database.insert(BATCH_TABLE_NAME,null,values);
    }

    Cursor getBatchTable(){
        SQLiteDatabase database = this.getReadableDatabase();

        return database.rawQuery(SELECT_BATCH_TABLE,null);
    }

    int deleteClass(long batch_id){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(BATCH_TABLE_NAME,BATCH_ID+"=?",new String[]{String.valueOf(batch_id)});
    }

    long updateBatch(long batch_id,String batchName,String subjectName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BATCH_NAME_KEY,batchName);
        values.put(SUBJECT_NAME_KEY,subjectName);

        return database.update(BATCH_TABLE_NAME,values,BATCH_ID+"=?",new String[]{String.valueOf(batch_id)});
    }

    //STUDENT table activities
    long addStudent(long batch_id,int rollnumber,String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BATCH_ID,batch_id);
        values.put(STUDENT_ROLL_KEY,rollnumber);
        values.put(STUDENT_NAME_KEY,name);

        return database.insert(STUDENT_TABLE_NAME,null,values);
    }

    Cursor getStudentTable(long batch_id){
        SQLiteDatabase database = this.getReadableDatabase();

        return database.query(STUDENT_TABLE_NAME,null,BATCH_ID+"=?",new String[]{String.valueOf(batch_id)},null,null,STUDENT_ROLL_KEY);
    }

    int deleteStudent(long student_id){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(STUDENT_TABLE_NAME,STUDENT_ID+"=?",new String[]{String.valueOf(student_id)});
    }

    long updateStudent(long student_id,String name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY,name);

        return database.update(STUDENT_TABLE_NAME,values,STUDENT_ID+"=?",new String[]{String.valueOf(student_id)});
    }

    long addStatus(long student_id,long batch_id,String date,String status){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_ID,student_id);
        values.put(BATCH_ID,batch_id);
        values.put(DATE_KEY,date);
        values.put(STATUS_KEY,status);

        return database.insert(STATUS_TABLE_NAME,null,values);
    }

    long updateStatus(long student_id,String date,String status){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY,status);
        String whereClause = DATE_KEY +"='"+date+"' AND "+STUDENT_ID+"="+student_id;
        return database.update(STATUS_TABLE_NAME,values,whereClause,null);
    }

    String getStatus(long student_id,String date){
        String status = null;
        SQLiteDatabase database = this.getReadableDatabase();
        String whereClause = DATE_KEY +"='"+date+"' AND "+STUDENT_ID+"="+student_id;
        Cursor cursor = database.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
        if (cursor.moveToFirst())
            status = cursor.getString(cursor.getColumnIndex(STATUS_KEY));
        return status;
    }

    Cursor getDistinctMonths(long batch_id){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STATUS_TABLE_NAME,new String[]{DATE_KEY},BATCH_ID+"="+batch_id,null,"substr("+DATE_KEY+",4,7)",null,null);
    }
}
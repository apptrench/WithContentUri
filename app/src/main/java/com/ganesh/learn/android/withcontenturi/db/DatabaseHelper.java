package com.ganesh.learn.android.withcontenturi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ganesh.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "payroll.db";
    private static final int DATABASE_VERSION = 1;

    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " text ";

    private static final String INT_TYPE = " INTEGER ";
    private static final String SQL_CREATE_EMP =
            "CREATE TABLE Employee(" +
                    Columns.ID + " INTEGER PRIMARY KEY," +
                    Columns.EMP_ID + TEXT_TYPE + COMMA_SEP +
                    Columns.FIRST_NAME + TEXT_TYPE + COMMA_SEP +
                    Columns.LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    Columns.DEPT_ID + INT_TYPE + COMMA_SEP +
                    "FOREIGN KEY(" + Columns.DEPT_ID + ") REFERENCES Department(" + Columns.ID + "))";

    private static final String SQL_CREATE_DEPT =
            "CREATE TABLE Department(" +
                    Columns.ID + " INTEGER PRIMARY KEY," +
                    Columns.NAME + TEXT_TYPE + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DEPT);
        db.execSQL(SQL_CREATE_EMP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}

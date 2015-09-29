package com.ganesh.learn.android.withcontenturi.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.ganesh.learn.android.withcontenturi.db.PayRollProviderContract.Department;
import com.ganesh.learn.android.withcontenturi.db.PayRollProviderContract.DepartmentStats;
import com.ganesh.learn.android.withcontenturi.db.PayRollProviderContract.Employee;

public class PayRollProvider extends ContentProvider {
    private static final int EMPLOYEE_LIST = 1;
    private static final int EMPLOYEE_ID = 2;
    private static final int DEPT_LIST = 3;
    private static final int DEPT_ID = 4;
    private static final int DEPT_STATS = 5;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PayRollProviderContract.AUTHORITY, Employee.TABLE_NAME, EMPLOYEE_LIST);
        URI_MATCHER.addURI(PayRollProviderContract.AUTHORITY, Employee.TABLE_NAME + "/#", EMPLOYEE_ID);
        URI_MATCHER.addURI(PayRollProviderContract.AUTHORITY, Department.TABLE_NAME, DEPT_LIST);
        URI_MATCHER.addURI(PayRollProviderContract.AUTHORITY, Department.TABLE_NAME + "/#", DEPT_ID);
        URI_MATCHER.addURI(PayRollProviderContract.AUTHORITY, DepartmentStats.TABLE_NAME, DEPT_STATS);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case EMPLOYEE_LIST:
                return Employee.CONTENT_TYPE;
            case EMPLOYEE_ID:
                return Employee.CONTENT_ITEM_TYPE;
            case DEPT_LIST:
                return Department.CONTENT_TYPE;
            case DEPT_ID:
                return Department.CONTENT_ITEM_TYPE;
            case DEPT_STATS:
                return Department.CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        checkUri(uri);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor;

        switch (URI_MATCHER.match(uri)) {
            case DEPT_STATS:
                String allColumns = TextUtils.join(",", projection);
                String query = "select "+allColumns+" from department left outer join employee on employee.deptId = department._id " +
                        "group by department._id";
                cursor = db.rawQuery(query, selectionArgs);
                cursor.setNotificationUri(getContext().getContentResolver(), PayRollProviderContract.CONTENT_URI);
                break;
            default:
                SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables(getTableName(uri));

                cursor = queryBuilder.query(db, projection, getAppendedWhere(uri, selection), selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        checkUriForInsertion(uri);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.insert(getTableName(uri), null, values);
        return getUriForId(id, uri);
    }

    private void checkUriForInsertion(Uri uri) {
        checkUri(uri);

        int match = URI_MATCHER.match(uri);
        if (match == EMPLOYEE_ID || match == DEPT_ID) {
            throw new IllegalArgumentException("Invalid Uri for insertion");
        }
    }

    private String getTableName(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case EMPLOYEE_ID:
            case EMPLOYEE_LIST:
                return Employee.TABLE_NAME;

            case DEPT_ID:
            case DEPT_LIST:
                return Department.TABLE_NAME;
            case DEPT_STATS:
                return Employee.TABLE_NAME + " INNER JOIN " + Department.TABLE_NAME;
        }
        return "";
    }

    private void checkUri(Uri uri) {
        int match = URI_MATCHER.match(uri);
        if (match != EMPLOYEE_ID && match != EMPLOYEE_LIST && match != DEPT_ID && match != DEPT_LIST && match != DEPT_STATS) {
            throw new IllegalArgumentException("Invalid Uri specified");
        }
    }

    /* This piece of code I got from
        http://www.grokkingandroid.com/android-tutorial-writing-your-own-content-provider/ */
    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        checkUri(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        final int deletedRows = db.delete(getTableName(uri), getAppendedWhere(uri, selection),
                selectionArgs);
        notifyObservers(uri, deletedRows);

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        checkUri(uri);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        final int updatedRows = db.update(getTableName(uri), values, getAppendedWhere(uri, selection),
                selectionArgs);
        notifyObservers(uri, updatedRows);

        return updatedRows;
    }

    private void notifyObservers(Uri uri, int noOfRowsModified) {
        if (noOfRowsModified > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
    }

    private String getAppendedWhere(Uri uri, String selection) {
        int match = URI_MATCHER.match(uri);
        if (match == EMPLOYEE_ID || match == DEPT_ID) {
            String idWhere = BaseColumns._ID + " = " + uri.getLastPathSegment();
            if (!TextUtils.isEmpty(selection)) {
                return idWhere + " AND " + selection;
            } else {
                return idWhere;
            }
        }
        return selection;
    }
}
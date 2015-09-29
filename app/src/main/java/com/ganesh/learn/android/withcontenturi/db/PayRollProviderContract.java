package com.ganesh.learn.android.withcontenturi.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

final public class PayRollProviderContract {
    //TODO: Specify your App's package name
    public static final String PACKAGE = "com.ganesh.learn.android.withcontenturi";
    public static final String AUTHORITY = PACKAGE + ".provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Employee implements BaseColumns {
        public static final String TABLE_NAME = "Employee";

        private static final String FIRST_NAME = Columns.FIRST_NAME;
        private static final String LAST_NAME = Columns.LAST_NAME;
        private static final String EMP_ID = Columns.EMP_ID;

        //Column indices
        public static final int _ID_COLUMN_INDEX = 0;

        public static final int FIRST_NAME_COLUMN_INDEX = 1;
        public static final int LAST_NAME_COLUMN_INDEX = 2;
        public static final int EMP_ID_COLUMN_INDEX = 3;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(PayRollProviderContract.CONTENT_URI, TABLE_NAME);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "vnd." + PACKAGE + "." + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "vnd." + PACKAGE + "." + TABLE_NAME;

        public static final String[] PROJECTION_ALL = {_ID, FIRST_NAME, LAST_NAME, EMP_ID};
    }

    public static final class Department implements BaseColumns {
        public static final String TABLE_NAME = "Department";

        private static final String NAME = Columns.NAME;

        //Column indices
        public static final int _ID_COLUMN_INDEX = 0;
        public static final int NAME_COLUMN_INDEX = 1;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(PayRollProviderContract.CONTENT_URI, TABLE_NAME);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "vnd." + PACKAGE + "." + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "vnd." + PACKAGE + "." + TABLE_NAME;

        public static final String[] PROJECTION_ALL = {_ID, NAME};
    }

    public static final class DepartmentStats implements BaseColumns {
        public static final String TABLE_NAME = "EmpDepartment";

        private static final String NAME = Columns.NAME;
        private static final String NO_OF_EMPLOYEES = "count(employee._id) as noOfEmployees";

        //Column indices
        public static final int _ID_COLUMN_INDEX = 0;
        public static final int NAME_COLUMN_INDEX = 1;
        public static final int NO_OF_EMPLOYEES_COLUMN_INDEX = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(PayRollProviderContract.CONTENT_URI, TABLE_NAME);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "vnd." + PACKAGE + "." + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "vnd." + PACKAGE + "." + TABLE_NAME;

        public static final String[] PROJECTION_ALL = {"Department._id", NAME, NO_OF_EMPLOYEES};
    }
}
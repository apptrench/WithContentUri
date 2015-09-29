package com.ganesh.learn.android.withcontenturi;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ganesh.learn.android.withcontenturi.db.PayRollProviderContract.Employee;

/**
 * Created by Ganesh.
 */
public class EmpCursorAdapter extends CursorAdapter {
    public EmpCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.emp_row, parent, false);
        view.setTag(R.id.name, view.findViewById(R.id.name));
        view.setTag(R.id.empId, view.findViewById(R.id.empId));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = (TextView) view.getTag(R.id.name);
        TextView idView = (TextView) view.getTag(R.id.empId);

        nameView.setText(cursor.getString(Employee.FIRST_NAME_COLUMN_INDEX)+" "
                +cursor.getString(Employee.LAST_NAME_COLUMN_INDEX));
        idView.setText(cursor.getString(Employee.EMP_ID_COLUMN_INDEX));
    }
}

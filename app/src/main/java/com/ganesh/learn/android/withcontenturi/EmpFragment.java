package com.ganesh.learn.android.withcontenturi;


import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.ganesh.learn.android.withcontenturi.db.Columns;
import com.ganesh.learn.android.withcontenturi.db.PayRollProviderContract.Employee;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

public class EmpFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewEmpDialogFragment.OnNewEmpAdded{
    private static final int EMP_LOADER = 0;
    private static final String TAG = "EmpFragment";
    private CursorAdapter adapter;
    DynamicListView listView;

    public EmpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emp, container, false);
        // Inflate the layout for this fragment
        listView = (DynamicListView) view.findViewById(R.id.empList);
        adapter = new EmpCursorAdapter(getActivity(), null);
        listView.setAdapter(adapter);

        listView.enableSwipeToDismiss(new OnDismissCallback() {
            @Override
            public void onDismiss(ViewGroup viewGroup, int[] ints) {
                long id = adapter.getItemId(ints[0]);
                getActivity().getContentResolver().delete(ContentUris.withAppendedId(Employee.CONTENT_URI, id), null, null);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_emp, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addEmp:
                openDialogForNewEmp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialogForNewEmp() {
        new NewEmpDialogFragment().show(getChildFragmentManager(),"newEmp");
    }


    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(EMP_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == EMP_LOADER) {
            return new CursorLoader(getActivity(),
                    Employee.CONTENT_URI,
                    Employee.PROJECTION_ALL,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "onLoadFinished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onNewEmpAdded(String firstName, String lastName, String empId, int deptId) {
        ContentValues values = new ContentValues();
        values.put(Columns.FIRST_NAME, firstName);
        values.put(Columns.LAST_NAME, lastName);
        values.put(Columns.EMP_ID, empId);
        values.put(Columns.DEPT_ID, deptId);

        getActivity().getContentResolver().insert(Employee.CONTENT_URI, values);
    }
}

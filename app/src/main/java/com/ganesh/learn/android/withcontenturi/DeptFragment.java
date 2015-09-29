package com.ganesh.learn.android.withcontenturi;


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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ganesh.learn.android.withcontenturi.db.Columns;
import com.ganesh.learn.android.withcontenturi.db.PayRollProviderContract;

public class DeptFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewDeptDialogFragment.OnNewDeptAdded {
    private static final int DEPT_LOADER = 0;
    private static final String TAG = DeptFragment.class.getName();
    private CursorAdapter adapter;
    ListView listView;

    public DeptFragment() {
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
        View view = inflater.inflate(R.layout.fragment_dept, container, false);
        // Inflate the layout for this fragment
        listView = (ListView) view.findViewById(R.id.deptList);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.dept_row, null,
                new String[]{Columns.NAME}, new int[]{R.id.name}, 0);
        listView.setAdapter(adapter);

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
                openDialogForNewDept();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialogForNewDept() {
        new NewDeptDialogFragment().show(getChildFragmentManager(), "newEmp");
    }


    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(DEPT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == DEPT_LOADER) {
            return new CursorLoader(getActivity(),
                    PayRollProviderContract.Department.CONTENT_URI,
                    PayRollProviderContract.Department.PROJECTION_ALL,
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
    public void onNewDeptAdded(String name) {
        ContentValues values = new ContentValues();
        values.put(Columns.NAME, name);
        getActivity().getContentResolver().insert(PayRollProviderContract.Department.CONTENT_URI, values);
    }
}

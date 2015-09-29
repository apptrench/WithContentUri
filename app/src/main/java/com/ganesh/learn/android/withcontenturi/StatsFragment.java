package com.ganesh.learn.android.withcontenturi;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ganesh.learn.android.withcontenturi.db.Columns;
import com.ganesh.learn.android.withcontenturi.db.PayRollProviderContract.DepartmentStats;

public class StatsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DEPT_LOADER = 0;
    private static final String TAG = StatsFragment.class.getName();
    private CursorAdapter adapter;
    ListView listView;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dept, container, false);
        // Inflate the layout for this fragment
        listView = (ListView) view.findViewById(R.id.deptList);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.stats_row, null,
                new String[]{Columns.NAME,"noOfEmployees"}, new int[]{R.id.name, R.id.noOfEmps}, 0);
        listView.setAdapter(adapter);

        return view;
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
                    DepartmentStats.CONTENT_URI,
                    DepartmentStats.PROJECTION_ALL,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "onLoadFinished " + data.getCount());
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}

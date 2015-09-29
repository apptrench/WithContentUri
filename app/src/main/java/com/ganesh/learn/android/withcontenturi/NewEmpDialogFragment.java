package com.ganesh.learn.android.withcontenturi;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.ganesh.learn.android.withcontenturi.db.Columns;
import com.ganesh.learn.android.withcontenturi.db.PayRollProviderContract;

public class NewEmpDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int DEPT_LOADER = 0;
    private static final String TAG = "NewEmpDialgFragment";
    private OnNewEmpAdded mListener;
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private int selectedDept;

    public NewEmpDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_emp_dialog, container, false);

        final EditText firstNameTextView = ((EditText) view.findViewById(R.id.firstName));
        final EditText lastNameTextView = ((EditText) view.findViewById(R.id.lastName));
        final EditText empIdTextView = ((EditText) view.findViewById(R.id.empId));

        spinner = (Spinner) view.findViewById(R.id.dept);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)spinner.getSelectedItem();
                selectedDept = cursor.getInt(PayRollProviderContract.Department._ID_COLUMN_INDEX);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDept = 0;
            }
        });
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, null, new String[]{Columns.ID},
                new int[]{android.R.id.text1}, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        setSize(view);
        setTitle();

        setActionListeners(view, firstNameTextView, lastNameTextView, empIdTextView);
        // Inflate the layout for this fragment
        return view;
    }

    private void setActionListeners(View view, final EditText firstNameTextView, final EditText lastNameTextView,
                                    final EditText empIdTextView) {
        Button button = (Button) view.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameTextView.getText().toString();
                String lastName = lastNameTextView.getText().toString();
                String empId = empIdTextView.getText().toString();

                onButtonPressed(firstName, lastName, empId);
                NewEmpDialogFragment.this.dismiss();
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewEmpDialogFragment.this.dismiss();
            }
        });
    }

    private void setTitle() {
        this.getDialog().setTitle(getResources().getString(R.string.newEmp));
    }

    private void setSize(View view) {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        view.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
    }

    public void onButtonPressed(String firstName, String lastName, String empId) {
        if (mListener != null) {
            mListener.onNewEmpAdded(firstName, lastName, empId, selectedDept);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNewEmpAdded) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public interface OnNewEmpAdded {
        void onNewEmpAdded(String firstName, String lastName, String empId, int deptId);
    }
}

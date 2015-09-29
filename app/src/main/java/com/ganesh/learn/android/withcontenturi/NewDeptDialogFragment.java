package com.ganesh.learn.android.withcontenturi;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class NewDeptDialogFragment extends DialogFragment {

    private OnNewDeptAdded mListener;

    public NewDeptDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_dept_dialog, container, false);

        final EditText nameTextView = ((EditText) view.findViewById(R.id.name));

        setSize(view);
        setTitle();

        setActionListeners(view, nameTextView);
        // Inflate the layout for this fragment
        return view;
    }

    private void setActionListeners(View view, final EditText nameTextView) {
        Button button = (Button) view.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTextView.getText().toString();
                onButtonPressed(name);
                NewDeptDialogFragment.this.dismiss();
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewDeptDialogFragment.this.dismiss();
            }
        });
    }

    private void setTitle() {
        this.getDialog().setTitle(getResources().getString(R.string.newDept));
    }

    private void setSize(View view) {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        view.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
    }

    public void onButtonPressed(String firstName) {
        if (mListener != null) {
            mListener.onNewDeptAdded(firstName);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNewDeptAdded) getParentFragment();
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

    public interface OnNewDeptAdded {
        void onNewDeptAdded(String name);
    }
}

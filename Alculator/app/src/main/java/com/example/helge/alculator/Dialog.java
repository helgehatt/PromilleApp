package com.example.helge.alculator;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Dialog extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);

        Button okButton = (Button) view.findViewById(R.id.dialogButtonOK);
        Button editButton = (Button) view.findViewById(R.id.dialogButtonEdit);

        okButton.setOnClickListener(this);
        editButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialogButtonOK:
                dismiss();
                break;
            case R.id.dialogButtonEdit:
                break;
        }
    }


}

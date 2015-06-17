package com.example.helge.alculator;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Dialog extends DialogFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialogButtonOK:
                dismiss();
                break;
            case R.id.dialogButtonEdit:
                dismiss();
                break;
        }
    }


}

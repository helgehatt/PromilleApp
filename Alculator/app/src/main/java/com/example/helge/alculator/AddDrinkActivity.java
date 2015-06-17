package com.example.helge.alculator;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AddDrinkActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_drink);

        Button okButton = (Button) findViewById(R.id.button_ok);
        Button cancelButton = (Button) findViewById(R.id.button_cancel);
        Button deleteButton = (Button) findViewById(R.id.button_delete);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_ok:
                break;
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_delete:
                break;
        }
    }
}

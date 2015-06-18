package com.example.helge.alculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

public class MainFragment extends Fragment implements OnClickListener {

    private GridView mGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button addButton = (Button) view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        Button remButton = (Button) view.findViewById(R.id.button_rem);
        remButton.setOnClickListener(this);

        mGrid = (GridView) view.findViewById(R.id.gridView);
        mGrid.setAdapter(new GridAdapter(getActivity().getApplicationContext()));


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_add:
                Dialog dialog = new Dialog();
                dialog.setTargetFragment(this, 1);
                dialog.show(getFragmentManager(), "Dialog");
                break;
            case R.id.button_rem:
                startActivity(new Intent(getActivity().getApplicationContext(), AddDrinkActivity.class));
                break;
        }
    }
}

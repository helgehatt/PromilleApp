package com.example.helge.alculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MainFragment extends Fragment implements OnClickListener {

    private GridView mGrid;
    private GridAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button addButton = (Button) view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        Button remButton = (Button) view.findViewById(R.id.button_rem);
        remButton.setOnClickListener(this);

        mGrid = (GridView) view.findViewById(R.id.gridView);
        mAdapter = new GridAdapter(getActivity().getApplicationContext());
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Click: Position: " + position, Toast.LENGTH_LONG).show();
                mAdapter.getItem(position);
            }
        });
        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Long click: Position: " + position, Toast.LENGTH_LONG).show();
                return true;
            }
        });

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

package com.example.helge.alculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MainFragment extends Fragment {
    private final String TAG = "Alculator";

    private GridView mGrid;
    private GridAdapter mAdapter;
    private HistoryFragment mHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mHistory = getHistory();

        Button addButton = (Button) view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Drink drink = mAdapter.getSelectedItem();
                if (drink != null){
                    drink.incQuantity();
                    Log.i(TAG, "Increment drink");
                    mAdapter.notifyDataSetChanged();
                    mGrid.invalidateViews();
                    mHistory.addDrink(drink.getVolume(), drink.getAlcoholPercent(), drink.getCalories(), getActivity());
                }
            }
        });

        Button remButton = (Button) view.findViewById(R.id.button_rem);
        remButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Drink drink = mAdapter.getSelectedItem();
                if (drink != null) {
                    if (drink.getQuantity() > 0) {
                        drink.decQuantity();
                        Log.i(TAG, "Decrement drink");
                        mAdapter.notifyDataSetChanged();
                        mGrid.invalidateViews();
                        mHistory.remDrink(drink.getVolume(), drink.getAlcoholPercent(), drink.getCalories(), getActivity());
                    }
                }
            }
        });

        mGrid = (GridView) view.findViewById(R.id.gridView);
        mAdapter = new GridAdapter(getActivity().getApplicationContext());
        mGrid.setAdapter(mAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Click: Position: " + position, Toast.LENGTH_LONG).show();
                if (mAdapter.setSelected(position))
                    startActivity(new Intent(getActivity().getApplicationContext(), AddDrinkActivity.class));
            }
        });
        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Long click: Position: " + position, Toast.LENGTH_LONG).show();
                Dialog dialog = new Dialog();
                dialog.setTargetFragment(MainFragment.this, 1);
                dialog.show(getFragmentManager(), "Dialog");
                return true;
            }
        });

        Log.i(TAG, "CreateView()");

        return view;
    }

    private HistoryFragment getHistory() {
        String tagName = "android:switcher:" + R.id.pager + ":" + 0;
        HistoryFragment history = (HistoryFragment) getActivity().getSupportFragmentManager().findFragmentByTag(tagName);
        if (null == history)
            return new HistoryFragment();
        return history;
    }
}

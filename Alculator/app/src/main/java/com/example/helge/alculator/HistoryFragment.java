package com.example.helge.alculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

public class HistoryFragment extends Fragment {

    private TextView cVolumeView, cQuantityView, cAlcoholView, cCaloriesView;
    private TextView tVolumeView, tQuantityView, tAlcoholView, tCaloriesView;

    private static final DecimalFormat df = new DecimalFormat("######.#");
    private static final DecimalFormat pf = new DecimalFormat("#####0.0#");

    private SharedPreferences cPrefs, tPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        cVolumeView = (TextView) view.findViewById(R.id.cVolume);
        tVolumeView = (TextView) view.findViewById(R.id.tVolume);

        cQuantityView = (TextView) view.findViewById(R.id.cQuantity);
        tQuantityView = (TextView) view.findViewById(R.id.tQuantity);

        cAlcoholView = (TextView) view.findViewById(R.id.cAlcohol);
        tAlcoholView = (TextView) view.findViewById(R.id.tAlcohol);

        cCaloriesView = (TextView) view.findViewById(R.id.cCalories);
        tCaloriesView = (TextView) view.findViewById(R.id.tCalories);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateLabels();
    }

    public void updateLabels() {
        if (null == cPrefs)
            cPrefs = getActivity().getSharedPreferences("current", Context.MODE_PRIVATE);

        if (null == tPrefs)
            tPrefs = getActivity().getSharedPreferences("total", Context.MODE_PRIVATE);

        cVolumeView.setText(pf.format(getDouble(cPrefs, "cVolume", 0)));
        tVolumeView.setText(pf.format(getDouble(tPrefs, "tVolume", 0)));

        cQuantityView.setText("" + cPrefs.getInt("cQuantity", 0));
        tQuantityView.setText("" + tPrefs.getInt("tQuantity", 0));

        cAlcoholView.setText(df.format(getDouble(cPrefs, "cAlcohol", 0)));
        tAlcoholView.setText(df.format(getDouble(tPrefs, "tAlcohol", 0)));

        cCaloriesView.setText(df.format(getDouble(cPrefs, "cCalories", 0)));
        tCaloriesView.setText(df.format(getDouble(tPrefs, "tCalories", 0)));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}

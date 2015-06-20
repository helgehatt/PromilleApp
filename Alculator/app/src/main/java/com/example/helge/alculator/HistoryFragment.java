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

    private static double cVolume, tVolume;
    private static int cQuantity, tQuantity;
    private static double cAlcohol, tAlcohol;
    private static double cCalories, tCalories;

    private TextView cVolumeView, cQuantityView, cAlcoholView, cCaloriesView;
    private TextView tVolumeView, tQuantityView, tAlcoholView, tCaloriesView;

    private static final DecimalFormat df = new DecimalFormat("######.#");
    private static final DecimalFormat pf = new DecimalFormat("#####0.0#");

    private SharedPreferences cPrefs, tPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        getViews(view);

        updateLabels();

        return view;
    }

    public void getViews(View view) {

        cVolumeView = (TextView) view.findViewById(R.id.cVolume);
        tVolumeView = (TextView) view.findViewById(R.id.tVolume);

        cQuantityView = (TextView) view.findViewById(R.id.cQuantity);
        tQuantityView = (TextView) view.findViewById(R.id.tQuantity);

        cAlcoholView = (TextView) view.findViewById(R.id.cAlcohol);
        tAlcoholView = (TextView) view.findViewById(R.id.tAlcohol);

        cCaloriesView = (TextView) view.findViewById(R.id.cCalories);
        tCaloriesView = (TextView) view.findViewById(R.id.tCalories);
    }

    public void getPrefs() {
        if (null == cPrefs)
            cPrefs = getActivity().getSharedPreferences("current", Context.MODE_PRIVATE);

        if (null == tPrefs)
            tPrefs = getActivity().getSharedPreferences("total", Context.MODE_PRIVATE);

        cVolume = getDouble(cPrefs, "cVolume", 0);
        tVolume = getDouble(tPrefs, "tVolume", 0);

        cQuantity = cPrefs.getInt("cQuantity", 0);
        tQuantity = tPrefs.getInt("tQuantity", 0);

        cAlcohol = getDouble(cPrefs, "cAlcohol", 0);
        tAlcohol = getDouble(tPrefs, "tAlcohol", 0);

        cCalories = getDouble(cPrefs, "cCalories", 0);
        tCalories = getDouble(tPrefs, "tCalories", 0);
    }

    public void resetAllPrefs(Context context) {
        if (null == cPrefs)
            cPrefs = context.getSharedPreferences("current", Context.MODE_PRIVATE);

        if (null == tPrefs)
            tPrefs = context.getSharedPreferences("total", Context.MODE_PRIVATE);

        cPrefs.edit().clear().apply();
        tPrefs.edit().clear().apply();
    }

    public void resetCurrentPrefs(Context context) {
        if (null == cPrefs)
            cPrefs = context.getSharedPreferences("current", Context.MODE_PRIVATE);

        cPrefs.edit().clear().apply();
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public void updateLabels() {
        getPrefs();

        cVolumeView.setText(pf.format(cVolume));
        tVolumeView.setText(pf.format(tVolume));

        cQuantityView.setText("" + cQuantity);
        tQuantityView.setText("" + tQuantity);

        cAlcoholView.setText(df.format(cAlcohol));
        tAlcoholView.setText(df.format(tAlcohol));

        cCaloriesView.setText(df.format(cCalories));
        tCaloriesView.setText(df.format(tCalories));
    }
}

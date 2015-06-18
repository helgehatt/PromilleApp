package com.example.helge.alculator;

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

    private static final DecimalFormat df = new DecimalFormat("#####.#");

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

        updateLabels();
        return view;
    }

    public void addHistory(double volume, double alcohol) {

        cVolume += volume / 100;
        tVolume += volume / 100;

        cQuantity ++;
        tQuantity ++;

        cAlcohol += volume * alcohol / 10;
        tAlcohol += volume * alcohol / 10;
    }

    public void addHistory(double volume, double alcohol, double calories) {

        addHistory(volume, alcohol);

        cCalories += calories;
        tCalories += calories;
    }

    public void remHistory(double volume, double alcohol) {

        cVolume -= volume / 100;
        tVolume -= volume / 100;

        cQuantity --;
        tQuantity --;

        cAlcohol -= volume * alcohol / 10;
        tAlcohol -= volume * alcohol / 10;
    }

    public void remHistory(double volume, double alcohol, double calories) {

        remHistory(volume, alcohol);

        cCalories -= calories;
        tCalories -= calories;
    }

    public boolean updateLabels() {

        cVolumeView.setText("" + cVolume);
        tVolumeView.setText("" + tVolume);

        cQuantityView.setText("" + cQuantity);
        tQuantityView.setText("" + tQuantity);

        cAlcoholView.setText(df.format(cAlcohol));
        tAlcoholView.setText(df.format(tAlcohol));

        cCaloriesView.setText(df.format(cCalories));
        tCaloriesView.setText(df.format(tCalories));

        return true;
    }
}

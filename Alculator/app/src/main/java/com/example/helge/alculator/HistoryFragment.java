package com.example.helge.alculator;

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

    private MainFragment mMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mMain = getMain();

        getViews(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLabels();
    }

    public void updateLabels() {

        cVolumeView.setText(pf.format(mMain.getCurrentVolume()));
        tVolumeView.setText(pf.format(mMain.getTotalVolume()));

        cQuantityView.setText("" + mMain.getCurrentQuantity());
        tQuantityView.setText("" + mMain.getTotalQuantity());

        cAlcoholView.setText(df.format(mMain.getCurrentAlcohol()));
        tAlcoholView.setText(df.format(mMain.getTotalAlcohol()));

        cCaloriesView.setText(df.format(mMain.getCurrentCalories()));
        tCaloriesView.setText(df.format(mMain.getTotalCalories()));
    }

    private void getViews(View view) {

        cVolumeView = (TextView) view.findViewById(R.id.cVolume);
        tVolumeView = (TextView) view.findViewById(R.id.tVolume);

        cQuantityView = (TextView) view.findViewById(R.id.cQuantity);
        tQuantityView = (TextView) view.findViewById(R.id.tQuantity);

        cAlcoholView = (TextView) view.findViewById(R.id.cAlcohol);
        tAlcoholView = (TextView) view.findViewById(R.id.tAlcohol);

        cCaloriesView = (TextView) view.findViewById(R.id.cCalories);
        tCaloriesView = (TextView) view.findViewById(R.id.tCalories);
    }

    private MainFragment getMain() {
        return (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 2);
    }
}

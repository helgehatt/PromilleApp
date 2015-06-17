package com.example.helge.alculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private MetricImperial metricImperial;
    private Gender gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RadioButton metricButton = (RadioButton) view.findViewById(R.id.radioButton_metric);
        metricButton.setOnClickListener(this);
        RadioButton imperialButton = (RadioButton) view.findViewById(R.id.radioButton_imperial);
        imperialButton.setOnClickListener(this);

        RadioButton maleButton = (RadioButton) view.findViewById(R.id.radioButton_male);
        maleButton.setOnClickListener(this);
        RadioButton femaleButton = (RadioButton) view.findViewById(R.id.radioButton_female);
        femaleButton.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {

        TextView kgLbsView = (TextView) getView().findViewById(R.id.textView_kg_lbs);

        switch (v.getId()) {
            case R.id.radioButton_metric:
                kgLbsView.setText(R.string.kg);
                metricImperial = MetricImperial.METRIC;
                break;
            case R.id.radioButton_imperial:
                kgLbsView.setText(R.string.lbs);
                metricImperial = MetricImperial.IMPERIAL;
                break;

            case R.id.radioButton_male:
                gender = Gender.MALE;
                break;
            case R.id.radioButton_female:
                gender = Gender.FEMALE;
                break;
        }
    }
}

enum MetricImperial {
    METRIC, IMPERIAL
}

enum Gender {
    MALE, FEMALE
}

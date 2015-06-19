package com.example.helge.alculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private SharedPreferences settings;
    private TextView kgLbsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        kgLbsView = (TextView) view.findViewById(R.id.textView_kg_lbs);

        settings = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        RadioGroup unit = (RadioGroup) view.findViewById(R.id.radioGroup_unit);
        unit.setOnCheckedChangeListener(this);
        String metric = getResources().getString(R.string.metric);
        unit.check(settings.getString("unit", metric).equals(metric) ? R.id.radioButton_metric : R.id.radioButton_imperial);

        RadioGroup gender = (RadioGroup) view.findViewById(R.id.radioGroup_gender);
        gender.setOnCheckedChangeListener(this);
        String male = getResources().getString(R.string.male);
        gender.check(settings.getString("gender", male).equals(male) ? R.id.radioButton_male : R.id.radioButton_female);

        final EditText weightField = (EditText) view.findViewById(R.id.editText_weight);

        weightField.setText(String.valueOf(settings.getInt("weight", 70)));

        weightField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    settings.edit().putInt("weight", Integer.parseInt(weightField.getText().toString())).commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        SharedPreferences.Editor editor = settings.edit();

        switch (checkedId) {
            case R.id.radioButton_metric:
                kgLbsView.setText(R.string.kg);
                editor.putString("unit", getResources().getString(R.string.metric));
                break;
            case R.id.radioButton_imperial:
                kgLbsView.setText(R.string.lbs);
                editor.putString("unit", getResources().getString(R.string.imperial));
                break;

            case R.id.radioButton_male:
                editor.putString("gender", getResources().getString(R.string.male));
                break;
            case R.id.radioButton_female:
                editor.putString("gender", getResources().getString(R.string.female));
                break;
        }

        editor.commit();

    }
}
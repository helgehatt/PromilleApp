package com.example.helge.alculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        kgLbsView = (TextView) view.findViewById(R.id.textView_kg_lbs);

        settings = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        RadioGroup unit = (RadioGroup) view.findViewById(R.id.radioGroup_unit);
        unit.setOnCheckedChangeListener(this);
        String metric = getResources().getString(R.string.settings_metric);
        unit.check(settings.getString("unit", metric).equals(metric) ? R.id.radioButton_metric : R.id.radioButton_imperial);

        RadioGroup gender = (RadioGroup) view.findViewById(R.id.radioGroup_gender);
        gender.setOnCheckedChangeListener(this);
        String male = getResources().getString(R.string.settings_male);
        gender.check(settings.getString("gender", male).equals(male) ? R.id.radioButton_male : R.id.radioButton_female);

        final EditText weightField = (EditText) view.findViewById(R.id.editText_weight);

        weightField.setText(String.valueOf(settings.getInt("weight", 70)));
        weightField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (weightField.getText().length() != 0) {
                    settings.edit().putInt("weight", Integer.parseInt(weightField.getText().toString())).commit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        SharedPreferences.Editor editor = settings.edit();

        switch (checkedId) {
            case R.id.radioButton_metric:
                kgLbsView.setText(R.string.settings_kg);
                editor.putString("unit", getResources().getString(R.string.settings_metric));
                break;
            case R.id.radioButton_imperial:
                kgLbsView.setText(R.string.settings_lbs);
                editor.putString("unit", getResources().getString(R.string.settings_imperial));
                break;

            case R.id.radioButton_male:
                editor.putString("gender", getResources().getString(R.string.settings_male));
                break;
            case R.id.radioButton_female:
                editor.putString("gender", getResources().getString(R.string.settings_female));
                break;
        }

        editor.commit();

    }
}
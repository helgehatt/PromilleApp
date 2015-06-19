package com.example.helge.alculator;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;


public class Dialog extends DialogFragment implements View.OnClickListener {

    private String mName;
    private int mImageID;
    private double mAlcohol, mVolume, mCalories;
    private TextView nameView, alcoholView, volumeView, caloriesView;
    private ImageView imageView;

    private static final DecimalFormat df = new DecimalFormat("#####.#");

    static Dialog newInstance(String name, double alcohol, double volume, double calories, int imageID) {
        Dialog dialog = new Dialog();

        Bundle args = new Bundle();
        args.putString("mName", name);
        args.putDouble("mAlcohol", alcohol);
        args.putDouble("mVolume", volume);
        args.putDouble("mCalories",calories);
        args.putInt("mImageID", imageID);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);

        mName = getArguments().getString("mName");
        mAlcohol = getArguments().getDouble("mAlcohol");
        mVolume = getArguments().getDouble("mVolume");
        mCalories = getArguments().getDouble("mCalories");
        mImageID = getArguments().getInt("mImageID");

        nameView = (TextView) view.findViewById(R.id.dialog_name);
        alcoholView = (TextView) view.findViewById(R.id.dialog_alcoholPercentage);
        volumeView = (TextView) view.findViewById(R.id.dialog_volume);
        caloriesView = (TextView) view.findViewById(R.id.dialog_calories);
        imageView = (ImageView) view.findViewById(R.id.dialog_image);

        nameView.setText(mName);
        alcoholView.setText(df.format(mAlcohol) + " %");
        volumeView.setText(df.format(mVolume) + " cl");
        caloriesView.setText(df.format(mCalories) + " kcal");
        imageView.setImageResource(mImageID);

        Button okButton = (Button) view.findViewById(R.id.dialog_button_ok);
        Button editButton = (Button) view.findViewById(R.id.dialog_button_edit);

        okButton.setOnClickListener(this);
        editButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_button_ok:
                dismiss();
                break;
            case R.id.dialog_button_edit:
                break;
        }
    }
}

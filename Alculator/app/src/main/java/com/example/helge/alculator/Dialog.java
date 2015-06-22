package com.example.helge.alculator;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;


public class Dialog extends DialogFragment {

    private String mName;
    private Bitmap mImage;
    private double mAlcohol, mVolume, mCalories;
    private TextView nameView, alcoholView, volumeView, caloriesView;
    private ImageView imageView;

    private static final DecimalFormat df = new DecimalFormat("#####.#");

    static Dialog newInstance(String name, double alcohol, double volume, double calories, Bitmap image) {
        Dialog dialog = new Dialog();

        Bundle args = new Bundle();
        args.putString("mName", name);
        args.putDouble("mAlcohol", alcohol);
        args.putDouble("mVolume", volume);
        args.putDouble("mCalories", calories);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        args.putByteArray("mImage", byteArray);

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
        byte[] bytes = getArguments().getByteArray("mImage");
        mImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        nameView = (TextView) view.findViewById(R.id.dialog_name);
        alcoholView = (TextView) view.findViewById(R.id.dialog_alcoholPercentage);
        volumeView = (TextView) view.findViewById(R.id.dialog_volume);
        caloriesView = (TextView) view.findViewById(R.id.dialog_calories);
        imageView = (ImageView) view.findViewById(R.id.dialog_image);

        nameView.setText(mName);
        alcoholView.setText(df.format(mAlcohol) + " %");
        volumeView.setText(df.format(mVolume) + " cl");
        caloriesView.setText(df.format(mCalories) + " kcal");
        imageView.setImageBitmap(mImage);

        Button okButton = (Button) view.findViewById(R.id.dialog_button_ok);
        Button deleteButton = (Button) view.findViewById(R.id.dialog_button_delete);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) Dialog.this.getTargetFragment()).onDialogDeletePressed();
                dismiss();
            }
        });

        return view;
    }
}

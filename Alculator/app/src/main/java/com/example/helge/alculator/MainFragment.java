package com.example.helge.alculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainFragment extends Fragment {
    private final String TAG = "Alculator";

    private GridView mGrid;
    private GridAdapter mAdapter;
    private TextView mPermilleView, mSoberInView;
    private long mDrinkingStart;

    private static double cVolume, tVolume;
    private static int cQuantity, tQuantity;
    private static double cAlcohol, tAlcohol;
    private static double cCalories, tCalories;

    private SharedPreferences cPrefs, tPrefs, sPrefs;

    private static final DecimalFormat df = new DecimalFormat("00");
    private static final DecimalFormat pf = new DecimalFormat("#0.00");
    private static final DecimalFormat sf = new DecimalFormat("##");

    static final int ADD_DRINK_REQUEST = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mDrinkingStart = System.currentTimeMillis();

        mPermilleView = (TextView) view.findViewById(R.id.permille);
        mSoberInView = (TextView) view.findViewById(R.id.soberIn);

        sPrefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        getPrefs();

        updateLabels();

        Button addButton = (Button) view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Drink drink = mAdapter.getSelectedItem();
                if (drink != null) {
                    drink.incQuantity();
                    Log.i(TAG, "Increment drink");
                    mAdapter.notifyDataSetChanged();
                    mGrid.invalidateViews();
                    addDrink(drink.getVolume(), drink.getAlcoholPercent(), drink.getCalories());
                    double mCurrentScore = calculatePermille();
                    updateLabels();
                    makeImpairmentsToast(mCurrentScore);
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
                        remDrink(drink.getVolume(), drink.getAlcoholPercent(), drink.getCalories());
                        calculatePermille();
                        updateLabels();
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
                if (!mAdapter.setSelected(position)) {
                    Intent addDrinkIntent = new Intent(getActivity().getApplicationContext(), AddDrinkActivity.class);
                    startActivityForResult(addDrinkIntent, ADD_DRINK_REQUEST);
                }
            }
        });
        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "Long click: Position: " + position, Toast.LENGTH_LONG).show();
                Drink drink = mAdapter.getSelectedItem();
                Dialog.newInstance(drink.getName(), drink.getAlcoholPercent(), drink.getVolume(), drink.getCalories(), R.drawable.drink_black_box)
                        .show(getFragmentManager(), "Dialog");
                return true;
            }
        });

        Log.i(TAG, "CreateView()");

        return view;
    }

    private double calculatePermille() {

        String mGender = sPrefs.getString("gender", "Male");
        double mBloodWater = 0.806; // Constant for body water in the blood.
        double mNumStdDrinks = getDouble(cPrefs, "cAlcohol", 0) * 7.89 / 10; // Number of drinks containing 10 grams of ethanol.
        double mConversion = 1.2; // Conversion standard set by The Swedish National Institute of Public Health.
        double mBodyWater = mGender.equals("Male") ? 0.58 : 0.49; // Body water constant.
        int mBodyWeight = sPrefs.getInt("weight", 0); // Body weight.
        double mMetabolism = mGender.equals("Male") ? 0.015 : 0.017; // Metabolism constant.
        long mTimeSinceStart = (System.currentTimeMillis() - mDrinkingStart) / 3600000; // Time since drinking start in hours.

        double mCurrentScore = (mBloodWater * mNumStdDrinks * mConversion / mBodyWater / mBodyWeight - mMetabolism * mTimeSinceStart) * 10;
        double mHighScore = getDouble(tPrefs, "mHighScore", 0);

        if (mCurrentScore > mHighScore) {
            mHighScore = mCurrentScore;

            SharedPreferences.Editor editor = tPrefs.edit();
            putDouble(editor, "mHighScore", mHighScore);
            editor.apply();
        }

        double mCountScore = (mHighScore - mCurrentScore) / 10 / 0.806 / 1.2 * mBodyWater * mBodyWeight;

        SharedPreferences.Editor editor = cPrefs.edit();
        putDouble(editor, "mCurrentScore", mCurrentScore);
        putDouble(editor, "mCountScore", mCountScore);
        editor.apply();

        return mCurrentScore;
    }

    private void addDrink(double volume, double alcohol, double calories) {

        cVolume += volume / 100;
        tVolume += volume / 100;

        cQuantity ++;
        tQuantity ++;

        cAlcohol += volume * alcohol / 100;
        tAlcohol += volume * alcohol / 100;

        cCalories += calories;
        tCalories += calories;

        setPrefs();
    }

    private void remDrink(double volume, double alcohol, double calories) {

        cVolume -= volume / 100;
        tVolume -= volume / 100;

        cQuantity --;
        tQuantity --;

        cAlcohol -= volume * alcohol / 100;
        tAlcohol -= volume * alcohol / 100;

        cCalories -= calories;
        tCalories -= calories;

        setPrefs();
    }

    private void updateLabels() {

        double mCurrentScore = getDouble(cPrefs, "mCurrentScore", 0);
        mPermilleView.setText("  " + pf.format(mCurrentScore) + " ‰");

        double mMetabolism = (sPrefs.getString("gender", "Male").equals("Male") ? 0.015 : 0.017) * 10;
        double n = mCurrentScore / mMetabolism;
        mSoberInView.setText("Sober in " + sf.format(n % 100 - n % 1) + " h " + sf.format(n % 1 * 60) + " m");
    }

    private void getPrefs() {
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

    private void setPrefs() {

        SharedPreferences.Editor cEditor = cPrefs.edit();
        SharedPreferences.Editor tEditor = tPrefs.edit();

        putDouble(cEditor, "cVolume", cVolume);
        putDouble(tEditor, "tVolume", tVolume);

        cEditor.putInt("cQuantity", cQuantity);
        tEditor.putInt("tQuantity", tQuantity);

        putDouble(cEditor, "cAlcohol", cAlcohol);
        putDouble(tEditor, "tAlcohol", tAlcohol);

        putDouble(cEditor, "cCalories", cCalories);
        putDouble(tEditor,"tCalories", tCalories);

        cEditor.apply();
        tEditor.apply();
    }

    private double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ADD_DRINK_REQUEST == requestCode && Activity.RESULT_OK == resultCode){
            Bundle bundle = data.getExtras();
            String name = bundle.getString(AddDrinkActivity.NAME);
            double alcohol = Double.parseDouble(bundle.getString(AddDrinkActivity.ALCOHOL));
            double volume = Double.parseDouble(bundle.getString(AddDrinkActivity.VOLUME));

            String caloriesString = bundle.getString(AddDrinkActivity.CALORIES);
            double calories = 0;
            if (!caloriesString.isEmpty()) {
                calories = Double.parseDouble(bundle.getString(AddDrinkActivity.CALORIES));
            }

            //Uncompress image.
            byte[] bytes = bundle.getByteArray(AddDrinkActivity.IMAGE);
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            Drink newDrink = new Drink(name, alcohol, volume, calories, image);
            mAdapter.addToDatabase(name, alcohol, volume, calories, 1); // TODO add image resource to device and save resource id to database
            mAdapter.add(newDrink);
            mAdapter.notifyDataSetChanged();
            mGrid.invalidateViews();
        } else if (resultCode == Activity.RESULT_CANCELED && requestCode == ADD_DRINK_REQUEST) {
            Toast.makeText(getActivity().getApplicationContext(), "No drink added", Toast.LENGTH_LONG).show();
        }
    }

    private void makeImpairmentsToast(double mCurrentScore) {
        String toastText;
        if (mCurrentScore > 5) {
            toastText = getResources().getString(R.string.permille_above_5);
        } else if (mCurrentScore > 3) {
            toastText = getResources().getString(R.string.permille_above_3);
        } else if (mCurrentScore > 2) {
            toastText = getResources().getString(R.string.permille_above_2);
        } else if (mCurrentScore > 1) {
            toastText = getResources().getString(R.string.permille_above_1);
        } else if (mCurrentScore > 0.5) {
            toastText = getResources().getString(R.string.permille_above_05);
        } else {
            toastText = getResources().getString(R.string.permille_below_05);
        }
        Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_LONG).show();

        //TODO create proper strings
    }
}

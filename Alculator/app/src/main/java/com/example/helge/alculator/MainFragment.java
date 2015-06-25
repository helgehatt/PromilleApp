package com.example.helge.alculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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

    private int     cQuantity, tQuantity;
    private double  cVolume, tVolume,
                    cAlcohol, tAlcohol,
                    cCalories, tCalories,
                    mCurrentScore, mHighScore, mCountScore,
                    mMetabolism, mBodyWater, mBodyWeight;
    private boolean hasStartedDrinking;
    private long    mStartTime;
    private String  soberIn, soberInH, soberInM;

    private GridView mGrid;
    private GridAdapter mAdapter;
    private TextView mPermilleView, mSoberInView;
    private SharedPreferences cPrefs, tPrefs, sPrefs;
    private GraphFragment mGraph;
    private final Handler mHandler = new Handler();

    private static final DecimalFormat pf = new DecimalFormat("#0.00");
    private static final DecimalFormat sf = new DecimalFormat("###");

    private static final int NEW_DRINK_REQUEST = 1;
    private static final long ONE_MINUTE = 1000 * 60;
    private static final double ONE_HOUR = 1000 * 60 * 60;

    public double getCurrentVolume() {
        return cVolume;
    }

    public double getTotalVolume() {
        return tVolume;
    }

    public int getCurrentQuantity() {
        return cQuantity;
    }

    public int getTotalQuantity() {
        return tQuantity;
    }

    public double getCurrentAlcohol() {
        return cAlcohol;
    }

    public double getTotalAlcohol() {
        return tAlcohol;
    }

    public double getCurrentCalories() {
        return cCalories;
    }

    public double getTotalCalories() {
        return tCalories;
    }

    public double getCurrentScore() {
        return mCurrentScore;
    }

    public double getHighScore() {
        return mHighScore;
    }

    public double getCountScore() {
        return mCountScore;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGraph = getGraph();

        mPermilleView = (TextView) view.findViewById(R.id.permille);
        mSoberInView = (TextView) view.findViewById(R.id.soberIn);
        soberIn = getResources().getText(R.string.sober_in) + " ";
        soberInH = " " + getResources().getText(R.string.sober_in_hours) + " ";
        soberInM = " " + getResources().getText(R.string.sober_in_minutes);

        initPrefs();

        Button addButton = (Button) view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Drink drink = mAdapter.getSelectedItem();
                if (drink != null) {
                    drink.incQuantity();
                    mAdapter.sort();
                    Log.i(TAG, "Increment drink");
                    mAdapter.setLastUseAndSort(drink, System.currentTimeMillis());
                    mAdapter.setSelected(0);
                    mAdapter.notifyDataSetChanged();
                    mGrid.invalidateViews();

                    if (!hasStartedDrinking) start();

                    addDrink(drink.getVolume(), drink.getAlcoholPercent(), drink.getCalories());
                    makeImpairmentsToast(mCurrentScore);
                    updateLabels();
                    mGraph.updateLabels();
                    mGraph.updateGraph();
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
                        mAdapter.sort();
                        Log.i(TAG, "Decrement drink");
                        mAdapter.notifyDataSetChanged();
                        mGrid.invalidateViews();
                        remDrink(drink.getVolume(), drink.getAlcoholPercent(), drink.getCalories());
                        calculateScores();

                        if (mCurrentScore == 0) stop();

                        updateLabels();
                        mGraph.updateLabels();
                        mGraph.updateGraph();
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
                if (!mAdapter.setSelected(position)) {
                    Intent addDrinkIntent = new Intent(getActivity().getApplicationContext(), AddDrinkActivity.class);
                    startActivityForResult(addDrinkIntent, NEW_DRINK_REQUEST);
                }
                mGrid.invalidateViews();
            }
        });
        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelected(position);
                Drink drink = mAdapter.getItem(position);
                if (drink != null){
                    Dialog dialog = Dialog.newInstance(drink.getName(), drink.getAlcoholPercent(), drink.getVolume(), drink.getCalories(), drink.getImage());
                    dialog.setTargetFragment(MainFragment.this, 0);
                    dialog.show(getFragmentManager(), getResources().getString(R.string.dialog_title));
                }
                mGrid.invalidateViews();
                return true;
            }
        });

        Log.i(TAG, "CreateView()");

        return view;
    }

    final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            calculateScores();
            mHandler.postDelayed(this, ONE_MINUTE);

            if (mCurrentScore == 0) stop();

            updateLabels();
            mGraph.updateLabels();
        }
    };

    protected void start() {
        resetCurrentPrefs();
        hasStartedDrinking = true;
        mStartTime = System.currentTimeMillis();
        mHandler.postDelayed(mRunnable, ONE_MINUTE);
    }

    protected void stop() {
        hasStartedDrinking = false;
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLabels();
        if (hasStartedDrinking) {
            mHandler.removeCallbacks(mRunnable);
            mHandler.post(mRunnable);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        setPrefs();
    }

    protected void resetCurrentPrefs() {
        cPrefs.edit().clear().apply();
        mGraph.resetGraph();
        initPrefs();
    }

    private void calculateScores() {

        double mTimeSinceStart = (System.currentTimeMillis() - mStartTime) / ONE_HOUR;

        mCurrentScore = (0.806 * cAlcohol * 7.89 / 10 * 1.2 / mBodyWater / mBodyWeight - mMetabolism * mTimeSinceStart) * 10;

        if (mCurrentScore < 0) mCurrentScore = 0;

        if (mCurrentScore > mHighScore) mHighScore = mCurrentScore;

        mCountScore = (mHighScore - mCurrentScore) / 0.806 / 1.2 * mBodyWater * mBodyWeight / 7.89 / 1.5;

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

        calculateScores();
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

        calculateScores();
    }

    protected void updateLabels() {

        mPermilleView.setText("  " + pf.format(mCurrentScore) + " ‰");

        double n = mCurrentScore / mMetabolism / 10;
        mSoberInView.setText(soberIn + sf.format(n % 100 - n % 1) + soberInH + sf.format(n % 1 * 60) + soberInM);
    }

    protected void initPrefs() {
        if (null == cPrefs)
            cPrefs = getActivity().getSharedPreferences("current", Context.MODE_PRIVATE);

        if (null == tPrefs)
            tPrefs = getActivity().getSharedPreferences("total", Context.MODE_PRIVATE);

        if (null == sPrefs)
            sPrefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        cVolume = getDouble(cPrefs, "cVolume", 0);
        tVolume = getDouble(tPrefs, "tVolume", 0);

        cQuantity = cPrefs.getInt("cQuantity", 0);
        tQuantity = tPrefs.getInt("tQuantity", 0);

        cAlcohol = getDouble(cPrefs, "cAlcohol", 0);
        tAlcohol = getDouble(tPrefs, "tAlcohol", 0);

        cCalories = getDouble(cPrefs, "cCalories", 0);
        tCalories = getDouble(tPrefs, "tCalories", 0);

        mCurrentScore = getDouble(cPrefs, "mCurrentScore", 0);
        mHighScore = getDouble(tPrefs, "mHighScore", 0);

        String mGender = sPrefs.getString("gender", "Male");
        mBodyWater = mGender.equals("Male") ? 0.58 : 0.49;
        mMetabolism = mGender.equals("Male") ? 0.015 : 0.017;
        mBodyWeight = (double) sPrefs.getInt("weight", 70);
        if (sPrefs.getString("unit", "Metric").equals("Imperial")) mBodyWeight /= 2.20462;

        mStartTime = cPrefs.getLong("mStartTime", 0);
        hasStartedDrinking = cPrefs.getBoolean("hasStartedDrinking", false);
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

        putDouble(cEditor, "mCurrentScore", mCurrentScore);
        putDouble(tEditor, "mHighScore", mHighScore);

        cEditor.putLong("mStartTime", mStartTime);
        cEditor.putBoolean("hasStartedDrinking", hasStartedDrinking);

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
        if (NEW_DRINK_REQUEST == requestCode && Activity.RESULT_OK == resultCode){
            Bundle bundle = data.getExtras();
            String name = bundle.getString(AddDrinkActivity.NAME);
            double alcohol = Double.parseDouble(bundle.getString(AddDrinkActivity.ALCOHOL));
            int volume = Integer.parseInt(bundle.getString(AddDrinkActivity.VOLUME));
            String imagePath = bundle.getString(AddDrinkActivity.IMAGE_PATH);

            String caloriesString = bundle.getString(AddDrinkActivity.CALORIES);
            int calories = 0;
            if (!caloriesString.isEmpty()) {
                calories = Integer.parseInt(bundle.getString(AddDrinkActivity.CALORIES));
            }

            //Uncompress image.
            byte[] bytes = bundle.getByteArray(AddDrinkActivity.IMAGE);
            Bitmap image;
            if (bytes != null)
                image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            else
                image = ((BitmapDrawable) getResources().getDrawable(R.drawable.drink_empty)).getBitmap();

            long time = System.currentTimeMillis();
            Drink newDrink = new Drink(name, alcohol, volume, calories, image, time);
            mAdapter.addToDatabase(name, alcohol, volume, calories, imagePath, time);
            mAdapter.add(newDrink);
            mAdapter.notifyDataSetChanged();
            mGrid.invalidateViews();
        } else if (resultCode == Activity.RESULT_CANCELED && requestCode == NEW_DRINK_REQUEST) {
            Toast.makeText(getActivity().getApplicationContext(), getResources().getText(R.string.main_no_drink_added), Toast.LENGTH_LONG).show();
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

    private GraphFragment getGraph() {
        return (GraphFragment) getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 1);
    }

    // Deletes the selected item.
    // This method should only be called if the desired item to removeSelectedItem is already selected.
    public void onDialogDeletePressed() {
        mAdapter.removeSelectedItem();
        mAdapter.notifyDataSetChanged();
        mGrid.invalidateViews();
    }

    protected void onSettingsChanged() {

        String mGender = sPrefs.getString("gender", "Male");
        mBodyWater = mGender.equals("Male") ? 0.58 : 0.49;
        mMetabolism = mGender.equals("Male") ? 0.015 : 0.017;
        mBodyWeight = (double) sPrefs.getInt("weight", 70);
        if (sPrefs.getString("unit", "Metric").equals("Imperial")) mBodyWeight /= 2.20462;

        calculateScores();
        updateLabels();
    }

    protected void onResetPressed() {
        tPrefs.edit().clear().apply();
        resetCurrentPrefs();
        stop();
        updateLabels();
    }
}

package com.example.helge.alculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GraphFragment extends Fragment {

    private double mHighScore, mCurrentScore, mCountScore;
    private TextView mHighScoreView, mCurrentScoreView, mCountScoreView;
    private Viewport mViewport;
    private LineGraphSeries<DataPoint> mSeries;
    private ArrayList<DataPoint> mDataPoints;
    private static final DecimalFormat df = new DecimalFormat("00");
    private static final DecimalFormat pf = new DecimalFormat("#0.00");
    private static final DecimalFormat sf = new DecimalFormat("##");

    private SharedPreferences cPrefs, tPrefs, sPrefs;

    private String fileName = "DATA_POINTS";
    private static int DAYS = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        mHighScoreView = (TextView) view.findViewById(R.id.score_high);
        mCurrentScoreView = (TextView) view.findViewById(R.id.score_current);
        mCountScoreView = (TextView) view.findViewById(R.id.score_count);

        GraphView mGraph = (GraphView) view.findViewById(R.id.graphView);
        mViewport = mGraph.getViewport();

        cPrefs = getActivity().getSharedPreferences("current", Context.MODE_PRIVATE);
        tPrefs = getActivity().getSharedPreferences("total", Context.MODE_PRIVATE);
        sPrefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        mDataPoints = readFile();

        mSeries = new LineGraphSeries<>(new DataPoint[] { });
        for (DataPoint dp : mDataPoints)
            mSeries.appendData(dp, true, 100);

        mGraph.addSeries(mSeries);
        mGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    double num = Double.parseDouble(super.formatLabel(value % 24, true));
                    return df.format(num % 100 - num % 1) + ":" + df.format(num % 1 * 60);
                } else {
                    return super.formatLabel(value, false);
                }
            }
        });

        mViewport.setScrollable(true);
        mViewport.setScalable(true);

        updateLabels();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        writeFile();
    }

    public void updateLabels() {
        getPrefs();

        mHighScoreView.setText("" + pf.format(mHighScore) + " ‰");
        mCurrentScoreView.setText("" + pf.format(mCurrentScore) + " ‰");
        mCountScoreView.setText("" + sf.format(Math.ceil(mCountScore)) + " x ");
    }

    protected boolean updateGraph() {
        double mHours = getHours();
        DataPoint mDataPoint = new DataPoint(mHours, mCurrentScore);
        try {
            mSeries.appendData(mDataPoint, true, 200);
            mDataPoints.add(mDataPoint);
            return true;
        } catch (IllegalArgumentException e) {
            DAYS++;
            return false;
        }
    }

    private void getPrefs() {

        double mBodyWater = sPrefs.getString("gender", "Male").equals("Male") ? 0.58 : 0.49;
        double mBodyWeight = (double) sPrefs.getInt("weight", 70);

        mHighScore = getDouble(tPrefs, "mHighScore", 0);
        mCurrentScore = getDouble(cPrefs, "mCurrentScore", 0);
        mCountScore = (mHighScore - mCurrentScore) / 0.806 / 1.2 * mBodyWater * mBodyWeight / 7.89 / 1.5;
    }

    protected void resetGraph() {
        DAYS = 0;
        mSeries.resetData(new DataPoint[]{});
        mDataPoints.clear();
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private double getHours() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 1.0 + Calendar.getInstance().get(Calendar.MINUTE) / 60.0 + 24.0 * DAYS;
    }

    public void writeFile() {
        try {
            FileOutputStream fos = getActivity().getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(mDataPoints);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    public ArrayList<DataPoint> readFile() {
        ArrayList<DataPoint> toReturn = new ArrayList<>();
        try {
            FileInputStream fis = getActivity().getApplicationContext().openFileInput(fileName);
            ObjectInputStream oi = new ObjectInputStream(fis);
            toReturn = (ArrayList<DataPoint>) oi.readObject();
            oi.close();
        } catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        }
        return toReturn;
    }
}

package com.example.helge.alculator;

import android.content.Context;
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

    private TextView mHighScoreView, mCurrentScoreView, mCountScoreView;
    private Viewport mViewport;
    private MainFragment mMain;
    private LineGraphSeries<DataPoint> mSeries;
    private ArrayList<DataPoint> mDataPoints;
    private static final DecimalFormat df = new DecimalFormat("00");
    private static final DecimalFormat pf = new DecimalFormat("#0.00");
    private static final DecimalFormat sf = new DecimalFormat("##");

    private String fileName = "DATA_POINTS";
    private static int DAYS = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        mMain = getMain();

        mHighScoreView = (TextView) view.findViewById(R.id.score_high);
        mCurrentScoreView = (TextView) view.findViewById(R.id.score_current);
        mCountScoreView = (TextView) view.findViewById(R.id.score_count);

        GraphView mGraph = (GraphView) view.findViewById(R.id.graphView);
        mViewport = mGraph.getViewport();

        mDataPoints = readFile();
        mSeries = new LineGraphSeries<>(new DataPoint[] { });

        double mLastX = 0;
        for (DataPoint dp : mDataPoints) {
            mSeries.appendData(dp, true, 100);
            mLastX = dp.getX();
        }

        mGraph.addSeries(mSeries);
        mGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    double num = Double.parseDouble(super.formatLabel(value % 24, true).replace(",", "."));
                    return df.format(num % 100 - num % 1) + ":" + df.format(num % 1 * 60);
                } else {
                    return super.formatLabel(value, false);
                }
            }
        });

        mViewport.setScrollable(true);
        mViewport.setScalable(true);
        mViewport.setXAxisBoundsManual(true);
        mViewport.setMinX(mLastX - 0.75);
        mViewport.setMaxX(mLastX + 0.25);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLabels();
        updateGraph();
    }

    @Override
    public void onPause() {
        super.onPause();
        writeFile();
    }

    protected void updateLabels() {
        mCurrentScoreView.setText("" + pf.format(mMain.getCurrentScore()) + " ‰");
        mHighScoreView.setText("" + pf.format(mMain.getHighScore()) + " ‰");
        mCountScoreView.setText("" + sf.format(Math.ceil(mMain.getCountScore())) + " x ");
    }

    protected boolean updateGraph() {
        double mHours = getHours();
        DataPoint mDataPoint = new DataPoint(mHours, mMain.getCurrentScore());
        try {
            mSeries.appendData(mDataPoint, true, 200);
            mViewport.setMinX(mHours - 0.50);
            mViewport.setMaxX(mHours + 0.25);
            mDataPoints.add(mDataPoint);
            return true;
        } catch (IllegalArgumentException e) {
            DAYS++;
            return false;
        }
    }

    protected void resetGraph() {
        DAYS = 0;
        mSeries.resetData(new DataPoint[]{});
        mDataPoints.clear();
        writeFile();
    }

    private double getHours() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 1.0 + Calendar.getInstance().get(Calendar.MINUTE) / 60.0 + 24.0 * DAYS;
    }

    private void writeFile() {
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

    private ArrayList<DataPoint> readFile() {
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

    private MainFragment getMain() {
        return (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 2);
    }
}

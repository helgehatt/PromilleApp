package com.example.helge.alculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.Calendar;

public class GraphFragment extends Fragment {

    private double mHighScore, mCurrentScore, mCountScore, mBodyWater, mBodyWeight;
    private TextView mHighScoreView, mCurrentScoreView, mCountScoreView;
    private GraphView mGraph;
    private Viewport mViewport;
    private LineGraphSeries<DataPoint> mSeries;
    private static final DecimalFormat df = new DecimalFormat("00");
    private static final DecimalFormat pf = new DecimalFormat("#0.00");
    private static final DecimalFormat sf = new DecimalFormat("##");
    private static int DAYS = 0;

    private SharedPreferences cPrefs, tPrefs, sPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        mHighScoreView = (TextView) view.findViewById(R.id.score_high);
        mCurrentScoreView = (TextView) view.findViewById(R.id.score_current);
        mCountScoreView = (TextView) view.findViewById(R.id.score_count);

        mGraph = (GraphView) view.findViewById(R.id.graphView);
        mViewport = mGraph.getViewport();
        mSeries = new LineGraphSeries<>(new DataPoint[] { new DataPoint(getHours(), 0) });

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

        return view;
    }

    public void updateLabels() {
        getPrefs();

        mHighScoreView.setText("" + pf.format(mHighScore) + " ‰");
        mCurrentScoreView.setText("" + pf.format(mCurrentScore) + " ‰");
        mCountScoreView.setText("" + sf.format(Math.ceil(mCountScore)) + " x ");
    }

    private double getHours() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + Calendar.getInstance().get(Calendar.MINUTE)/60 + 24*DAYS;
    }

    protected boolean updateGraph() {
        try {
            mSeries.appendData(new DataPoint(getHours(), mCurrentScore), true, 100);
            return true;
        } catch (IllegalArgumentException e) {
            DAYS++;
            return false;
        }
    }

    private void getPrefs() {
        if (null == cPrefs)
            cPrefs = getActivity().getSharedPreferences("current", Context.MODE_PRIVATE);
        if (null == tPrefs)
            tPrefs = getActivity().getSharedPreferences("total", Context.MODE_PRIVATE);
        if (null == sPrefs)
            sPrefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        mBodyWater = sPrefs.getString("gender", "Male").equals("Male") ? 0.58 : 0.49;
        mBodyWeight = sPrefs.getInt("weight", 70);

        mHighScore = getDouble(tPrefs, "mHighScore", 0);
        mCurrentScore = getDouble(cPrefs, "mCurrentScore", 0);
        mCountScore = (mHighScore - mCurrentScore) / 0.806 / 1.2 * mBodyWater * mBodyWeight / 7.89 / 1.5;
    }

    protected void resetGraph() {
        DAYS = 0;
        mSeries.resetData(new DataPoint[]{new DataPoint(getHours(), 0)});
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }
}

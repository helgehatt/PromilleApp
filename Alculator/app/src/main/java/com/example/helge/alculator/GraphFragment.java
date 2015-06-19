package com.example.helge.alculator;

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

    private double mHighScore, mCurrentScore;
    private int mCountScore;
    private TextView mHighScoreView, mCurrentScoreView, mCountScoreView;
    private GraphView mGraph;
    private Viewport mViewport;
    private LineGraphSeries<DataPoint> mSeries;
    private static final DecimalFormat df = new DecimalFormat("00");
    private static int DAYS = 0;

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
                    return df.format(num%100-num%1) + ":" + df.format(num%1*60);
                } else {
                    return super.formatLabel(value, false);
                }
            }
        });

        mViewport.setScrollable(true);
        mViewport.setScalable(true);
        mViewport.setYAxisBoundsManual(true);
        mViewport.setMinY(0);

        updateLabels();
        return view;
    }

    private void updateLabels() {
        mHighScoreView.setText("" + mHighScore + " ‰");
        mCurrentScoreView.setText("" + mCurrentScore + " ‰");
        mCountScoreView.setText("" + mCountScore + " x ");
    }

    private double getHours() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + Calendar.getInstance().get(Calendar.MINUTE)/60 + 24*DAYS;
    }

    protected boolean updateGraph(double permille) {
        try {
            mSeries.appendData(new DataPoint(getHours(), permille), true, 100);
            return true;
        } catch (IllegalArgumentException e) {
            DAYS++;
            return false;
        }
    }

    protected void resetGraph() {
        DAYS = 0;
        mSeries.resetData(new DataPoint[]{new DataPoint(getHours(), 0)});
    }
}

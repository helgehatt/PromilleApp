package com.example.helge.alculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;

public class GraphFragment extends Fragment {

    GraphView mGraph;
    Viewport mViewport;
    LineGraphSeries<DataPoint> mSeries;
    private static int DAYS = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        mGraph = (GraphView) view.findViewById(R.id.graphView);
        mViewport = mGraph.getViewport();
        mSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(17, 0),
                new DataPoint(20, 1),
                new DataPoint(22, 2),
                new DataPoint(23, 4),
                new DataPoint(24, 3.5),
                new DataPoint(26, 3)
        });

        mGraph.addSeries(mSeries);
        mGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value%24, true);
                } else {
                    return super.formatLabel(value, false);
                }
            }
        });

        mViewport.setScrollable(true);
        mViewport.setScalable(true);
        mViewport.setYAxisBoundsManual(true);
        mViewport.setMinY(0);

//        Button resetButton = (Button) view.findViewById(R.id.button_reset);
//        resetButton.setOnClickListener(this);
//
//        Button updateButton = (Button) view.findViewById(R.id.button_update);
//        updateButton.setOnClickListener(this);

        return view;
    }

//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.button_update:
//                updateGraph(2);
//                break;
//            case R.id.button_reset:
//                resetGraph();
//                break;
//        }
//    }

    private double getHours() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + Calendar.getInstance().get(Calendar.MINUTE)/60 + 24*DAYS;
    }

    protected void updateGraph(double permille) {
        try {
            mSeries.appendData(new DataPoint(getHours(), permille), true, 100);
        } catch (IllegalArgumentException e) {
            DAYS++;
            mSeries.appendData(new DataPoint(getHours(), permille), true, 100);
        }
    }

    protected void resetGraph() {
        DAYS = 0;
        mSeries.resetData(new DataPoint[]{new DataPoint(getHours(), 0)});
    }
}

package com.example.helge.alculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphFragment extends Fragment {

    GraphView mGraph;
    LineGraphSeries<DataPoint> mSeries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        mGraph = (GraphView) view.findViewById(R.id.graphView);
        mSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(3, 2),
                new DataPoint(4, 4),
                new DataPoint(7, 3.5)
        });

        mGraph.addSeries(mSeries);


        return view;
    }
}

package com.shunix.dailypushups.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.shunix.dailypushups.BuildConfig;
import com.shunix.dailypushups.R;
import com.shunix.dailypushups.ui.Line;
import com.shunix.dailypushups.ui.LineGraph;
import com.shunix.dailypushups.ui.LinePoint;

/**
 * @author Ray WANG <admin@shunix.com>
 * @version 1.0.0
 * @since Feb 10th, 2014
 */
public class LineGraphFragment extends Fragment {

    /**
     * used as the date range picker.
     */
    private Spinner dateSpinner;
    /**
     * used to show the graph.
     */
    private LineGraph lineGraph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.linegraph_layout, container, false);
        dateSpinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(arrayAdapter);
        lineGraph = (LineGraph) view.findViewById(R.id.linegraph);
        if (BuildConfig.DEBUG) {
            /**
             * Show the graph.
             */
            Line line = new Line();
            LinePoint p1 = new LinePoint();
            p1.setX(0);
            p1.setY(20);
            line.addPoint(p1);
            LinePoint p2 = new LinePoint();
            p2.setX(1);
            p2.setY(30);
            line.addPoint(p2);
            LinePoint p3 = new LinePoint();
            p3.setX(2);
            p3.setY(15);
            line.addPoint(p3);
            line.setColor(Color.CYAN);
            lineGraph.addLine(line);
            lineGraph.setRangeY(0, 35);
            lineGraph.setLineToFill(0);
        }
        return view;
    }
}

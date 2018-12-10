package com.example.sa.sleepanalysis.fragement;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sa.sleepanalysis.R;
import com.example.sa.sleepanalysis.model.NodeData;
import com.example.sa.sleepanalysis.model.user;
import com.example.sa.sleepanalysis.network.ApiService;
import com.example.sa.sleepanalysis.network.ApiUtils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class historyFragment extends Fragment {
    private Button tag1, tag2, tag4;
    private GraphView graph;
    private ApiService mAPIService;
    private List<NodeData> nodeDataList;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {

        //Button
        tag1 = rootView.findViewById(R.id.tag1);
        tag2 = rootView.findViewById(R.id.tag2);
        tag4 = rootView.findViewById(R.id.tag4);
        tag1.setSelected(true);



        //Chart
        graph = rootView.findViewById(R.id.graph);
        user user = new user();
        getHistoryData(user.getUser_id(), "20181209", "20181209");

        initOnclick();

    }

    private void getHistoryData(int user_id, String fromDate, String toDate) {
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getHistorybyDate(user_id, fromDate, toDate).enqueue(new Callback<List<NodeData>>() {
            @Override
            public void onResponse(Call<List<NodeData>> call, Response<List<NodeData>> response) {
                nodeDataList = response.body();
                DataPoint[] statsArray;
                if(nodeDataList.size() > 0) {
                    // Code for list longer than 0, query return something
                    statsArray = new DataPoint[nodeDataList.size()]; // so this is not null now
                    for (int i = 0; i < statsArray.length; i++) {
                        statsArray[i] = new DataPoint(i, i);
                        // i+1  to start from x = 1
                    }
                }else{
                    // Query return nothing, so we add some fake point
                    // IT WON'T BE VISIBLE cus we starts graph from 1
                    statsArray = new DataPoint[] {new DataPoint(0, 0)};
                }

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(statsArray);
                // set manual X bounds
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(1.0);
                graph.getViewport().setMaxX(nodeDataList.size());
                graph.addSeries(series);
                graph.setCursorMode(true);
                graph.setTitle("Temperature");
                graph.setTitleColor(Color.parseColor("#000000"));
                graph.getGridLabelRenderer().setGridColor(Color.parseColor("#000000"));
                graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#000000"));
                graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#000000"));
                graph.setTitleTextSize(55);

            }

            @Override
            public void onFailure(Call<List<NodeData>> call, Throwable t) {

            }
        });

    }

    private void initOnclick(){
        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag1.isSelected()) {
                    tag1.setSelected(true);
                    if (tag2.isSelected()){
                        tag2.setSelected(false);
                        tag2.setPressed(false);
                    }

                    if (tag4.isSelected()) {
                        tag4.setSelected(false);
                        tag4.setPressed(false);
                    }

                }

            }
        });

        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag2.isSelected()) {
                    tag2.setSelected(true);
                    if (tag1.isSelected()){
                        tag1.setSelected(false);
                        tag1.setPressed(false);
                    }

                    if (tag4.isSelected()) {
                        tag4.setSelected(false);
                        tag4.setPressed(false);
                    }

                }

            }
        });

        tag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag4.isSelected()) {
                    tag4.setSelected(true);
                    if (tag1.isSelected()){
                        tag1.setSelected(false);
                        tag1.setPressed(false);
                    }
                    if (tag2.isSelected()){
                        tag2.setSelected(false);
                        tag2.setPressed(false);
                    }

                }

            }
        });

    }


}

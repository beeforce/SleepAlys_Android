package com.example.sa.sleepanalysis.fragement;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class graphFragment extends Fragment {
    private Button tag1, tag2, tag4;
    private LineChart chart;
    private GraphView graph;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    private boolean started = false;
    private Handler handler = new Handler();
    private ApiService mAPIService;
    private List<NodeData> nodeDataList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        initInstances(rootView);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            start();
        } else {
            stop();

        }
    }





    private void initInstances(View rootView) {

        tag1 = rootView.findViewById(R.id.tag1);
        tag2 = rootView.findViewById(R.id.tag2);
        tag4 = rootView.findViewById(R.id.tag4);

        tag1.setSelected(true);

        graph = rootView.findViewById(R.id.graph);



        initOnclick();

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

    private void getnodeData(int user_id) {
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getNodeData(user_id).enqueue(new Callback<List<NodeData>>() {
            @Override
            public void onResponse(Call<List<NodeData>> call, Response<List<NodeData>> response) {
                nodeDataList = response.body();
                DataPoint[] statsArray;
                if(nodeDataList.size() > 0) {
                    // Code for list longer than 0, query return something
                    statsArray = new DataPoint[nodeDataList.size()]; // so this is not null now
                    for (int i = 0; i < statsArray.length; i++) {
                        Date mydate = fromStringToDate(nodeDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                        statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(nodeDataList.get(i).getTemperature()));
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
                Date lastDate = fromStringToDate(nodeDataList.get(nodeDataList.size()-1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph.getViewport().setMaxX(lastDate.getTime());
                Date firstDate = fromStringToDate(nodeDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph.getViewport().setMinX(1.0);
//                graph.getViewport().setMaxX(nodeDataList.size());
                graph.addSeries(series);
                graph.setCursorMode(true);
                graph.setTitle("Temperature");
                graph.setTitleColor(Color.parseColor("#000000"));
                graph.getGridLabelRenderer().setGridColor(Color.parseColor("#000000"));
                graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#000000"));
                graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#000000"));
                graph.setTitleTextSize(55);
//                graph.getGridLabelRenderer().setNumHorizontalLabels(1);
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            return sdf.format(new Date((long) value));
                        }
                        return super.formatLabel(value, isValueX);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<NodeData>> call, Throwable t) {

            }
        });


    }

    private Date fromStringToDate(String stringDate, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = format.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            list.add(5);

            user user = new user();
            getnodeData(user.getUser_id());





            if(started) {
                start();
            }
        }
    };


    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void start() {
        started = true;
        handler.postDelayed(runnable, 3000);
    }

    }

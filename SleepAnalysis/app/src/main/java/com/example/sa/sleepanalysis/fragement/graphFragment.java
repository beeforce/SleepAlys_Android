package com.example.sa.sleepanalysis.fragement;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.sa.sleepanalysis.R;
import com.example.sa.sleepanalysis.model.NodeData;
import com.example.sa.sleepanalysis.model.user;
import com.example.sa.sleepanalysis.network.ApiService;
import com.example.sa.sleepanalysis.network.ApiUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class graphFragment extends Fragment {
    private Button tag1, tag2, tag4;
    private LineChart chart;
    private GraphView graph, graph2, graph3;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    private boolean started = false;
    private Handler handler = new Handler();
    private ApiService mAPIService;
    private List<NodeData> nodeDataList = new ArrayList<NodeData>();
    private LinearLayout temperature, humidity, vibration;



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

        temperature = rootView.findViewById(R.id.temperature);
        humidity = rootView.findViewById(R.id.humidity);
        vibration = rootView.findViewById(R.id.vibration);


        graph = rootView.findViewById(R.id.graph);
        graph2 = rootView.findViewById(R.id.graph2);
        graph3 = rootView.findViewById(R.id.graph3);




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
                    temperature.setVisibility(View.VISIBLE);
                    vibration.setVisibility(View.GONE);
                    humidity.setVisibility(View.GONE);

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
                    temperature.setVisibility(View.GONE);
                    vibration.setVisibility(View.GONE);
                    humidity.setVisibility(View.VISIBLE);
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
                    temperature.setVisibility(View.GONE);
                    vibration.setVisibility(View.VISIBLE);
                    humidity.setVisibility(View.GONE);
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
                    // Code for list longer than 0, query return somethin

                    if (temperature.getVisibility() == View.VISIBLE){
                        statsArray = new DataPoint[nodeDataList.size()]; // so this is not null now
                        for (int i = 0; i < statsArray.length; i++) {
                            Date mydate = fromStringToDate(nodeDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                            statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(nodeDataList.get(i).getTemperature()));
                            // i+1  to start from x = 1
                        }
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(statsArray);
                        // set manual X bounds
                        graph.getViewport().setXAxisBoundsManual(true);
                        Date lastDate = fromStringToDate(nodeDataList.get(nodeDataList.size()-1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                        graph.getViewport().setMaxX(lastDate.getTime());
                        Date firstDate = fromStringToDate(nodeDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                        graph.getViewport().setMinX(firstDate.getTime());
                        graph.addSeries(series);
                        graph.setTitle("Temperature");
                        graph.setTitleColor(Color.parseColor("#000000"));
                        graph.getGridLabelRenderer().setGridColor(Color.parseColor("#000000"));
                        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#000000"));
                        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#000000"));
                        graph.setTitleTextSize(55);
                        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                        graph.getGridLabelRenderer().setHumanRounding(true);
                        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#000000"));//                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext()));

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
                    else if (humidity.getVisibility() == View.VISIBLE){
                        statsArray = new DataPoint[nodeDataList.size()]; // so this is not null now
                        for (int i = 0; i < statsArray.length; i++) {
                            Date mydate = fromStringToDate(nodeDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                            statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(nodeDataList.get(i).getHumidity()));
                            // i+1  to start from x = 1
                        }
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(statsArray);
                        // set manual X bounds
                        graph2.getViewport().setXAxisBoundsManual(true);
                        Date lastDate = fromStringToDate(nodeDataList.get(nodeDataList.size()-1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                        graph2.getViewport().setMaxX(lastDate.getTime());
                        Date firstDate = fromStringToDate(nodeDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                        graph2.getViewport().setMinX(firstDate.getTime());
                        graph2.addSeries(series);
                        graph2.setTitle("Humidity");
                        graph2.setTitleColor(Color.parseColor("#000000"));
                        graph2.getGridLabelRenderer().setGridColor(Color.parseColor("#000000"));
                        graph2.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#000000"));
                        graph2.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#000000"));
                        graph2.setTitleTextSize(55);
                        graph2.getGridLabelRenderer().setNumHorizontalLabels(4);
                        graph2.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                        graph2.getGridLabelRenderer().setHumanRounding(true);
                        graph2.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#000000"));

                        graph2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                            @Override
                            public String formatLabel(double value, boolean isValueX) {
                                if (isValueX) {
                                    return sdf.format(new Date((long) value));
                                }
                                return super.formatLabel(value, isValueX);
                            }
                        });
                    }

                    else if (vibration.getVisibility() == View.VISIBLE){
                        statsArray = new DataPoint[nodeDataList.size()]; // so this is not null now
                        for (int i = 0; i < statsArray.length; i++) {
                            Date mydate = fromStringToDate(nodeDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                            statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(nodeDataList.get(i).getVibration()));
                            // i+1  to start from x = 1
                        }
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(statsArray);
                        // set manual X bounds
                        graph3.getViewport().setXAxisBoundsManual(true);
                        Date lastDate = fromStringToDate(nodeDataList.get(nodeDataList.size()-1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                        graph3.getViewport().setMaxX(lastDate.getTime());
                        Date firstDate = fromStringToDate(nodeDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                        graph3.getViewport().setMinX(firstDate.getTime());
                        graph3.addSeries(series);
                        graph3.setTitle("Vibration");
                        graph3.setTitleColor(Color.parseColor("#000000"));
                        graph3.getGridLabelRenderer().setGridColor(Color.parseColor("#000000"));
                        graph3.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#000000"));
                        graph3.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#000000"));
                        graph3.setTitleTextSize(55);
                        graph3.getGridLabelRenderer().setNumHorizontalLabels(4);
                        graph3.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                        graph3.getGridLabelRenderer().setHumanRounding(true);
                        graph3.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#000000"));//                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext()));

                        graph3.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                            @Override
                            public String formatLabel(double value, boolean isValueX) {
                                if (isValueX) {
                                    return sdf.format(new Date((long) value));
                                }
                                return super.formatLabel(value, isValueX);
                            }
                        });
                    }


                }else{
                    // Query return nothing, so we add some fake point
                    // IT WON'T BE VISIBLE cus we starts graph from 1
//                    statsArray = new DataPoint[] {new DataPoint(0, 0)};

                    graph.setTitle("No data available");
                    graph.setTitleColor(Color.parseColor("#000000"));
                    graph.setTitleTextSize(55);
                    graph2.setTitle("No data available");
                    graph2.setTitleColor(Color.parseColor("#000000"));
                    graph2.setTitleTextSize(55);
                    graph3.setTitle("No data available");
                    graph3.setTitleColor(Color.parseColor("#000000"));
                    graph3.setTitleTextSize(55);
                    graph.removeAllSeries();
                    graph2.removeAllSeries();
                    graph3.removeAllSeries();
                }


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

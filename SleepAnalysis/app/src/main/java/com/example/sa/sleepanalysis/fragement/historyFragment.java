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


public class historyFragment extends Fragment {
    private Button tag1, tag2, tag4;
    private GraphView graph;
    private ApiService mAPIService;
    private List<NodeData> nodeDataList;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");






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
        getHistoryData(user.getUser_id(), "20181211", "20181211");

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
                        Date mydate = fromStringToDate(nodeDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                        statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(nodeDataList.get(i).getTemperature()));
                        // i+1  to start from x = 1
                    }

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(statsArray);
                    // set manual X bounds
                    graph.getViewport().setXAxisBoundsManual(true);
                    Date lastDate = fromStringToDate(nodeDataList.get(nodeDataList.size()-1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                    graph.getViewport().setMaxX(lastDate.getTime()+10000000);
                    Date firstDate = fromStringToDate(nodeDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                    graph.getViewport().setMinX(firstDate.getTime()-20000000);
                    graph.addSeries(series);
                    graph.setTitle("Temperature");
                    graph.setTitleColor(Color.parseColor("#000000"));
                    graph.getGridLabelRenderer().setGridColor(Color.parseColor("#000000"));
                    graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#000000"));
                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#000000"));
                    graph.setTitleTextSize(55);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(5);
//                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext()));

                    graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if (isValueX) {
                                return sdf.format(new Date((long) value));
                            }
                            return super.formatLabel(value, isValueX);
                        }
                    });
                }else{
                    graph.setTitle("No data available");
                    graph.setTitleColor(Color.parseColor("#000000"));

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

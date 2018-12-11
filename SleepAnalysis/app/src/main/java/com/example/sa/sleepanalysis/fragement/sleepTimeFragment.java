package com.example.sa.sleepanalysis.fragement;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sa.sleepanalysis.R;
import com.example.sa.sleepanalysis.model.NodeData;
import com.example.sa.sleepanalysis.model.Userdetail;
import com.example.sa.sleepanalysis.model.user;
import com.example.sa.sleepanalysis.network.AccessToken;
import com.example.sa.sleepanalysis.network.ApiService;
import com.example.sa.sleepanalysis.network.ApiUtils;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sleepTimeFragment extends Fragment {

    private ToggleButton tg;
    private Chronometer chronometer;
    private long timeWhenStopped = 0;
    private boolean stopClicked;
    private TextView secondsText;
    private ApiService mAPIService;
    private TextView name, dateofBirth, age, sleepTime;
    private user user = new user();
    private Button tag1, tag2, tag4, closed;
    private GraphView graph, graph2, graph3;
    private List<NodeData> nodeDataList = new ArrayList<NodeData>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    private LinearLayout temperature, humidity, vibration, graphLayout;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sleep_time, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {

        chronometer = rootView.findViewById(R.id.chronometer);

        secondsText = rootView.findViewById(R.id.hmsTekst);

        name = rootView.findViewById(R.id.user_name);
        dateofBirth = rootView.findViewById(R.id.user_birthday);
        age = rootView.findViewById(R.id.user_age);
        sleepTime = rootView.findViewById(R.id.sleepSuggest);

        getuserDetail(user.getUser_id());
//        Log.e("user id", "initInstances: "+ user.getUser_id() );

        tg = rootView.findViewById(R.id.toggleButton);


        //Button
        tag1 = rootView.findViewById(R.id.tag1);
        tag2 = rootView.findViewById(R.id.tag2);
        tag4 = rootView.findViewById(R.id.tag4);
        tag1.setSelected(true);
        closed = rootView.findViewById(R.id.closed);


        temperature = rootView.findViewById(R.id.temperature);
        humidity = rootView.findViewById(R.id.humidity);
        vibration = rootView.findViewById(R.id.vibration);
        graphLayout = rootView.findViewById(R.id.graphLayout);


        graph = rootView.findViewById(R.id.graph);
        graph2 = rootView.findViewById(R.id.graph2);
        graph3 = rootView.findViewById(R.id.graph3);

        initOnclick();





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

    private void getuserDetail(int user_id) {
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getUserdetail(user_id).enqueue(new Callback<Userdetail>() {
            @Override
            public void onResponse(Call<Userdetail> call, Response<Userdetail> response) {

                name.setText(response.body().getData().getName());
                Date mydate = fromStringToDate(response.body().getData().getDateofBirth(), "yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dateofBirth.setText(sdf.format(mydate.getTime()));

                age.setText(""+response.body().getData().getAge());
                if (response.body().getData().getAge() >= 0 && response.body().getData().getAge() <=2){
                    sleepTime.setText("11-14 Hours");
                }else if (response.body().getData().getAge() >= 3 && response.body().getData().getAge() <=5){
                    sleepTime.setText("10-13 Hours");
                }else if (response.body().getData().getAge() >= 6 && response.body().getData().getAge() <=13){
                    sleepTime.setText("9-11 Hours");
                }else if (response.body().getData().getAge() >= 14 && response.body().getData().getAge() <=17){
                    sleepTime.setText("8-10 Hours");
                }else if (response.body().getData().getAge() >= 18 && response.body().getData().getAge() <=25){
                    sleepTime.setText("7-9 Hours");
                }else {
                        sleepTime.setText("7-8 Hours");
                }

            }

            @Override
            public void onFailure(Call<Userdetail> call, Throwable t) {
                Log.e("user detail", "onFailure: "+ t.toString() );
            }
        });


    }

    private void initOnclick(){

        tg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tg.isChecked()) {
                    resetButtonClick();
                    tg.setText("Stop");
                    startButtonClick();

                }
                else
                {
                    tg.setText("Start");
                    stopButtonClick();
                }
            }
        });

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
                    setGraph();

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
                    setGraph();

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
                    setGraph();

                }

            }
        });

        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphLayout.setVisibility(View.GONE);
            }
        });

    }

    private void addUserSleepingHours(int user_id, String hour) {
        final int userId = user_id;
        final String hourS = hour;
        mAPIService = ApiUtils.getAPIService();

        RequestBody hourR = RequestBody.create(MultipartBody.FORM, hour);
        RequestBody id = RequestBody.create(MultipartBody.FORM, ""+user_id);

        mAPIService.addSleepingHours(id, hourR).enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.body().isSuccess()){
                    Log.e("add sleeping hour", "onResponse: "+ response.body().getMessage() );
                    getHistorybyHours(userId, "100.30");
                }
                else{
                    Log.e("add sleeping hour", "onResponse: "+ response.body().getMessage() );

                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e("add sleeping hours", "onFailure: "+ t.toString() );
            }
        });

    }

    private void getHistorybyHours(int user_id, String hours) {
        mAPIService = ApiUtils.getAPIService();
        String[] parts = hours.split(Pattern.quote("."));
        String part1 = parts[0];
        String part2 = parts[1];
        mAPIService.getHistorybyHours(user_id, Integer.parseInt(part1), Integer.parseInt(part2)).enqueue(new Callback<List<NodeData>>() {
            @Override
            public void onResponse(Call<List<NodeData>> call, Response<List<NodeData>> response) {
                nodeDataList = response.body();
                setGraph();
                graphLayout.setVisibility(View.VISIBLE);


            }

            @Override
            public void onFailure(Call<List<NodeData>> call, Throwable t) {

            }
        });


    }

    public void resetButtonClick() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        secondsText.setText("00:00:00");
    }

    // the method for when we press the 'start' button
    public void startButtonClick() {
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();
        stopClicked = false;

    }

    // the method for when we press the 'stop' button
    public void stopButtonClick(){
        if (!stopClicked)  {
            chronometer.stop();
            stopClicked = true;
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            int seconds = (int) (timeWhenStopped / 1000) % 60;
            int minutes = (int) ((timeWhenStopped / (1000*60)) % 60);
            int hours   = (int) ((timeWhenStopped / (1000*60*60)) % 24);
            String secondsString = Integer.toString(Math.abs(seconds));
            String minuteString = Integer.toString(Math.abs(minutes));
            String hoursString = Integer.toString(Math.abs(hours));


            addUserSleepingHours(user.getUser_id(), hoursString+"."+minuteString);


            if (Math.abs(seconds) < 10){
                secondsString = "0"+secondsString;
            }
            if (Math.abs(minutes) < 10){
                minuteString = "0"+minuteString;
            }
            if (hours < 10){
                hoursString = "0"+hoursString;
            }


            secondsText.setText(hoursString+"::"+minuteString+"::"+secondsString);

        }
    }

    private void setGraph(){
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
                graph.setTitleColor(Color.parseColor("#ffffff"));
                graph.getGridLabelRenderer().setGridColor(Color.parseColor("#ffffff"));
                graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#ffffff"));
                graph.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#ffffff"));
                graph.setTitleTextSize(55);
                graph.getGridLabelRenderer().setNumHorizontalLabels(4);
                graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                graph.getGridLabelRenderer().setHumanRounding(true);
                graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#ffffff"));//                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext()));

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
                graph2.setTitleColor(Color.parseColor("#ffffff"));
                graph2.getGridLabelRenderer().setGridColor(Color.parseColor("#ffffff"));
                graph2.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#ffffff"));
                graph2.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#ffffff"));
                graph2.setTitleTextSize(55);
                graph2.getGridLabelRenderer().setNumHorizontalLabels(4);
                graph2.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                graph2.getGridLabelRenderer().setHumanRounding(true);
                graph2.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#ffffff"));

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

            else if (vibration.getVisibility() == View.VISIBLE) {
                statsArray = new DataPoint[nodeDataList.size()]; // so this is not null now
                for (int i = 0; i < statsArray.length; i++) {
                    Date mydate = fromStringToDate(nodeDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                    statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(nodeDataList.get(i).getVibration()));
                    // i+1  to start from x = 1
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(statsArray);
                // set manual X bounds
                graph3.getViewport().setXAxisBoundsManual(true);
                Date lastDate = fromStringToDate(nodeDataList.get(nodeDataList.size() - 1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph3.getViewport().setMaxX(lastDate.getTime());
                Date firstDate = fromStringToDate(nodeDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph3.getViewport().setMinX(firstDate.getTime());
                graph3.addSeries(series);
                graph3.setTitle("Vibration");
                graph3.setTitleColor(Color.parseColor("#ffffff"));
                graph3.getGridLabelRenderer().setGridColor(Color.parseColor("#ffffff"));
                graph3.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#ffffff"));
                graph3.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#ffffff"));
                graph3.setTitleTextSize(55);
                graph3.getGridLabelRenderer().setNumHorizontalLabels(4);
                graph3.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                graph3.getGridLabelRenderer().setHumanRounding(true);
                graph3.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#ffffff"));//                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext()));

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

            graph.setTitle("No data available");
            graph.setTitleColor(Color.parseColor("#ffffff"));
            graph2.setTitle("No data available");
            graph2.setTitleColor(Color.parseColor("#ffffff"));
            graph3.setTitle("No data available");
            graph3.setTitleColor(Color.parseColor("#ffffff"));
            graph.removeAllSeries();
            graph2.removeAllSeries();
            graph3.removeAllSeries();


        }
    }

}

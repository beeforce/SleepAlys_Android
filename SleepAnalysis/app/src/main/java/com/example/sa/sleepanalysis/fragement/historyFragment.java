package com.example.sa.sleepanalysis.fragement;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sa.sleepanalysis.R;
import com.example.sa.sleepanalysis.model.NodeData;
import com.example.sa.sleepanalysis.model.SleepTime;
import com.example.sa.sleepanalysis.model.Userdetail;
import com.example.sa.sleepanalysis.model.user;
import com.example.sa.sleepanalysis.network.ApiService;
import com.example.sa.sleepanalysis.network.ApiUtils;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class historyFragment extends Fragment {
    private Button tag1, tag2, tag4, tag5;
    private GraphView graph, graph2, graph3, graph4;
    private ApiService mAPIService;
    private List<NodeData> nodeDataList = new ArrayList<NodeData>();
    private List<SleepTime> qualityDataList = new ArrayList<SleepTime>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    private LinearLayout temperature, humidity, vibration, qualityTime;
    private TextView toDate, fromDate;
    private String fromDatestring = "", toDatestring = "";
    private int userAge, timeQualityuser;







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
        tag5 = rootView.findViewById(R.id.tag5);
        tag1.setSelected(true);

        temperature = rootView.findViewById(R.id.temperature);
        humidity = rootView.findViewById(R.id.humidity);
        vibration = rootView.findViewById(R.id.vibration);
        qualityTime = rootView.findViewById(R.id.qualityTime);


        graph = rootView.findViewById(R.id.graph);
        graph2 = rootView.findViewById(R.id.graph2);
        graph3 = rootView.findViewById(R.id.graph3);
        graph4 = rootView.findViewById(R.id.graph4);

        fromDate = rootView.findViewById(R.id.fromDate);
        toDate = rootView.findViewById(R.id.toDate);

        user user = new user();
        getuserDetail(user.getUser_id());

        fromDate.setClickable(true);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        fromDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        fromDatestring = "" + selectedyear + "" + selectedmonth + "" + selectedday;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();

            }
        });

        toDate.setClickable(true);
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        toDate.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        toDatestring = "" + selectedyear + "" + selectedmonth + "" + selectedday;

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        Button search = rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromDatestring != "" && toDatestring != ""){
                    nodeDataList.removeAll(nodeDataList);
                    user user = new user();
                    getHistoryData(user.getUser_id(), fromDatestring, toDatestring);
                    getQualityData(user.getUser_id(), fromDatestring, toDatestring);
                }
                }

        });


        initOnclick();

    }

    private void setGraph(){
        DataPoint[] statsArray;
        if(nodeDataList.size() > 0) {
            // Code for list longer than 0, query return somethin

//            if (temperature.getVisibility() == View.VISIBLE){
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
//                graph.addSeries(second);
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
//            }
//            else if (humidity.getVisibility() == View.VISIBLE){
                statsArray = new DataPoint[nodeDataList.size()]; // so this is not null now
                for (int i = 0; i < statsArray.length; i++) {
                    Date mydate = fromStringToDate(nodeDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                    statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(nodeDataList.get(i).getHumidity()));
                    // i+1  to start from x = 1
                }
                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(statsArray);
                // set manual X bounds
                graph2.getViewport().setXAxisBoundsManual(true);
                Date lastDate2 = fromStringToDate(nodeDataList.get(nodeDataList.size()-1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph2.getViewport().setMaxX(lastDate2.getTime());
                Date firstDate2 = fromStringToDate(nodeDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph2.getViewport().setMinX(firstDate2.getTime());
                graph2.addSeries(series2);
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
//            }

//            else if (vibration.getVisibility() == View.VISIBLE) {
                statsArray = new DataPoint[nodeDataList.size()]; // so this is not null now
                for (int i = 0; i < statsArray.length; i++) {
                    Date mydate = fromStringToDate(nodeDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                    statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(nodeDataList.get(i).getVibration()));
                    // i+1  to start from x = 1
                }
                LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(statsArray);
                // set manual X bounds
                graph3.getViewport().setXAxisBoundsManual(true);
                Date lastDate3 = fromStringToDate(nodeDataList.get(nodeDataList.size() - 1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph3.getViewport().setMaxX(lastDate3.getTime());
                Date firstDate3 = fromStringToDate(nodeDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph3.getViewport().setMinX(firstDate3.getTime());
                graph3.addSeries(series3);
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
//            }

    }else{

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

    private void setGraphQuality(){
        DataPoint[] statsArray;
        DataPoint[] secondArray;
        if(qualityDataList.size() > 0) {
            // Code for list longer than 0, query return somethin

                statsArray = new DataPoint[qualityDataList.size()]; // so this is not null now
                for (int i = 0; i < statsArray.length; i++) {
                    Date mydate = fromStringToDate(qualityDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                    statsArray[i] = new DataPoint(mydate.getTime(), Double.parseDouble(qualityDataList.get(i).getQuantity_hours()));
                    // i+1  to start from x = 1
                }

            secondArray = new DataPoint[qualityDataList.size()]; // so this is not null now
            for (int i = 0; i < statsArray.length; i++) {
                Date mydate = fromStringToDate(qualityDataList.get(i).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                secondArray[i] = new DataPoint(mydate.getTime(), timeQualityuser);
                // i+1  to start from x = 1
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(statsArray);
            LineGraphSeries<DataPoint> second = new LineGraphSeries<>(secondArray);
            // set manual X bounds
                graph4.getViewport().setXAxisBoundsManual(true);
                graph4.getViewport().setYAxisBoundsManual(true);
                Date lastDate = fromStringToDate(qualityDataList.get(qualityDataList.size()-1).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
                graph4.getViewport().setMaxX(lastDate.getTime());
                Date firstDate = fromStringToDate(qualityDataList.get(0).getCreated_at(), "yyyy-MM-dd HH:mm:ss");
            graph4.getViewport().setMinX(firstDate.getTime());
            graph4.addSeries(series);
            second.setColor(Color.GREEN);
            graph4.getViewport().setMaxY(18);
            graph4.getViewport().setMinY(-1);
            graph4.addSeries(second);
            graph4.setTitle("Quality Time");
            graph4.setTitleColor(Color.parseColor("#000000"));
            graph4.getGridLabelRenderer().setGridColor(Color.parseColor("#000000"));
            graph4.getGridLabelRenderer().setHorizontalLabelsColor(Color.parseColor("#000000"));
            graph4.getGridLabelRenderer().setVerticalLabelsColor(Color.parseColor("#000000"));
            graph4.setTitleTextSize(55);
            graph4.getGridLabelRenderer().setNumHorizontalLabels(4);
            graph4.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            graph4.getGridLabelRenderer().setHumanRounding(true);
            graph4.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.parseColor("#000000"));//
            //              graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext()));


//            Paint paint = new Paint();
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(10);
//            paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
//            series2.setCustomPaint(paint);

            graph4.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            return sdf.format(new Date((long) value));
                        }
                        return super.formatLabel(value, isValueX);
                    }
                });

        }else{

            graph4.setTitle("No data available");
            graph4.setTitleColor(Color.parseColor("#000000"));
            graph4.setTitleTextSize(55);
            graph4.removeAllSeries();


        }
    }

    private void getHistoryData(int user_id, String fromDate, String toDate) {
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getHistorybyDate(user_id, fromDate, toDate).enqueue(new Callback<List<NodeData>>() {
            @Override
            public void onResponse(Call<List<NodeData>> call, Response<List<NodeData>> response) {
                nodeDataList = response.body();
                setGraph();

            }

            @Override
            public void onFailure(Call<List<NodeData>> call, Throwable t) {

            }
        });

    }

    private void getQualityData(int user_id, String fromDate, String toDate) {
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getQualityTimebyDate(user_id+".G", fromDate, toDate).enqueue(new Callback<List<SleepTime>>() {
            @Override
            public void onResponse(Call<List<SleepTime>> call, Response<List<SleepTime>> response) {
                qualityDataList = response.body();
                setGraphQuality();

            }

            @Override
            public void onFailure(Call<List<SleepTime>> call, Throwable t) {

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
                    if (tag5.isSelected()){
                        tag5.setSelected(false);
                        tag5.setPressed(false);
                    }
                    temperature.setVisibility(View.VISIBLE);
                    vibration.setVisibility(View.GONE);
                    humidity.setVisibility(View.GONE);
                    qualityTime.setVisibility(View.GONE);
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
                    if (tag5.isSelected()){
                        tag5.setSelected(false);
                        tag5.setPressed(false);
                    }
                    temperature.setVisibility(View.GONE);
                    vibration.setVisibility(View.GONE);
                    humidity.setVisibility(View.VISIBLE);
                    qualityTime.setVisibility(View.GONE);
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
                    if (tag5.isSelected()){
                        tag5.setSelected(false);
                        tag5.setPressed(false);
                    }
                    temperature.setVisibility(View.GONE);
                    vibration.setVisibility(View.VISIBLE);
                    humidity.setVisibility(View.GONE);
                    qualityTime.setVisibility(View.GONE);
                    setGraph();

                }

            }
        });

        tag5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tag5.isSelected()) {
                    tag5.setSelected(true);
                    if (tag1.isSelected()){
                        tag1.setSelected(false);
                        tag1.setPressed(false);
                    }
                    if (tag2.isSelected()){
                        tag2.setSelected(false);
                        tag2.setPressed(false);
                    }
                    if (tag4.isSelected()){
                        tag4.setSelected(false);
                        tag4.setPressed(false);
                    }
                    temperature.setVisibility(View.GONE);
                    vibration.setVisibility(View.GONE);
                    humidity.setVisibility(View.GONE);
                    qualityTime.setVisibility(View.VISIBLE);
                    setGraph();

                }
            }
        });

    }

    private void getuserDetail(int user_id) {
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getUserdetail(user_id).enqueue(new Callback<Userdetail>() {
            @Override
            public void onResponse(Call<Userdetail> call, Response<Userdetail> response) {

                if (response.body().getData().getAge() >= 0 && response.body().getData().getAge() <=2){
                    timeQualityuser = 11;
                }else if (response.body().getData().getAge() >= 3 && response.body().getData().getAge() <=5){
                    timeQualityuser = 10;
                }else if (response.body().getData().getAge() >= 6 && response.body().getData().getAge() <=13){
                    timeQualityuser = 9;
                }else if (response.body().getData().getAge() >= 14 && response.body().getData().getAge() <=17){
                    timeQualityuser = 8;
                }else if (response.body().getData().getAge() >= 18 && response.body().getData().getAge() <=25){
                    timeQualityuser = 7;
                }else {
                    timeQualityuser = 7;
                }

            }

            @Override
            public void onFailure(Call<Userdetail> call, Throwable t) {
                Log.e("user detail", "onFailure: "+ t.toString() );
            }
        });


    }


}

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


    }

    private void addUserSleepingHours(int user_id, String hour) {
//        final int userId = user_id;
//        final String hourS = hour;
        mAPIService = ApiUtils.getAPIService();

        RequestBody hourR = RequestBody.create(MultipartBody.FORM, hour);
        RequestBody id = RequestBody.create(MultipartBody.FORM, ""+user_id);

        mAPIService.addSleepingHours(id, hourR).enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.body().isSuccess()){
                    Log.e("add sleeping hour", "onResponse: "+ response.body().getMessage() );
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




            if (Math.abs(seconds) < 10){
                secondsString = "0"+secondsString;
            }
            if (Math.abs(minutes) < 10){
                minuteString = "0"+minuteString;
            }
            if (hours < 10){
                hoursString = "0"+hoursString;
            }

            addUserSleepingHours(user.getUser_id(), hoursString+"."+minuteString);



            secondsText.setText(hoursString+"::"+minuteString+"::"+secondsString);

        }
    }

}

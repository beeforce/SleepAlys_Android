package com.example.buri_paoton.sleepanalysis.fragement;

import android.app.AlarmManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.buri_paoton.sleepanalysis.MainActivity;
import com.example.buri_paoton.sleepanalysis.R;

import java.util.Calendar;

public class sleepTimeFragment extends Fragment {

    private ToggleButton tg;
    private Chronometer chronometer;
    private long timeWhenStopped = 0;
    private boolean stopClicked;
    private TextView secondsText;



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

        tg = rootView.findViewById(R.id.toggleButton);
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


            secondsText.setText(hoursString+"::"+minuteString+"::"+secondsString);

        }
    }

}

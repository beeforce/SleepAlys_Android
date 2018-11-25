package com.example.buri_paoton.sleepanalysis.fragement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.buri_paoton.sleepanalysis.AlarmScreen;
import com.example.buri_paoton.sleepanalysis.R;

import static android.content.ContentValues.TAG;

public class alarmClockFragment extends Fragment {

    private LinearLayout layout1, layout2;
    private Button setAlarm;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm_clock, container, false);
        initInstances(rootView);

        return rootView;
    }

    private void initInstances(View rootView) {

        alarmManager = (AlarmManager) getActivity().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmScreen.class);
        pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        //LinearLayout
        layout1 = rootView.findViewById(R.id.layout1);
        layout2 = rootView.findViewById(R.id.layout2);

        //Button
        setAlarm = rootView.findViewById(R.id.setAlarmclock);
        setAlarm.setText("Set");

        initOnclick();

    }

    private void initOnclick(){

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = setAlarm.getText().toString();
                if (text == "Set"){
                    setAlarm.setText("Cancel");
                    layout2.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);

                }
                else{
                    setAlarm.setText("Set");
                    layout2.setVisibility(View.VISIBLE);
                    layout1.setVisibility(View.GONE);
                    alarmManager.cancel(pendingIntent);

                }


            }
        });
    }

}

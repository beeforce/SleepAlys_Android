package com.example.sa.sleepanalysis.fragement;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sa.sleepanalysis.AlarmScreen;
import com.example.sa.sleepanalysis.R;

import java.util.Calendar;
import java.util.Date;

public class alarmClockFragment extends Fragment {

    private LinearLayout layout1, layout2;
    private Button setAlarm, suggestTime1, suggestTime2, suggestTime3, suggestTime4;
    private TextView alarmTime;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private int alarmHour, alarmMinute;




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

        Calendar mcurrentTime = Calendar.getInstance();
        mcurrentTime.add(Calendar.MINUTE, 1);
        alarmHour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        alarmMinute = mcurrentTime.get(Calendar.MINUTE);




        //LinearLayout
        layout1 = rootView.findViewById(R.id.layout1);
        layout2 = rootView.findViewById(R.id.layout2);

        //Button
        setAlarm = rootView.findViewById(R.id.setAlarmclock);
        setAlarm.setText("Set");
        suggestTime1 = rootView.findViewById(R.id.suggestTime1);
        suggestTime2 = rootView.findViewById(R.id.suggestTime2);
        suggestTime3 = rootView.findViewById(R.id.suggestTime3);
        suggestTime4 = rootView.findViewById(R.id.suggestTime4);


        alarmTime = rootView.findViewById(R.id.alarmTime);
        String minuteString = Integer.toString(alarmMinute);
        String hoursString = Integer.toString(alarmHour);

        if (alarmMinute < 10){
            minuteString = "0"+minuteString;
        }
        if (alarmHour < 10){
            hoursString = "0"+hoursString;
        }

        alarmTime.setText( hoursString + ":" + minuteString);


        initOnclick();

    }

    private void initOnclick(){

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = setAlarm.getText().toString();
                if (text == "Set"){
                    long time;
                    setAlarm.setText("Cancel");
                    layout2.setVisibility(View.GONE);
                    layout1.setVisibility(View.VISIBLE);
                    Calendar alarm = Calendar.getInstance();
                    alarm.set(Calendar.HOUR_OF_DAY, alarmHour);
                    alarm.set(Calendar.MINUTE, alarmMinute);

                    time = (alarm.getTimeInMillis() - (alarm.getTimeInMillis() % 60000));
                    if (System.currentTimeMillis() > time) {
                        if (alarm.AM_PM == 0)
                            time = time + (1000 * 60 * 60 * 12);
                        else
                            time = time + (1000 * 60 * 60 * 24);
                    }
                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.HOUR_OF_DAY, -9);
                    calendar.add(Calendar.MINUTE, -15);
                    suggestTime1.setText( calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

                    Date date2 = new Date();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(date2);
                    calendar2.add(Calendar.HOUR_OF_DAY, -7);
                    calendar2.add(Calendar.MINUTE, -45);
                    suggestTime2.setText( calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE));

                    Date date3 = new Date();
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(date3);
                    calendar3.add(Calendar.HOUR_OF_DAY, -6);
                    calendar3.add(Calendar.MINUTE, -15);
                    suggestTime3.setText( calendar3.get(Calendar.HOUR_OF_DAY) + ":" + calendar3.get(Calendar.MINUTE));

                    Date date4 = new Date();
                    Calendar calendar4 = Calendar.getInstance();
                    calendar4.setTime(date4);
                    calendar4.add(Calendar.HOUR_OF_DAY, -4);
                    calendar4.add(Calendar.MINUTE, -45);
                    suggestTime4.setText( calendar4.get(Calendar.HOUR_OF_DAY) + ":" + calendar4.get(Calendar.MINUTE));


                }
                else{
                    setAlarm.setText("Set");
                    layout2.setVisibility(View.VISIBLE);
                    layout1.setVisibility(View.GONE);
                    alarmManager.cancel(pendingIntent);

                }


            }
        });

        alarmTime.setClickable(true);
        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String minuteString = Integer.toString(selectedMinute);
                        String hoursString = Integer.toString(selectedHour);
                        alarmHour = selectedHour;
                        alarmMinute = selectedMinute;

                        if (selectedMinute < 10){
                            minuteString = "0"+minuteString;
                        }
                        if (selectedHour < 10){
                            hoursString = "0"+hoursString;
                        }

                        alarmTime.setText( hoursString + ":" + minuteString);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }


}

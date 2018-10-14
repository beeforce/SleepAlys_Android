package com.example.buri_paoton.sleepanalysis;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class AlarmScreen extends AppCompatActivity {
    private PowerManager.WakeLock mWakeLock;
    Ringtone ringtone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
        {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Wake Lock");
        mWakeLock.acquire();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm_screen);
    }
    public void stopAlarm(View v){
        AlarmManager alarmManager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);

        mWakeLock.release();
        mWakeLock.acquire();
        ringtone.stop();
        finish();
    }
    protected void onPause()
    {
        super.onPause();
        mWakeLock.release();
        mWakeLock.acquire();
    }
}

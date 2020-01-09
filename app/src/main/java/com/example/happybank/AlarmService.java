package com.example.happybank;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

public class AlarmService extends Service {

    private AlarmManager alarmManager;
    private Context appContext;

    //Fields for the deposit notification alarm
    public static final int depositNotIntentCode = 1;
    private PendingIntent depositNotOperation;

    public AlarmService(Context context) {
        super();
        appContext = context;
        alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarms() {

        //Set deposit alarm to start at correct time
        //Get this time from the preferences file
        SharedPreferences preferences = appContext.getSharedPreferences(appContext.getString(R.string.preferences_filename), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        String bedtime = preferences.getString("BED_ID", appContext.getString(R.string.default_bedtime));
        String[] bedtimeParts = bedtime.split(":");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bedtimeParts[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(bedtimeParts[1]));

        //Check that the calendar isn't in the past
        //If it is then set it starting from tomorrow
        if(cal.before(Calendar.getInstance())) {
            cal.setTimeInMillis(cal.getTimeInMillis() + 24*60*60*1000);
        }

        //Incase there is already a notification, cancel it
        if(depositNotOperation != null) {
            alarmManager.cancel(depositNotOperation); // in case it is already running
        }
        Intent intent = new Intent(appContext, DepositNotificationService.class);
        depositNotOperation = PendingIntent.getService(appContext, depositNotIntentCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, depositNotOperation);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 10 * 1000, AlarmManager.INTERVAL_DAY, depositNotOperation);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

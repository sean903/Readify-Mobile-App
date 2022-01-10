package com.mobileapllication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.mobileapllication.alarm.AlarmReceiver;

import java.text.DecimalFormat;
import java.util.Calendar;

//import android.support.v4.app.Fragment;

//Notficatons + user guide + playstore
public class settingsFragment extends Fragment {

    private Button buttonShare, userGuide, buyBooksBtn;
    private SwitchCompat notififyButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonShare = v.findViewById(R.id.shareButton);
        userGuide = v.findViewById(R.id.userGuide);
        notififyButton = v.findViewById(R.id.switchAlarm);
        buyBooksBtn = v.findViewById(R.id.buyBooksBtn);

        //fetching current state of alarm time
        SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String alarmTime = sp.getString("alarm_time", null);

        DecimalFormat decimalFormat = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(alarmTime)) {
            notififyButton.setChecked(true);
            notififyButton.setText(String.format("Reading Alarm: %s", alarmTime));
            calendar.set(Calendar.HOUR, Integer.parseInt(alarmTime.split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(alarmTime.split(":")[1]));
        } else {
            notififyButton.setChecked(false);
        }


        notififyButton.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                //showing time picker when reading notification is checked
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("alarm_time", decimalFormat.format(selectedHour) + ":" + decimalFormat.format(selectedMinute));
                    editor.apply();
                    calendar.set(Calendar.HOUR, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    setAlarm(calendar);
                    notififyButton.setText(String.format("Reading Alarm: %s", sp.getString("alarm_time", null)));
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.setOnCancelListener(dialogInterface -> notififyButton.setChecked(false));
                mTimePicker.show();
            } else {
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("alarm_time");
                editor.apply();
                cancelAlarm();
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareTitle = "Join Readify!";
                String shareUsername = "Download Readify in the PlayStore with this link" +
                        "www.playStore/readify.com" ;
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareTitle);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareUsername);
                startActivity(Intent.createChooser(myIntent, "Share Readify!"));
            }
        });

        userGuide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getContext().getApplicationContext(), UserGuide.class));
            }
        });

// Go to Google Book store app
        buyBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.books");
                startActivity(i);
            }
        });


        return v;
    }

    /**
     * this method sets alarm for given calendar time
     */
    private void setAlarm(Calendar targetCal) {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * this method cancels alarm
     */
    private void cancelAlarm() {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


}

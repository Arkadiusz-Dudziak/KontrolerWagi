package com.example.tsm_ad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.Calendar;



public class OptionsActivity extends AppCompatActivity {

    TextView tvTimer1, hoursText;
    EditText height_TV, age_TV;
    Button confirm_settingsB, button, button2;
    RadioGroup radioGroup;
    RadioButton radioButton;
    SwitchCompat switch_notification;

    int t1Hour=12, t1Minute=0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        tvTimer1 = findViewById(R.id.tv_timer1);
        hoursText = (TextView)findViewById(R.id.hoursText);
        confirm_settingsB = findViewById(R.id.confirm_settings_Button);
        height_TV = findViewById(R.id.height_EditText);
        age_TV = findViewById(R.id.age_EditText);
        button = findViewById(R.id.goto_main_Button);
        button2 = findViewById(R.id.goto_chart_Button);
        radioGroup = findViewById(R.id.radioGroup);
        switch_notification = findViewById(R.id.notificationSwitch);


        View someView = findViewById(R.id.ConstraintLayout);
        View root = someView.getRootView();

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        height_TV.setText(settings.getString("Height", "").toString());
        age_TV.setText(settings.getString("Age", "").toString());
        String text = settings.getString("Gender", "").toString();
        Boolean notification = settings.getBoolean("Notification", false);
        String hours_txt = settings.getString("Hour", "").toString();
        String minutes_txt = settings.getString("Minute", "").toString();
        //button.setText();

        if(text.charAt(0)=='K')
        {
            ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.lightPink));
        }
        if(text.charAt(0)=='M')
        {
            ((RadioButton)radioGroup.getChildAt(1)).setChecked(true);
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBlue));
        }

        //ustawienia powiadomien
        if(notification)
            switch_notification.setChecked(true);
        else
            switch_notification.setChecked(false);
        hoursText.setText(hours_txt + ":"+minutes_txt);
        if(!hours_txt.isEmpty())
        {
            t1Hour = Integer.valueOf(hours_txt);
            t1Minute = Integer.valueOf(minutes_txt);
        }


        tvTimer1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        OptionsActivity.this,
                        new TimePickerDialog.OnTimeSetListener()
                        {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t1Hour = hourOfDay;
                                t1Minute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,t1Hour,t1Minute);
                                String hourString = Integer.toString(hourOfDay);
                                String minuteString;
                                if(minute<10)
                                    minuteString = "0"+Integer.toString(minute);
                                else
                                    minuteString = Integer.toString(minute);
                                hoursText.setText(hourString+":"+minuteString);
                            }
                        }
                        , 12,0,true);
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.show();
            }
        });

        confirm_settingsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                checkGender(v);
                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Height",height_TV.getText().toString());
                editor.putString("Age",age_TV.getText().toString());

                String radioText = (String) radioButton.getText();
                String womenText = getString(R.string.women_text);
                if(radioText==womenText)
                {
                    editor.putString("Gender","Kobieta");
                }
                else
                {
                    editor.putString("Gender","Mężczyzna");
                }
                changeBackground(v);
//notification | powiadomienie
                Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                if(switch_notification.isChecked())
                {
                    String[] hour_minute = hoursText.getText().toString().split(":");
                    Integer hours = Integer.parseInt(hour_minute[0]);
                    Integer minute = Integer.parseInt(hour_minute[1]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hours);
                    calendar.set(Calendar.MINUTE, minute);

                    intent.setAction("MY_NOTIFICATION_MESSAGE");

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY, pendingIntent);

                    //wpisanie ustawien alarmu do ustawień użytkownika
                    editor.putBoolean("Notification",true);
                    String minute_txt, hour_txt;
                    if(minute<10)
                        minute_txt = "0"+Integer.toString(minute);
                    else
                        minute_txt = Integer.toString(minute);
                    if(hours<10)
                        hour_txt = "0"+Integer.toString(hours);
                    else
                        hour_txt = Integer.toString(hours);
                    editor.putString("Hour", hour_txt);
                    editor.putString("Minute", minute_txt);
                }
                else //rezygrnacja z powiadomien
                {
                    editor.putBoolean("Notification",false);
                    alarmManager.cancel(pendingIntent);
                }


                editor.commit();
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openChartActivity();
            }
        });


    }

    public void openMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openChartActivity()
    {
        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }

    public void checkGender(View v)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    public void changeBackground(View v)
    {
        View someView = findViewById(R.id.ConstraintLayout);
        View root = someView.getRootView();
        String radioText = (String) radioButton.getText();

        if(radioText.charAt(0)=='K')
        {
            ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.lightPink));
        }
        if(radioText.charAt(0)=='M')
        {
            ((RadioButton)radioGroup.getChildAt(1)).setChecked(true);
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBlue));
        }
    }
}



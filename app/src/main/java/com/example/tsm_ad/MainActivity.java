package com.example.tsm_ad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button, button2, buttonOk;
    private EditText weight_editText;
    private TextView bmi_text, bmi_bodyTV, bmi_warning;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        buttonOk = (Button)findViewById(R.id.buttonOk);
        weight_editText = (EditText)findViewById(R.id.weightEditText);
        bmi_text = (TextView)findViewById(R.id.bmi_TextView);
        bmi_bodyTV = findViewById(R.id.bmi_bodyTV);
        bmi_warning = findViewById(R.id.bmiWarning);



        View someView = findViewById(R.id.ConstraintLayoutM);
        View root = someView.getRootView();

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String text = settings.getString("Gender", "M").toString();

        if(text.charAt(0)=='K')
        {
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.lightPink));
        }
        if(text.charAt(0)=='M')
        {
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBlue));
        }
        //zapisywanie dzisiejszej wagi
        String days = settings.getString("Days", "").toString();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String day_txt = Integer.toString(day);
        List<String> daysList = new ArrayList<String>(Arrays.asList(days.split(",")));
        String weights = settings.getString("Weights", "").toString();
        List<String> weightsList = new ArrayList<String>(Arrays.asList(weights.split(",")));
        if(daysList.size()>0)
        {
            if(day_txt.equals(daysList.get(daysList.size()-1)))
                weight_editText.setText(weightsList.get(weightsList.size()-1));//
            else
                weight_editText.setText("");
        }
        //zapisywanie dzisiejszego BMI
        if(!weight_editText.getText().toString().isEmpty())
        {
            Double weight_value = (Double.parseDouble(weight_editText.getText().toString()));
            Double body_size = Double.valueOf(settings.getString("Height", "").toString()) / 100.0;
            Double body_size2 = body_size * body_size;
            Integer age = Integer.parseInt(settings.getString("Age", "").toString());
            Double bmi_value = (weight_value / body_size2);
            int temp = (int) (bmi_value * 10.0);
            Double shortDouble = ((double) temp) / 10.0;
            bmi_text.setText(shortDouble.toString());

            if(age<20)
                bmi_warning.setText("Wskaźnik BMI nie jest wskazany dla dzieci i młodzieży");

            if (shortDouble < 16.0)
                bmi_bodyTV.setText("wygłodzenie");
            if (shortDouble >= 16.0 && shortDouble < 17.0)
                bmi_bodyTV.setText("wychudzenie");
            if (shortDouble >= 17.0 && shortDouble < 18.5)
                bmi_bodyTV.setText("niedowaga");
            if (shortDouble >= 18.5 && shortDouble < 25.0)
                bmi_bodyTV.setText("optymalna masa ciała");
            if (shortDouble >= 25.0 && shortDouble < 30.0)
                bmi_bodyTV.setText("nadwaga");
            if (shortDouble >= 30.0 && shortDouble < 35.0)
                bmi_bodyTV.setText("otyłość I stopnia");
            if (shortDouble >= 35.0 && shortDouble < 40.0)
                bmi_bodyTV.setText("otyłość II stopnia");
            if (shortDouble >= 40.0)
                bmi_bodyTV.setText("otyłość III stopnia");
        }

        //

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openOptionsActivity();
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openChartActivity();
            }
        });


        buttonOk.setOnClickListener(new View.OnClickListener() {
            SharedPreferences settings = getSharedPreferences("UserInfo", 0);
            @Override
            public void onClick(View v) {
                Double weight_value = (Double.parseDouble(weight_editText.getText().toString()));
                Double body_size = Double.valueOf(settings.getString("Height", "").toString()) / 100.0;
                Double body_size2 = body_size * body_size;
                Integer age = Integer.parseInt(settings.getString("Age", "").toString());
                Double bmi_value = (weight_value / body_size2);
                int temp = (int) (bmi_value * 10.0);
                Double shortDouble = ((double) temp) / 10.0;
                bmi_text.setText(shortDouble.toString());

                if(age<20)
                    bmi_warning.setText("Wskaźnik BMI nie jest wskazany dla dzieci i młodzieży");

                if (shortDouble < 16.0)
                    bmi_bodyTV.setText("wygłodzenie");
                if (shortDouble >= 16.0 && shortDouble < 17.0)
                    bmi_bodyTV.setText("wychudzenie");
                if (shortDouble >= 17.0 && shortDouble < 18.5)
                    bmi_bodyTV.setText("niedowaga");
                if (shortDouble >= 18.5 && shortDouble < 25.0)
                    bmi_bodyTV.setText("optymalna masa ciała");
                if (shortDouble >= 25.0 && shortDouble < 30.0)
                    bmi_bodyTV.setText("nadwaga");
                if (shortDouble >= 30.0 && shortDouble < 35.0)
                    bmi_bodyTV.setText("otyłość I stopnia");
                if (shortDouble >= 35.0 && shortDouble < 40.0)
                    bmi_bodyTV.setText("otyłość II stopnia");
                if (shortDouble >= 40.0)
                    bmi_bodyTV.setText("otyłość III stopnia");

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String day_txt = Integer.toString(day);

                String days = settings.getString("Days", "").toString();
                String weights = settings.getString("Weights", "").toString();

                SharedPreferences.Editor editor = settings.edit();
                //days = "1,2,3,4,5,6,7,8,9,10,";
                //weights = "71.1,71.2,71.4,71.3,71.4,71.0,71.0,70.8,71.2,71.5,";

                if(days.equals(""))
                {
                    editor.putString("Days", day_txt);
                    editor.putString("Weights", weight_editText.getText().toString());
                }
                else
                {
                    List<String> daysList = new ArrayList<String>(Arrays.asList(days.split(",")));
                    List<String> weightsList = new ArrayList<String>(Arrays.asList(weights.split(",")));
                    String lastDay = daysList.get(daysList.size() - 1);
                    Integer lastDayInt = Integer.parseInt(daysList.get(daysList.size() - 1));
                    if(lastDayInt>day)
                    {
                        daysList.clear();
                        weightsList.clear();
                    }
                    if (lastDay.equals(day_txt))
                        weightsList.set(daysList.size() - 1, weight_value.toString());
                    else
                    {
                        weightsList.add(weight_value.toString());
                        daysList.add(day_txt);
                    }
                    StringBuilder csvList = new StringBuilder();
                    StringBuilder csvList2 = new StringBuilder();
                    for(String s : daysList)
                    {
                        csvList.append(s);
                        csvList.append(",");
                    }
                    for(String s : weightsList)
                    {
                        csvList2.append(s);
                        csvList2.append(",");
                    }


                    editor.putString("Days", csvList.toString());
                    editor.putString("Weights", csvList2.toString());
                }


                //editor.putString("Days", day_txt);
                //editor.putString("Days", "");
                //editor.putString("Weights", "");
                editor.commit();
            }
        });


    }

    public void openOptionsActivity()
    {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void openChartActivity()
    {
        Intent intent = new Intent(this, ChartActivity.class);
        startActivity(intent);
    }
}
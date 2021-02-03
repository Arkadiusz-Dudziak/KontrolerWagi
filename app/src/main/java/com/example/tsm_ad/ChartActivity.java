package com.example.tsm_ad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//implements OnChartGestureListener, OnChartValueSelectedListener
public class ChartActivity extends AppCompatActivity {
    Button button, button2;
    private LineChart mChart, mChartBMI;
    private static final String TAG = "ChartActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        button = (Button)findViewById(R.id.optionsC);
        button2 = (Button)findViewById(R.id.mainC);
        mChart = (LineChart)findViewById(R.id.linearChart);
        mChartBMI = (LineChart)findViewById(R.id.linearChart2);
        mChart.getDescription().setEnabled(false); mChartBMI.getDescription().setEnabled(false);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String text = settings.getString("Gender", "").toString();
        String days = settings.getString("Days", "").toString();
        //days = "1,2,3,4,5,6,7,8,9,10";

        String weights = settings.getString("Weights", "").toString();
        //weights = "71.1,71.2,71.4,71.3,71.4,71.0,71.0,70.8,71.2,71.5";
        Float height = Float.parseFloat(settings.getString("Height", "").toString());

        //mChart.setOnChartGestureListener(ChartActivity.this);
        //mChart.setOnChartValueSelectedListener(ChartActivity.this);
        mChart.setDragEnabled(true);    mChartBMI.setDragEnabled(true);
        mChart.setScaleEnabled(false);  mChartBMI.setScaleEnabled(false);

        AxisBase xAxis = mChart.getXAxis(); AxisBase xAxisBMI = mChartBMI.getXAxis();
        xAxis.setGranularity(1f);           xAxisBMI.setGranularity(1f);
        YAxis leftAxis = mChart.getAxisLeft();
//data for BMI
        Double body_size = Double.valueOf(settings.getString("Height", "").toString()) / 100.0;
        Double body_size2 = body_size * body_size;
        Integer age = Integer.parseInt(settings.getString("Age", "").toString());
        /*Double bmi_value = (weight_value / body_size2);
        int temp = (int) (bmi_value * 10.0);
        Double shortDouble = ((double) temp) / 10.0;
        bmi_text.setText(shortDouble.toString());*/
        //
//LineChart
        if(!days.equals("")&&!weights.equals(""))
        {
            List<String> daysList = new ArrayList<String>(Arrays.asList(days.split(",")));
            List<String> weightsList = new ArrayList<String>(Arrays.asList(weights.split(",")));
            //Float left_min=Float.valueOf(1), left_max=Float.valueOf(100);
            ArrayList<Entry> yValues = new ArrayList<>();
            ArrayList<Entry> yValuesBMI = new ArrayList<>();
            double weight, bmi_value;
            //Float weight;
            float weightF;

            for(int i=0; i<daysList.size();i++)//waga
            {
                yValues.add(new Entry(Integer.parseInt(daysList.get(i)),Float.parseFloat(weightsList.get(i))));
                weight = (double)Float.parseFloat(weightsList.get(i));
                weight=weight/body_size2;
                weightF=(float)weight;
                yValuesBMI.add(new Entry(Integer.parseInt(daysList.get(i)), weightF));
            }


            LineDataSet set1 = new LineDataSet(yValues, "Waga");
            LineDataSet set1BMI = new LineDataSet(yValuesBMI, "BMI");

            set1.setFillAlpha(110);         set1BMI.setFillAlpha(110);
            set1.setColor(Color.BLACK);     set1BMI.setColor(Color.BLUE);
            set1.setLineWidth(4f);          set1BMI.setLineWidth(4f);
            set1.setValueTextSize(0f);     set1BMI.setValueTextSize(0f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            ArrayList<ILineDataSet> dataSetsBMI = new ArrayList<>();
            dataSets.add(set1);
            dataSetsBMI.add(set1BMI);

            LineData data = new LineData(dataSets);
            LineData dataBMI = new LineData(dataSetsBMI);

            mChart.setData(data);   mChartBMI.setData(dataBMI);
        }
        else
        {
            mChart.setNoDataText("Wprowadź wagę, na ekranie głównym, by zobaczyć wykres");
            mChartBMI.setNoDataText("Wprowadź wagę, na ekranie głównym, by zobaczyć wykres");
            mChart.setNoDataTextColor(Color.RED);   mChartBMI.setNoDataTextColor(Color.RED);
        }


        View someView = findViewById(R.id.contrainLayoutC);
        View root = someView.getRootView();


        if(text.charAt(0)=='K')
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.lightPink));
        if(text.charAt(0)=='M')
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBlue));

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openOptionsActivity();
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openOptionsActivity()
    {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void openMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
/*
Copyright 2020 Philipp Jahoda

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
* */
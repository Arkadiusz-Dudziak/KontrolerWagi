package com.example.tsm_ad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Boolean firstTime;
    EditText height_TV, age_TV;
    TextView errorTV;
    RadioGroup radioGroup;
    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedPreferences = getSharedPreferences("UserInfo", 0);
        firstTime = sharedPreferences.getBoolean("firstTime", true);
        radioGroup = findViewById(R.id.radioGroup);
        height_TV = findViewById(R.id.height_EditText);
        age_TV = findViewById(R.id.age_EditText);
        errorTV = findViewById(R.id.errorTV);


        if(!firstTime)
        {
            Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }
    public void settingsConfirm(View view) {
        if (TextUtils.isEmpty(height_TV.getText()) || TextUtils.isEmpty(age_TV.getText()) || radioGroup.getCheckedRadioButtonId() == -1)
        {
            errorTV.setText("Nie wszystkie pola zostały wypełnione");
        } else
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            firstTime = false;
            editor.putBoolean("firstTime", firstTime);

            editor.putString("Height",height_TV.getText().toString());
            editor.putString("Age",age_TV.getText().toString());


            String radioText = (String) radioButton.getText();
            String womenText = getString(R.string.women_text);

            if(radioText==womenText)
                editor.putString("Gender","Kobieta");
            else
                editor.putString("Gender","Mężczyzna");

            editor.commit();
            Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }
    public void checkGender(View v)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }
}
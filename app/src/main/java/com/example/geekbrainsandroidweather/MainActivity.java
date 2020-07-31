package com.example.geekbrainsandroidweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView temperature;
    private Button increaseTemperatureBtn;
    private Button settingsBtn;
    private Button citiesBtn;
    private final String counterDataKey = "counterDataKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setOnIncreaseTemperatureBtnBehaviour();
        setOnBtnClickBehaviour(citiesBtn, CitiesActivity.class);
        setOnBtnClickBehaviour(settingsBtn, SettingsActivity.class);
    }

    private void init() {
        temperature = findViewById(R.id.temperature);
        settingsBtn = findViewById(R.id.settingsBtn);
        citiesBtn = findViewById(R.id.citiesBtn);
        increaseTemperatureBtn = findViewById(R.id.increaseTemperatureBtn);
    }

    private void setOnBtnClickBehaviour(Button button, final Class activity) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity);
                startActivity(intent);
            }
        });
    }

    private void setOnIncreaseTemperatureBtnBehaviour() {
        increaseTemperatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentTemperature = Integer.parseInt(temperature.getText().toString()
                        .replace("°", ""));
                String newTemperature = ++currentTemperature + "°";
                temperature.setText(newTemperature);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        String text = temperature.getText().toString();
        saveInstanceState.putString(counterDataKey, text);
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String text = savedInstanceState.getString(counterDataKey);
        temperature.setText(text);
    }
}

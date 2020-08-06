package com.example.geekbrainsandroidweather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView temperature;
    private TextView city;
    private Button increaseTemperatureBtn;
    private Button settingsBtn;
    private Button citiesBtn;
    private Button openUrlBtn;
    private TextView humidityTextView;
    private TextView windSpeedTextView;
    private TextView pressureTextView;
    private final String dataKey = "counterDataKey";
    private final int requestCode = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setOnIncreaseTemperatureBtnBehaviour();
        setOnBtnClickBehaviour(citiesBtn, CitiesActivity.class);
        setOnBtnClickBehaviour(settingsBtn, SettingsActivity.class);
        setOnOpenUrlBtnClick();
    }

    // инициализация views
    private void init() {
        city = findViewById(R.id.city);
        temperature = findViewById(R.id.temperature);
        settingsBtn = findViewById(R.id.settingsBtn);
        citiesBtn = findViewById(R.id.citiesBtn);
        increaseTemperatureBtn = findViewById(R.id.increaseTemperatureBtn);
        openUrlBtn = findViewById(R.id.openUrl);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
    }

    // увеличение температуры
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

    // слушатель события на кнопку
    private void setOnBtnClickBehaviour(Button button, final Class activity) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, activity);
                startActivityForResult(intent, requestCode);
            }
        });
    }

    // получение данных из других активити
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && resultCode == RESULT_OK && data != null) {
            String cityData = data.getStringExtra(CitiesActivity.cityDataKey);
            if (cityData != null) city.setText(cityData);
            String temperatureData = data.getStringExtra(SettingsActivity.temperatureDataKey);
            if (temperatureData != null) temperature.setText(temperatureData);

            boolean isPressureVisible = data.getBooleanExtra(SettingsActivity.pressureDataKey, true);
            setParameterVisibility(isPressureVisible, pressureTextView);

            boolean isHumidityVisible = data.getBooleanExtra(SettingsActivity.humidityDataKey, true);
            setParameterVisibility(isHumidityVisible, humidityTextView);

            boolean isWindSpeedVisible = data.getBooleanExtra(SettingsActivity.windSpeedDataKey, true);
            setParameterVisibility(isWindSpeedVisible, windSpeedTextView);
        }
    }

    // установка видимости TextView с параметрами погоды
    private void setParameterVisibility(boolean isVisible, TextView textView) {
        if (!isVisible) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    // сохранение данных текущей активити
    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState) {
        String text = temperature.getText().toString();
        saveInstanceState.putString(dataKey, text);
        super.onSaveInstanceState(saveInstanceState);
    }

    // восстановление данных текущей активити
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String text = savedInstanceState.getString(dataKey);
        temperature.setText(text);
    }

    // запуск браузера
    private void setOnOpenUrlBtnClick() {
        openUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getString(R.string.yandex_pogoda));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}

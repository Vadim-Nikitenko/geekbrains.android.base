package com.example.geekbrainsandroidweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private EditText temperatureEditText;
    private Button applySettingsBtn;
    private CheckBox pressureCheckbox;
    private CheckBox windSpeedCheckbox;
    private CheckBox humidityCheckbox;
    public final static String temperatureDataKey = "temperatureDataKey";
    public final static String pressureDataKey = "pressureDataKey";
    public final static String windSpeedDataKey = "windSpeedDataKey";
    public final static String humidityDataKey = "humidityDataKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        showBackBtn();
        setOnApplySettingsBtnClick();
    }

    // инициализация views
    private void init() {
        temperatureEditText = findViewById(R.id.temperatureEditText);
        applySettingsBtn = findViewById(R.id.applySettingsBtn);
        pressureCheckbox = findViewById(R.id.pressureCheckbox);
        windSpeedCheckbox = findViewById(R.id.windSpeedCheckbox);
        humidityCheckbox = findViewById(R.id.humidityCheckbox);
    }

    // сохранение данных для другой активити и finish текущей активити
    private void setOnApplySettingsBtnClick() {
        applySettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dataIntent = new Intent();

                String temperatureData = temperatureEditText.getText().toString();
                if (!temperatureData.equals(""))
                dataIntent.putExtra(temperatureDataKey, temperatureData);

                dataIntent.putExtra(pressureDataKey, pressureCheckbox.isChecked());
                dataIntent.putExtra(humidityDataKey, humidityCheckbox.isChecked());
                dataIntent.putExtra(windSpeedDataKey, windSpeedCheckbox.isChecked());

                setResult(RESULT_OK, dataIntent);
                finish();
            }
        });
    }

    // показ onBackPressed в меню
    private void showBackBtn() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    // finish текущей активити по клику на onBackPressed в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}


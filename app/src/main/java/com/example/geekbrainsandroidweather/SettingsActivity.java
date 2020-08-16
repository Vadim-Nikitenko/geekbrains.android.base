package com.example.geekbrainsandroidweather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private EditText temperatureEditText;
    private CheckBox pressureCheckbox;
    private CheckBox windSpeedCheckbox;
    private CheckBox humidityCheckbox;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch darkThemeSwitch;
    public final static String temperatureDataKey = "temperatureDataKey";
    public final static String pressureDataKey = "pressureDataKey";
    public final static String windSpeedDataKey = "windSpeedDataKey";
    public final static String humidityDataKey = "humidityDataKey";
    public static boolean isDarkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_settings);
        init();
        showBackBtn();
        setOnDarkThemeSwitchBehaviour();
    }


    private void setOnDarkThemeSwitchBehaviour() {
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isDarkTheme = b;
                recreate();
            }
        });
    }

    private void setTheme() {
        if (isDarkTheme) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    // инициализация views
    private void init() {
        temperatureEditText = findViewById(R.id.temperatureEditText);
        pressureCheckbox = findViewById(R.id.pressureCheckbox);
        windSpeedCheckbox = findViewById(R.id.windSpeedCheckbox);
        humidityCheckbox = findViewById(R.id.humidityCheckbox);
        darkThemeSwitch = findViewById(R.id.darkThemeSwitch);
        darkThemeSwitch.setChecked(isDarkTheme);
    }

    // показ onBackPressed в меню
    private void showBackBtn() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    // сохранение данных для другой активити и finish текущей активити
    // finish текущей активити по клику на onBackPressed в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent dataIntent = new Intent();
//            dataIntent.setClass(getApplicationContext(), MainActivity.class);
            dataIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            String temperatureData = temperatureEditText.getText().toString();
            Bundle bundle = new Bundle();
            if (!temperatureData.equals("")) {
                dataIntent.putExtra(temperatureDataKey, temperatureData);
            }
            dataIntent.putExtra(pressureDataKey, pressureCheckbox.isChecked());
            dataIntent.putExtra(humidityDataKey, humidityCheckbox.isChecked());
            dataIntent.putExtra(windSpeedDataKey, windSpeedCheckbox.isChecked());
            setResult(RESULT_OK, dataIntent);
            dataIntent.putExtras(bundle);
            isDarkTheme = darkThemeSwitch.isChecked();
            finish();
//            startActivity(dataIntent);
        }
        return true;
    }
}


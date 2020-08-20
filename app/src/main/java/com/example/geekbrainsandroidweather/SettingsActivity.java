package com.example.geekbrainsandroidweather;

import android.annotation.SuppressLint;
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
    public static boolean isPressureChecked = true;
    public static boolean isHumidityChecked = true;
    public static boolean isWindSpeedChecked = true;
    public static boolean isDarkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_settings);
        init();
        showBackBtn();
        setCheckBoxesBehaviour();
        setOnDarkThemeSwitchBehaviour();
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

    // finish текущей активити по клику на onBackPressed в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            isPressureChecked = pressureCheckbox.isChecked();
            isHumidityChecked = humidityCheckbox.isChecked();
            isWindSpeedChecked = windSpeedCheckbox.isChecked();
            finish();
        }
        return true;
    }

    private void setCheckBoxesBehaviour() {

        pressureCheckbox.setChecked(isPressureChecked);
        windSpeedCheckbox.setChecked(isWindSpeedChecked);
        humidityCheckbox.setChecked(isHumidityChecked);

        pressureCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isPressureChecked = b;
            }
        });
        windSpeedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isWindSpeedChecked = b;
            }
        });
        humidityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isHumidityChecked = b;
            }
        });
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
}


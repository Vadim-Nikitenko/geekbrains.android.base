package com.example.geekbrainsandroidweather.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.geekbrainsandroidweather.MainActivity;
import com.example.geekbrainsandroidweather.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private CheckBox pressureCheckbox;
    private CheckBox windSpeedCheckbox;
    private CheckBox humidityCheckbox;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public static boolean isPressureChecked = true;
    public static boolean isHumidityChecked = true;
    public static boolean isWindSpeedChecked = true;
    public static boolean isDarkTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setCheckBoxesBehaviour();
    }

    // инициализация views
    private void init(@NonNull View view) {
        pressureCheckbox = view.findViewById(R.id.pressureCheckbox);
        windSpeedCheckbox = view.findViewById(R.id.windSpeedCheckbox);
        humidityCheckbox = view.findViewById(R.id.humidityCheckbox);
    }

    // показ onBackPressed в меню

    // finish текущей активити по клику на onBackPressed в меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            isPressureChecked = pressureCheckbox.isChecked();
            isHumidityChecked = humidityCheckbox.isChecked();
            isWindSpeedChecked = windSpeedCheckbox.isChecked();
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


}


package ru.kiradev.weather.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.kiradev.weather.R;

public class SettingsFragment extends Fragment implements Constants {
    private CheckBox pressureCheckbox;
    private CheckBox windSpeedCheckbox;
    private CheckBox humidityCheckbox;
    public static boolean isPressureChecked;
    public static boolean isHumidityChecked;
    public static boolean isWindSpeedChecked;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setCheckBoxesBehaviour();
        requireActivity().findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
    }

    private void init(@NonNull View view) {
        pressureCheckbox = view.findViewById(R.id.pressureCheckbox);
        windSpeedCheckbox = view.findViewById(R.id.windSpeedCheckbox);
        humidityCheckbox = view.findViewById(R.id.humidityCheckbox);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        isPressureChecked = sharedPref.getBoolean(KEY_PRESSURE, true);
        isHumidityChecked = sharedPref.getBoolean(KEY_HUMIDITY, true);
        isWindSpeedChecked = sharedPref.getBoolean(KEY_WIND_SPEED, true);
    }

    private void setCheckBoxesBehaviour() {
        pressureCheckbox.setChecked(isPressureChecked);
        windSpeedCheckbox.setChecked(isWindSpeedChecked);
        humidityCheckbox.setChecked(isHumidityChecked);

        pressureCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isPressureChecked = b;
                saveToPreferences(KEY_PRESSURE, isPressureChecked);
            }
        });
        windSpeedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isWindSpeedChecked = b;
                saveToPreferences(KEY_WIND_SPEED, isWindSpeedChecked);
            }
        });
        humidityCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isHumidityChecked = b;
                saveToPreferences(KEY_HUMIDITY, isHumidityChecked);
            }
        });
    }

    private void saveToPreferences(String key, boolean bool) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, bool);
        editor.apply();
    }
}
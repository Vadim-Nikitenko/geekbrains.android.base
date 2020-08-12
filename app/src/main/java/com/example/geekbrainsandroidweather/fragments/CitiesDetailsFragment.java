package com.example.geekbrainsandroidweather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.SettingsActivity;
import com.example.geekbrainsandroidweather.model.CityDetailsData;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class CitiesDetailsFragment extends Fragment {
    private static TextView city;
    private TextView settingsTextView;
    private TextView temperature;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView windSpeedTextView;
    private TextView stateTextView;
    private TextView dayAndNightTemperatureTextView;
    private final int requestCode = 12345;
    private static boolean isPressureVisible;
    private static boolean isHumidityVisible;
    private static boolean isWindSpeedVisible;


    static CitiesDetailsFragment create(CityDetailsData cityDetails) {
        CitiesDetailsFragment citiesDetailsFragment = new CitiesDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("index", cityDetails);
        bundle.putBoolean(SettingsActivity.pressureDataKey, isPressureVisible);
        bundle.putBoolean(SettingsActivity.humidityDataKey, isHumidityVisible);
        bundle.putBoolean(SettingsActivity.windSpeedDataKey, isWindSpeedVisible);
        citiesDetailsFragment.setArguments(bundle);
        return citiesDetailsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setParameterVisibility(savedInstanceState.getBoolean(SettingsActivity.pressureDataKey), pressureTextView);
            setParameterVisibility(savedInstanceState.getBoolean(SettingsActivity.humidityDataKey), humidityTextView);
            setParameterVisibility(savedInstanceState.getBoolean(SettingsActivity.windSpeedDataKey), windSpeedTextView);
        }
        return inflater.inflate(R.layout.fragment_cities_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setOnSettingsClickBehaviour();
        getSettingParameters();
        setCityParameters();
    }

    private void setCityParameters() {
        city.setText(getCityName());
        temperature.setText(getTemperature());
        stateTextView.setText(getState());
        dayAndNightTemperatureTextView.setText(getDayAndNightTemperature());
    }

    private void initViews(@NonNull View view) {
        settingsTextView = view.findViewById(R.id.settingsTextView);
        city = view.findViewById(R.id.city);
        temperature = view.findViewById(R.id.temperature);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        windSpeedTextView = view.findViewById(R.id.windSpeedTextView);
        stateTextView = view.findViewById(R.id.weatherState);
        dayAndNightTemperatureTextView = view.findViewById(R.id.dayNightTemperature);
    }

    // слушатель события на кнопку
    private void setOnSettingsClickBehaviour() {
        settingsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });
    }

    // получение данных из других активити
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && resultCode == RESULT_OK && data != null) {
            String temperatureData = data.getStringExtra(SettingsActivity.temperatureDataKey);
            if (temperatureData != null) temperature.setText(temperatureData);

            isPressureVisible = data.getBooleanExtra(SettingsActivity.pressureDataKey, true);
            setParameterVisibility(isPressureVisible, pressureTextView);

            isHumidityVisible = data.getBooleanExtra(SettingsActivity.humidityDataKey, true);
            setParameterVisibility(isHumidityVisible, humidityTextView);

            isWindSpeedVisible = data.getBooleanExtra(SettingsActivity.windSpeedDataKey, true);
            setParameterVisibility(isWindSpeedVisible, windSpeedTextView);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(SettingsActivity.pressureDataKey, isPressureVisible);
        outState.putBoolean(SettingsActivity.humidityDataKey, isHumidityVisible);
        outState.putBoolean(SettingsActivity.windSpeedDataKey, isWindSpeedVisible);
        super.onSaveInstanceState(outState);
    }

    // установка видимости TextView с параметрами погоды
    private void setParameterVisibility(boolean isVisible, TextView textView) {
        if (textView != null) {
            if (!isVisible) {
                textView.setVisibility(View.INVISIBLE);
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    int getIndex() {
        CityDetailsData cityDetailsData = (CityDetailsData) Objects.requireNonNull(getArguments())
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    private String getCityName() {
        CityDetailsData cityDetailsData = (CityDetailsData) Objects.requireNonNull(getArguments())
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getCityName();
        } catch (Exception e) {
            return "";
        }
    }

    private String getTemperature() {
        CityDetailsData cityDetailsData = (CityDetailsData) Objects.requireNonNull(getArguments())
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getTemperature() + "°";
        } catch (Exception e) {
            return "0";
        }
    }

    private String getState() {
        CityDetailsData cityDetailsData = (CityDetailsData) Objects.requireNonNull(getArguments())
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getState();
        } catch (Exception e) {
            return "";
        }
    }

    private String getDayAndNightTemperature() {
        CityDetailsData cityDetailsData = (CityDetailsData) Objects.requireNonNull(getArguments())
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getDayAndNightTemperature();
        } catch (Exception e) {
            return "";
        }
    }

    private void getSettingParameters() {
        setParameterVisibility(getArguments().getBoolean(SettingsActivity.pressureDataKey), pressureTextView);
        setParameterVisibility(getArguments().getBoolean(SettingsActivity.humidityDataKey), humidityTextView);
        setParameterVisibility(getArguments().getBoolean(SettingsActivity.windSpeedDataKey), windSpeedTextView);
    }

}

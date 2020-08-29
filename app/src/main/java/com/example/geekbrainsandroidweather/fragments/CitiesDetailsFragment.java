package com.example.geekbrainsandroidweather.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.model.ForecastDayData;
import com.example.geekbrainsandroidweather.network.OpenWeatherMap;
import com.example.geekbrainsandroidweather.recycler_views.IRVOnItemClick;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerDataAdapter;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerForecastAdapter;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerHourlyDataAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CitiesDetailsFragment extends Fragment implements IRVOnItemClick, Constants {
    private TextView city;
    private TextView temperature;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView windSpeedTextView;
    private TextView stateTextView;
    private TextView dayAndNightTemperatureTextView;
    private TextView weatherMainState;
    private RecyclerView recyclerForecastView;
    private RecyclerView recyclerHourlyView;
    private ImageView weatherStateImg;
    private TextView lastUpdateTextView;

    public static CitiesDetailsFragment create(CityDetailsData cityDetails) {
        CitiesDetailsFragment citiesDetailsFragment = new CitiesDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CITIES_DETAILS_INDEX, cityDetails);
        citiesDetailsFragment.setArguments(bundle);
        return citiesDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupRecyclerView();
        setupHourlyRecyclerView();
        getSettingParameters();
        setCityParameters();
        requireActivity().findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
    }

    private void init(@NonNull View view) {
        city = view.findViewById(R.id.city);
        temperature = view.findViewById(R.id.temperature);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        windSpeedTextView = view.findViewById(R.id.windSpeedTextView);
        stateTextView = view.findViewById(R.id.weatherState);
        dayAndNightTemperatureTextView = view.findViewById(R.id.dayNightTemperature);
        weatherMainState = view.findViewById(R.id.weatherMainState);
        recyclerForecastView = view.findViewById(R.id.recyclerForecastView);
        weatherStateImg = view.findViewById(R.id.weatherStateImg);
        recyclerHourlyView = view.findViewById(R.id.recyclerHourlyView);
        lastUpdateTextView = view.findViewById(R.id.lastUpdateTextView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupRecyclerView() {
        if (OpenWeatherMap.responseCode == 200) {
            ArrayList<ForecastDayData> forecastDayData = OpenWeatherMap.weatherForTheWeek;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            DividerItemDecoration decorator = new DividerItemDecoration(requireContext(),
                    LinearLayoutManager.VERTICAL);
            decorator.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.decorator_item)));
            RecyclerForecastAdapter recyclerForecastAdapter = new RecyclerForecastAdapter(forecastDayData);
            recyclerForecastView.setLayoutManager(linearLayoutManager);
            recyclerForecastView.addItemDecoration(decorator);
            recyclerForecastView.setAdapter(recyclerForecastAdapter);
        }
    }

    private void setupHourlyRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerHourlyDataAdapter recyclerHourlyDataAdapter = new RecyclerHourlyDataAdapter(OpenWeatherMap.hourlyForecastList);
        recyclerHourlyView.setLayoutManager(linearLayoutManager);
        recyclerHourlyView.setAdapter(recyclerHourlyDataAdapter);
    }

    private void getSettingParameters() {
        setParameterVisibility(SettingsFragment.isPressureChecked, pressureTextView);
        setParameterVisibility(SettingsFragment.isHumidityChecked, humidityTextView);
        setParameterVisibility(SettingsFragment.isWindSpeedChecked, windSpeedTextView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSettingParameters();
    }

    private void setCityParameters() {
        CityDetailsData cityDetailsData = OpenWeatherMap.getCityDetailsData();

        city.setText(Objects.requireNonNull(cityDetailsData).getCityName());
        temperature.setText(cityDetailsData.getTemperature());
        stateTextView.setText(cityDetailsData.getState());
        humidityTextView.setText(cityDetailsData.getHumidity());
        pressureTextView.setText(cityDetailsData.getPressure());
        windSpeedTextView.setText(cityDetailsData.getWindSpeed());
        weatherMainState.setText(cityDetailsData.getWeatherMainState());
        dayAndNightTemperatureTextView.setText(cityDetailsData.getDayAndNightTemperature());
        Picasso.get().load(cityDetailsData.getIcon()).into(weatherStateImg);


        Calendar currentTime = Calendar.getInstance();
        String lastUpdate = getString(R.string.udpated_at_text) + currentTime.get(Calendar.HOUR)
                + ":" + currentTime.get(Calendar.MINUTE);
        lastUpdateTextView.setText(lastUpdate);
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

    @Override
    public void onItemClicked(View view, int position) {

    }

    @Override
    public void onItemLongPressed(View view) {

    }

    @Override
    public void changeItem(TextView view) {

    }

}

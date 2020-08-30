package com.example.geekbrainsandroidweather.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.custom_views.HumidityView;
import com.example.geekbrainsandroidweather.custom_views.PressureView;
import com.example.geekbrainsandroidweather.custom_views.ThermometerView;
import com.example.geekbrainsandroidweather.custom_views.WindView;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.model.ForecastDayData;
import com.example.geekbrainsandroidweather.network.OpenWeatherMap;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerForecastAdapter;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerHourlyDataAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CitiesDetailsFragment extends Fragment implements Constants {
    private TextView city;
    private TextView temperature;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView stateTextView;
    private TextView dayAndNightTemperatureTextView;
    private TextView weatherMainState;
    private RecyclerView recyclerForecastView;
    private RecyclerView recyclerHourlyView;
    private ImageView weatherStateImg;
    private TextView lastUpdateTextView;
    private TextView feelsLikeTextView;
    private HumidityView humidityCustomView;
    private WindView windCustomView;
    private PressureView pressureCustomView;
    private ThermometerView thermometerView;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        stateTextView = view.findViewById(R.id.weatherState);
        dayAndNightTemperatureTextView = view.findViewById(R.id.dayNightTemperature);
        weatherMainState = view.findViewById(R.id.weatherMainState);
        recyclerForecastView = view.findViewById(R.id.recyclerForecastView);
        weatherStateImg = view.findViewById(R.id.weatherStateImg);
        recyclerHourlyView = view.findViewById(R.id.recyclerHourlyView);
        lastUpdateTextView = view.findViewById(R.id.lastUpdateTextView);
        feelsLikeTextView = view.findViewById(R.id.feelsLikeTextView);
        thermometerView = view.findViewById(R.id.thermometerView);
        humidityCustomView = view.findViewById(R.id.humidityCustomView);
        windCustomView = view.findViewById(R.id.windCustomView);
        pressureCustomView = view.findViewById(R.id.pressureCustomView);
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
        setParameterVisibility(SettingsFragment.isPressureChecked, pressureCustomView);
        setParameterVisibility(SettingsFragment.isHumidityChecked, humidityCustomView);
        setParameterVisibility(SettingsFragment.isWindSpeedChecked, windCustomView);
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
        humidityTextView.animate();
        pressureTextView.setText(cityDetailsData.getPressure());
        weatherMainState.setText(cityDetailsData.getWeatherMainState());
        dayAndNightTemperatureTextView.setText(cityDetailsData.getDayAndNightTemperature());
        feelsLikeTextView.setText(cityDetailsData.getFeelsLikeTemperature());
        thermometerView.setCurrentTemp(Integer.parseInt(cityDetailsData.getTemperature().replace("°", "")));
        Picasso.get().load(cityDetailsData.getIcon()).into(weatherStateImg);
        humidityCustomView.setupTexts(cityDetailsData.getHumidity(), cityDetailsData.getSunriseAndSunset());
        windCustomView.setupTexts(cityDetailsData.getWindSpeed(), cityDetailsData.getWindDegrees());
        pressureCustomView.setupTexts(cityDetailsData.getPressure(), cityDetailsData.getCloudy());
        Calendar currentTime = Calendar.getInstance();
        String lastUpdate = getString(R.string.udpated_at_text) + currentTime.get(Calendar.HOUR)
                + ":" + currentTime.get(Calendar.MINUTE);
        lastUpdateTextView.setText(lastUpdate);
    }


    // установка видимости TextView с параметрами погоды
    private void setParameterVisibility(boolean isVisible, View view) {
        if (view != null) {
            if (!isVisible) {
                view.setVisibility(View.GONE);
            } else {
                view.setEnabled(true);
                view.setVisibility(View.VISIBLE);
            }
        }
    }

}

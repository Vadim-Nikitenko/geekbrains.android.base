package com.example.geekbrainsandroidweather.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
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
import com.example.geekbrainsandroidweather.network.OpenWeatherMap;
import com.example.geekbrainsandroidweather.recycler_views.IRVOnItemClick;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerDataAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private RecyclerView recyclerCitiesView;
    private ImageView weatherStateImg;

    static CitiesDetailsFragment create(CityDetailsData cityDetails) {
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
        initViews(view);
        setupRecyclerView();
        getSettingParameters();
        setCityParameters();
        if (requireArguments().getInt(RESPONSE_CODE, 200) != 200) {
            ErrorFragment errorFragment = new ErrorFragment();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.citiesContainer, errorFragment)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSettingParameters();
    }

    private void initViews(@NonNull View view) {
        city = view.findViewById(R.id.city);
        temperature = view.findViewById(R.id.temperature);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        windSpeedTextView = view.findViewById(R.id.windSpeedTextView);
        stateTextView = view.findViewById(R.id.weatherState);
        dayAndNightTemperatureTextView = view.findViewById(R.id.dayNightTemperature);
        weatherMainState = view.findViewById(R.id.weatherMainState);
        recyclerCitiesView = view.findViewById(R.id.recyclerCitiesView);
        weatherStateImg = view.findViewById(R.id.weatherStateImg);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupRecyclerView() {
        if (OpenWeatherMap.responseCode == 200) {
            ArrayList<String> weatherForTheWeek = OpenWeatherMap.weatherForTheWeek;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            DividerItemDecoration decorator = new DividerItemDecoration(requireContext(),
                    LinearLayoutManager.VERTICAL);
            decorator.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.decorator_item)));
            RecyclerDataAdapter recyclerDataAdapter = new RecyclerDataAdapter(weatherForTheWeek, this, this);
            recyclerCitiesView.setLayoutManager(linearLayoutManager);
            recyclerCitiesView.addItemDecoration(decorator);
            recyclerCitiesView.setAdapter(recyclerDataAdapter);
        }
    }

    private void getSettingParameters() {
        setParameterVisibility(SettingsFragment.isPressureChecked, pressureTextView);
        setParameterVisibility(SettingsFragment.isHumidityChecked, humidityTextView);
        setParameterVisibility(SettingsFragment.isWindSpeedChecked, windSpeedTextView);
    }

    private void setCityParameters() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable(CITIES_DETAILS_INDEX);

        city.setText(Objects.requireNonNull(cityDetailsData).getCityName());
        temperature.setText(cityDetailsData.getTemperature());
        stateTextView.setText(cityDetailsData.getState());
        dayAndNightTemperatureTextView.setText(cityDetailsData.getDayAndNightTemperature());
        humidityTextView.setText(cityDetailsData.getHumidity());
        pressureTextView.setText(cityDetailsData.getPressure());
        windSpeedTextView.setText(cityDetailsData.getWindSpeed());
        weatherMainState.setText(cityDetailsData.getWeatherMainState());
        weatherStateImg.setImageURI(Uri.parse(cityDetailsData.getIcon()));
        Picasso.get().load(cityDetailsData.getIcon()).into(weatherStateImg);
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

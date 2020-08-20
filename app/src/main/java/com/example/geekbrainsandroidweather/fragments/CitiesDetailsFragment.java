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

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

public class CitiesDetailsFragment extends Fragment implements IRVOnItemClick {
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
        bundle.putSerializable("index", cityDetails);
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
        if (requireArguments().getInt("ResponseCode", 200) != 200) {
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
        city.setText(getCityName());
        temperature.setText(getTemperature());
        stateTextView.setText(getState());
        dayAndNightTemperatureTextView.setText(getDayAndNightTemperature());
        humidityTextView.setText(getHumidity());
        pressureTextView.setText(getPressure());
        windSpeedTextView.setText(getWindSpeed());
        weatherMainState.setText(getWeatherMainState());
        weatherStateImg.setImageURI(Uri.parse(getIcon()));
        Picasso.get().load(getIcon()).into(weatherStateImg);
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
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    private String getCityName() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getCityName();
        } catch (Exception e) {
            return "";
        }
    }

    private String getTemperature() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getTemperature();
        } catch (Exception e) {
            return "0";
        }
    }

    private String getIcon() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getIcon();
        } catch (Exception e) {
            return "0";
        }
    }

    private String getState() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getState();
        } catch (Exception e) {
            return "";
        }
    }

    private String getWeatherMainState() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getWeatherMainState();
        } catch (Exception e) {
            return "";
        }
    }

    private String getHumidity() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return getString(R.string.humidity_city_details) + Objects.requireNonNull(cityDetailsData).getHumidity();
        } catch (Exception e) {
            return "";
        }
    }

    private String getPressure() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return getString(R.string.pressure_city_details) + Objects.requireNonNull(cityDetailsData).getPressure();
        } catch (Exception e) {
            return "";
        }
    }

    private String getWindSpeed() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return getString(R.string.wind_speed_city_details) + Objects.requireNonNull(cityDetailsData).getWindSpeed();
        } catch (Exception e) {
            return "";
        }
    }

    private String getDayAndNightTemperature() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments()
                .getSerializable("index");
        try {
            return Objects.requireNonNull(cityDetailsData).getDayAndNightTemperature();
        } catch (Exception e) {
            return "";
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

package com.example.geekbrainsandroidweather.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.geekbrainsandroidweather.BuildConfig;
import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.custom_views.HumidityView;
import com.example.geekbrainsandroidweather.custom_views.PressureView;
import com.example.geekbrainsandroidweather.custom_views.ThermometerView;
import com.example.geekbrainsandroidweather.custom_views.WindView;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.model.ForecastDayData;
import com.example.geekbrainsandroidweather.model.HourlyForecastData;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerForecastAdapter;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerHourlyDataAdapter;
import com.example.geekbrainsandroidweather.rest.OpenWeatherHelper;
import com.example.geekbrainsandroidweather.rest.OpenWeatherRepo;
import com.example.geekbrainsandroidweather.rest.entities.forecast.ForecastRequest;
import com.example.geekbrainsandroidweather.rest.entities.weather.WeatherRequest;
import com.example.geekbrainsandroidweather.room.App;
import com.example.geekbrainsandroidweather.room.RoomHelper;
import com.example.geekbrainsandroidweather.room.model.Favorites;
import com.example.geekbrainsandroidweather.room.model.History;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitiesDetailsFragment extends Fragment implements Constants {
    private TextView city;
    private TextView temperature;
    private TextView pressureTextView;
    private TextView humidityTextView;
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
    private CityDetailsData cityDetailsData;
    private DrawerLayout drawer;
    private SwipeRefreshLayout swipeRefresh;
    private SharedPreferences sharedPref;
    private AppCompatCheckBox addToFavorite;

    public static CitiesDetailsFragment create(CityDetailsData cityDetails, Bundle bundle) {
        CitiesDetailsFragment citiesDetailsFragment = new CitiesDetailsFragment();
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
        setCityParameters();
        setupRecyclerView();
        setupHourlyRecyclerView();
        getSettingParameters();
        requireActivity().findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
        setBackground();
        setSwipeRefreshBehaviour();
        setOnAddToFavoritesClickBehaviour();
    }


    private void setOnAddToFavoritesClickBehaviour() {
        RoomHelper roomHelper = new RoomHelper();
        addToFavorite.setOnCheckedChangeListener((compoundButton, b) -> addToFavorite.setOnClickListener(view -> {
            if (b) {
                roomHelper.insertFavorite(cityDetailsData);
            } else {
                roomHelper.deleteFavorite(cityDetailsData.getCityName());
            }
        }));
    }


    private void setCheckBoxVisibility() {
        Handler handler = new Handler();
        new Thread(() -> {
            List<Favorites> favoritesList = App.getInstance().getEducationDao().selectFavorites();
            handler.post(() -> {
                for (Favorites f: favoritesList) {
                    if (cityDetailsData.getCityName().equals(f.city)) {
                        addToFavorite.setChecked(true);
                    } else {
                        addToFavorite.setChecked(false);
                    }
                }
//                if (requireArguments().getBoolean(KEY_CHECKBOX_VISIBILITY)) {
//                    addToFavorite.setVisibility(View.INVISIBLE);
//                } else {
//                    addToFavorite.setVisibility(View.VISIBLE);
//                }
                if (favoritesList.size() == 0) addToFavorite.setChecked(false);
            });
        }).start();
    }

    private void setSwipeRefreshBehaviour() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OpenWeatherRepo.getInstance().getAPI().loadWeather(cityDetailsData.getLat(), cityDetailsData.getLon(),
                        BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                        .enqueue(new Callback<WeatherRequest>() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void onResponse(@NonNull Call<WeatherRequest> call,
                                                   @NonNull Response<WeatherRequest> response) {
                                Log.i("loadWeather", "Response " + response.code());
                                if (response.body() != null && response.isSuccessful()) {
                                    cityDetailsData = new CityDetailsData(response.body(), cityDetailsData.getLat(), cityDetailsData.getLon());
                                    CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData, new Bundle());
                                    replaceFragment(fragment, R.id.fragmentContainer, false);

                                    RoomHelper historyHelper = new RoomHelper();
                                    historyHelper.insertTemperature(cityDetailsData);
                                } else {
                                    showAlert();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<WeatherRequest> call, @NonNull Throwable t) {
                                //TODO
                            }
                        });
                setCityParameters();
                setupRecyclerView();
                setupHourlyRecyclerView();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom);
        builder.setTitle(R.string.error_message)
                .setMessage(R.string.try_later)
                .setIcon(R.drawable.ic_baseline_info_24)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void replaceFragment(Fragment fragment, int containerId, boolean isAddedToBackStack) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        if (isAddedToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        getParentFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }

    private void init(@NonNull View view) {
        city = view.findViewById(R.id.city);
        temperature = view.findViewById(R.id.temperature);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
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
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        addToFavorite = view.findViewById(R.id.addToFavorite);
        drawer = requireActivity().findViewById(R.id.drawer_layout);
        cityDetailsData = (CityDetailsData) requireArguments().getSerializable(CITIES_DETAILS_INDEX);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupRecyclerView() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments().getSerializable(CITIES_DETAILS_INDEX);
        DividerItemDecoration decorator = new DividerItemDecoration(requireContext(),
                LinearLayoutManager.VERTICAL);
        decorator.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.decorator_item)));

        OpenWeatherRepo.getInstance().getAPI().loadForecast(cityDetailsData.getCityName(),
                BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                .enqueue(new Callback<ForecastRequest>() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onResponse(@NonNull Call<ForecastRequest> call,
                                           @NonNull Response<ForecastRequest> response) {
                        Log.i("loadForecast", "Response " + response.code());
                        if (response.body() != null && response.isSuccessful()) {
                            OpenWeatherHelper openWeatherHelper = new OpenWeatherHelper();
                            ArrayList<ForecastDayData> forecastDayData = openWeatherHelper.setForecastData(response.body());
                            dayAndNightTemperatureTextView.setText(openWeatherHelper.getDayAndNightTemperature(response.body()));
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            RecyclerForecastAdapter recyclerForecastAdapter = new RecyclerForecastAdapter(forecastDayData);
                            recyclerForecastView.setLayoutManager(linearLayoutManager);
                            recyclerForecastView.addItemDecoration(decorator);
                            recyclerForecastView.setAdapter(recyclerForecastAdapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ForecastRequest> call, @NonNull Throwable t) {
                        //TODO
                        Log.i("RV", "Ошибка");
                    }
                });
    }

    private void setupHourlyRecyclerView() {
        CityDetailsData cityDetailsData = (CityDetailsData) requireArguments().getSerializable(CITIES_DETAILS_INDEX);
        OpenWeatherRepo.getInstance().getAPI().loadForecast(cityDetailsData.getCityName(),
                BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                .enqueue(new Callback<ForecastRequest>() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onResponse(@NonNull Call<ForecastRequest> call,
                                           @NonNull Response<ForecastRequest> response) {
                        Log.i("loadForecast", "Response " + response.code());
                        if (response.body() != null && response.isSuccessful()) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                            OpenWeatherHelper openWeatherHelper = new OpenWeatherHelper();
                            ArrayList<HourlyForecastData> hourlyForecastList = openWeatherHelper.setHourlyData(response.body());
                            RecyclerHourlyDataAdapter recyclerHourlyDataAdapter = new RecyclerHourlyDataAdapter(hourlyForecastList);
                            recyclerHourlyView.setLayoutManager(linearLayoutManager);
                            recyclerHourlyView.setAdapter(recyclerHourlyDataAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastRequest> call, Throwable t) {
                        //TODO
                        Log.i("RV", "Ошибка");
                    }
                });
    }

    private void getSettingParameters() {
        setParameterVisibility(sharedPref.getBoolean(KEY_PRESSURE, true), pressureCustomView);
        setParameterVisibility(sharedPref.getBoolean(KEY_HUMIDITY, true), humidityCustomView);
        setParameterVisibility(sharedPref.getBoolean(KEY_WIND_SPEED, true), windCustomView);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSettingParameters();
    }

    @Override
    public void onStart() {
        super.onStart();
        setCheckBoxVisibility();
    }

    private void setCityParameters() {
        city.setText(Objects.requireNonNull(cityDetailsData).getCityName());
        temperature.setText(cityDetailsData.getTemperature());
        humidityTextView.setText(cityDetailsData.getHumidity());
        pressureTextView.setText(cityDetailsData.getPressure());
        weatherMainState.setText(cityDetailsData.getState());
        dayAndNightTemperatureTextView.setText(cityDetailsData.getDayAndNightTemperature());
        feelsLikeTextView.setText(cityDetailsData.getFeelsLikeTemperature());
        humidityCustomView.setupTexts(cityDetailsData.getHumidity(), cityDetailsData.getSunriseAndSunset());
        windCustomView.setupTexts(cityDetailsData.getWindSpeed(), cityDetailsData.getWindDegrees());
        pressureCustomView.setupTexts(cityDetailsData.getPressure(), cityDetailsData.getCloudy());
        Picasso.get().load(cityDetailsData.getIcon()).into(weatherStateImg);
        lastUpdateTextView.setText(getCurrentTime());
        thermometerView.setCurrentTemp(Integer.parseInt(cityDetailsData.getTemperature().replace("°", "")));
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

    private String getCurrentTime() {
        Calendar now = Calendar.getInstance();
        return getString(R.string.udpated_at_text) + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setBackground() {
        switch (cityDetailsData.getDefaultIcon()) {
            case "01n": {
                drawer.setBackground(getResources().getDrawable(R.drawable.n01));
                break;
            }
            case "01d": {
                drawer.setBackground(getResources().getDrawable(R.drawable.d01));
                break;
            }
            case "02n":
            case "04n": {
                drawer.setBackground(getResources().getDrawable(R.drawable.n02));
                break;
            }
            case "02d": {
                drawer.setBackground(getResources().getDrawable(R.drawable.d02));
                break;
            }
            case "03n":
            case "50n": {
                drawer.setBackground(getResources().getDrawable(R.drawable.n03));
                break;
            }
            case "03d": {
                drawer.setBackground(getResources().getDrawable(R.drawable.d03));
                break;
            }
            case "04d": {
                drawer.setBackground(getResources().getDrawable(R.drawable.d04));
                break;
            }
            case "09d":
            case "10d": {
                drawer.setBackground(getResources().getDrawable(R.drawable.d09));
                break;
            }
            case "09n":
            case "10n": {
                drawer.setBackground(getResources().getDrawable(R.drawable.n09));
                break;
            }
            case "11d": {
                drawer.setBackground(getResources().getDrawable(R.drawable.d11));
                break;
            }
            case "11n": {
                drawer.setBackground(getResources().getDrawable(R.drawable.n11));
                break;
            }
            case "50d": {
                drawer.setBackground(getResources().getDrawable(R.drawable.d50));
                break;
            }
            case "13d": {
                drawer.setBackground(getResources().getDrawable(R.drawable.d13));
                break;
            }
            case "13n": {
                drawer.setBackground(getResources().getDrawable(R.drawable.n13));
                break;
            }
        }
    }
}
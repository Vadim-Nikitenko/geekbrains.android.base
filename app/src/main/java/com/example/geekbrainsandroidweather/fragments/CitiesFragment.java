package com.example.geekbrainsandroidweather.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.model.HourlyForecastData;
import com.example.geekbrainsandroidweather.network.OpenWeatherMap;
import com.example.geekbrainsandroidweather.recycler_views.IRVOnItemClick;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerDataAdapter;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerHourlyDataAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CitiesFragment extends Fragment implements IRVOnItemClick, Constants {
    private TextView emptyTextView;
    private RecyclerView recyclerCitiesView;
    private RecyclerDataAdapter recyclerDataAdapter;
    public static ArrayList<String> cities;
    private OpenWeatherMap openWeatherMap;

    private boolean isCityDetailsExists;
    private static int currentPosition;

    // указываем макет при создании фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cities, container, false);
    }

    // инициализируем views и listView с городами
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    // инициализация views
    private void init(@NonNull View view) {
        emptyTextView = view.findViewById(R.id.emptyCityTextView);
        recyclerCitiesView = view.findViewById(R.id.recyclerCitiesView);
    }

    // проверяем ориентацию устройства и устанавливаем флаг isCityDetailsExists
    // если Bundle не пустой, устанавливаем текущую позицию значением из Bundle
    // если гор. ориентация устанавливаем единичный выбор элементов в ListView
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isCityDetailsExists = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("CurrentPosition");
        }
        setupRecyclerView();
        if (isCityDetailsExists && cities != null) {
            showCitiesDetails(cities.get(currentPosition));
        }

    }

    private void setupRecyclerView() {
        if (cities == null) {
            cities = new ArrayList<>(Arrays.asList("Moscow", "Miami", "Test")); //debug
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerDataAdapter = new RecyclerDataAdapter(cities, this, this);
        recyclerCitiesView.setLayoutManager(linearLayoutManager);
        recyclerCitiesView.setAdapter(recyclerDataAdapter);
    }


    // если гор. ориентация подсвечиваем выбранный item из ListView
    // создаем фрагмент CitiesDetailsFragment
    private void showCitiesDetails(String cityName) {
        openWeatherMap = new OpenWeatherMap();
        openWeatherMap.makeRequest(cityName);
        if (isCityDetailsExists) {
            if (OpenWeatherMap.responseCode != 200) {
                ErrorFragment errorFragment = new ErrorFragment();
                requireFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                startFragmentTransaction(R.id.citiesDetailsContainer, errorFragment);
            } else {
                replaceFragment(R.id.citiesDetailsContainer);
            }
        } else {
            replaceFragment(R.id.citiesContainer);
        }
    }

    private void replaceFragment(int container) {
        CityDetailsData cityDetailsData = openWeatherMap.getCityDetailsData();
        CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData);

        Bundle bundle = new Bundle();
        bundle.putSerializable(CITIES_DETAILS_INDEX, cityDetailsData);
        bundle.putInt(RESPONSE_CODE, OpenWeatherMap.responseCode);
        fragment.setArguments(bundle);
        startFragmentTransaction(container, fragment);
    }

    private void startFragmentTransaction(int container, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentPosition", currentPosition);
    }

    @Override
    public void onItemClicked(View view, int position) {
        currentPosition = position;
        showCitiesDetails(cities.get(position));
    }

    @Override
    public void onItemLongPressed(View view) {
    }

    @Override
    public void changeItem(TextView view) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.default_text_size));
    }


}

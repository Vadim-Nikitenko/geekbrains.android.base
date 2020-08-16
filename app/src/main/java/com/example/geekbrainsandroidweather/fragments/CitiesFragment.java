package com.example.geekbrainsandroidweather.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.CitiesDetailsActivity;
import com.example.geekbrainsandroidweather.IRVOnItemClick;
import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.RecyclerDataAdapter;
import com.example.geekbrainsandroidweather.model.CityDetailsData;

import java.util.Objects;

public class CitiesFragment extends Fragment implements IRVOnItemClick {
    private TextView emptyTextView;
    private RecyclerView recyclerCitiesView;
    private RecyclerDataAdapter recyclerDataAdapter;

    private boolean isCityDetailsExists;
    private int currentPosition = 0;

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
        initViews(view);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        String[] cities = getResources().getStringArray(R.array.cities);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration decorator = new DividerItemDecoration(Objects.requireNonNull(getContext()),
                LinearLayoutManager.VERTICAL);
        decorator.setDrawable(Objects.requireNonNull(getContext().getDrawable(R.drawable.decorator_item_1)));
        recyclerDataAdapter = new RecyclerDataAdapter(cities, this, this);
        recyclerCitiesView.setLayoutManager(linearLayoutManager);
        recyclerCitiesView.addItemDecoration(decorator);
        recyclerCitiesView.setAdapter(recyclerDataAdapter);
    }

    // инициализация views
    private void initViews(@NonNull View view) {
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
            currentPosition = savedInstanceState.getInt("CurrentCity", 0);
        }

        if (isCityDetailsExists) {
            showCitiesDetails();
        }
    }

    // если гор. ориентация подсвечиваем выбранный item из ListView
    // создаем фрагмент CitiesDetailsFragment
    public void showCitiesDetails() {
        if (isCityDetailsExists) {
            CitiesDetailsFragment fragment = (CitiesDetailsFragment)
                    Objects.requireNonNull(getFragmentManager()).findFragmentById(R.id.citiesDetailsContainer);
            if (fragment == null || fragment.getIndex() != currentPosition) {
                fragment = CitiesDetailsFragment.create(getCityDetails());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.citiesDetailsContainer, fragment);
                fragmentTransaction.addToBackStack("key");
                fragmentTransaction.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(getActivity()), CitiesDetailsActivity.class);
            intent.putExtra("index", getCityDetails());
            startActivity(intent);
        }
    }

    private CityDetailsData getCityDetails() {
        String[] cities = getResources().getStringArray(R.array.cities);
        String[] states = getResources().getStringArray(R.array.states);
        return new CityDetailsData()
                .withCityName(cities[currentPosition])
                .withPosition(currentPosition)
                .withState(states[currentPosition])
                .withDayAndNightTemperature((int) (Math.random() * 40) + "° / " + (int) (Math.random() * 40) + "°")
                .withTemperature((int) (Math.random() * 40));
    }

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClicked(String text, int position) {
        currentPosition = position;
        showCitiesDetails();
    }

    @Override
    public void changeItem(TextView view) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.default_text_size));
    }
}

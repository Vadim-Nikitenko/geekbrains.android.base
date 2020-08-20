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
import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.network.OpenWeatherMap;
import com.example.geekbrainsandroidweather.recycler_views.IRVOnItemClick;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerDataAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CitiesFragment extends Fragment implements IRVOnItemClick {
    private TextView emptyTextView;
    private RecyclerView recyclerCitiesView;
    private RecyclerDataAdapter recyclerDataAdapter;
    private ArrayList<String> cities;
    private String[] citiesList;
    private OpenWeatherMap openWeatherMap;

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
    }

    private void setupRecyclerView() {
        if (cities == null) {
            cities = new ArrayList<>(Arrays.asList(getResources()
                    .getStringArray(R.array.cities)));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration decorator = new DividerItemDecoration(requireContext(),
                LinearLayoutManager.VERTICAL);
        decorator.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.decorator_item_1)));
        recyclerDataAdapter = new RecyclerDataAdapter(cities, this, this);
        recyclerCitiesView.setLayoutManager(linearLayoutManager);
        recyclerCitiesView.addItemDecoration(decorator);
        recyclerCitiesView.setAdapter(recyclerDataAdapter);
    }

    // инициализация views
    private void initViews(@NonNull View view) {
        emptyTextView = view.findViewById(R.id.emptyCityTextView);
        recyclerCitiesView = view.findViewById(R.id.recyclerCitiesView);
        citiesList = getResources().getStringArray(R.array.cities);
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
            cities = savedInstanceState.getStringArrayList("CitiesList");
        }

        if (isCityDetailsExists) {
            showCitiesDetails(citiesList[currentPosition]);
        }
        setupRecyclerView();
    }

    // если гор. ориентация подсвечиваем выбранный item из ListView
    // создаем фрагмент CitiesDetailsFragment
    public void showCitiesDetails(String cityName) {
        openWeatherMap = new OpenWeatherMap(cityName);
        if (isCityDetailsExists) {
            if (OpenWeatherMap.responseCode != 200) {
                ErrorFragment errorFragment = new ErrorFragment();
                FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.citiesDetailsContainer, errorFragment);
                fragmentTransaction.commit();
            } else {
                CitiesDetailsFragment fragment = new CitiesDetailsFragment();
                fragment = CitiesDetailsFragment.create(openWeatherMap.getCityDetailsData());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.citiesDetailsContainer, fragment);
                fragmentTransaction.addToBackStack("key");
                fragmentTransaction.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(requireActivity(), CitiesDetailsActivity.class);
            intent.putExtra("index", openWeatherMap.getCityDetailsData());
            intent.putExtra("ResponseCode", OpenWeatherMap.responseCode);
            intent.putExtra("CitiesList", cities);
            startActivity(intent);
        }
    }

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("CurrentCity", currentPosition);
        outState.putStringArrayList("CitiesList", cities);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onItemClicked(View view, int position) {
        currentPosition = position;
        showCitiesDetails(citiesList[position]);
    }

    @Override
    public void onItemLongPressed(View view) {
        TextView textView = (TextView) view;
        deleteItem(textView);
    }

    @Override
    public void changeItem(TextView view) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.default_text_size));
    }

    public void deleteItem(final TextView view) {
        Snackbar.make(view, "Удалить город?", Snackbar.LENGTH_LONG)
                .setAction("Удалить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cityName = view.getText().toString();
                        recyclerDataAdapter.remove(cityName);
                        cities.remove(cityName);
                    }
                }).show();
    }


}

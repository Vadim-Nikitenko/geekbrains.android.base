package com.example.geekbrainsandroidweather.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.BuildConfig;
import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.recycler_views.IRVOnItemClick;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerDataAdapter;
import com.example.geekbrainsandroidweather.rest.OpenWeatherRepo;
import com.example.geekbrainsandroidweather.rest.entities.weather.WeatherRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCityFragment extends Fragment implements IRVOnItemClick, Constants {

    private TextInputEditText addCityInput;
    private RecyclerView recyclerCities;
    private RecyclerDataAdapter recyclerDataAdapter;
    public ArrayList<String> cities;
    private ProgressBar progressBar;
    private TextView emptyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();
        setOnAddCityInputEditorBehaviour(view);
        requireActivity().findViewById(R.id.appBarLayout).setVisibility(View.INVISIBLE);
    }

    private void setOnAddCityInputEditorBehaviour(@NonNull View view) {
        addCityInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    String inputText = addCityInput.getText().toString();
                    if (!inputText.equals("")) {
                        if (!cities.contains(inputText)) {
                            cities.add(inputText);
                            setupRecyclerView();
                            addCityInput.getText().clear();
                        } else {
                            Snackbar.make(view, R.string.snackbar_sity_already_added, Snackbar.LENGTH_LONG).setAnchorView(addCityInput).show();
                        }
                    }
                }
                return true;
            }
        });
    }


    private void initViews(View view) {
        addCityInput = view.findViewById(R.id.addCityInput);
        recyclerCities = view.findViewById(R.id.recyclerCities);
        emptyTextView = view.findViewById(R.id.emptyCityTextView);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        if (cities == null) {
            cities = new ArrayList<>(Arrays.asList("Moscow", "Miami", "Test")); //debug
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (getContext());
        DividerItemDecoration decorator = new DividerItemDecoration(requireContext(),
                LinearLayoutManager.VERTICAL);
        recyclerDataAdapter = new RecyclerDataAdapter(cities, this, this);
        recyclerCities.setLayoutManager(linearLayoutManager);
        recyclerCities.addItemDecoration(decorator);
        recyclerCities.setAdapter(recyclerDataAdapter);
    }

    @Override
    public void onItemClicked(View view, int position) {
        progressBar.setVisibility(View.VISIBLE);
        OpenWeatherRepo.getInstance().getAPI().loadWeather(cities.get(position),
                BuildConfig.WEATHER_API_KEY, "metric")
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            CityDetailsData cityDetailsData = new CityDetailsData(response.body());
                            replaceFragment(cityDetailsData);
                            NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                            navigationView.setCheckedItem(R.id.page_1);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogCustom);
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
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequest> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onItemLongPressed(View view) {

    }

    @Override
    public void changeItem(TextView view) {

    }

    private void replaceFragment(CityDetailsData cityDetailsData) {
        CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        getParentFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }

}

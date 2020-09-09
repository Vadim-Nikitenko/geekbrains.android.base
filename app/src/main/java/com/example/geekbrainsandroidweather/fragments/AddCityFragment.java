package com.example.geekbrainsandroidweather.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.BuildConfig;
import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.recycler_views.IRVOnItemClick;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerDataAdapter;
import com.example.geekbrainsandroidweather.rest.DadataRepo;
import com.example.geekbrainsandroidweather.rest.OpenWeatherRepo;
import com.example.geekbrainsandroidweather.rest.entities.city.BodyCityRequest;
import com.example.geekbrainsandroidweather.rest.entities.city.CityGeoData;
import com.example.geekbrainsandroidweather.rest.entities.city.CityRequest;
import com.example.geekbrainsandroidweather.rest.entities.weather.WeatherRequest;
import com.example.geekbrainsandroidweather.room.TemperatureHistoryHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCityFragment extends Fragment implements IRVOnItemClick, Constants {

    private TextInputEditText addCityInput;
    private RecyclerView recyclerCities;
    private RecyclerDataAdapter recyclerDataAdapter;
    public ArrayList<CityGeoData> cities;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private TemperatureHistoryHelper historyHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setupRecyclerView();
        setOnAddCityInputEditorBehaviour(view);
        requireActivity().findViewById(R.id.appBarLayout).setVisibility(View.INVISIBLE);
        setOnCityEditTextChangeBehaviour();
    }

    //инициализация вьюх
    private void init(View view) {
        addCityInput = view.findViewById(R.id.addCityInput);
        recyclerCities = view.findViewById(R.id.recyclerCities);
        emptyTextView = view.findViewById(R.id.emptyCityTextView);
        progressBar = view.findViewById(R.id.progressBar);
        historyHelper = new TemperatureHistoryHelper();
    }

    //сворачиваем клавиатуру при клике на ОК (IME_ACTION_DONE) keyboard
    private void setOnAddCityInputEditorBehaviour(@NonNull View view) {
        addCityInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    View view = requireActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                return true;
            }
        });
    }

    // инициализируем RecyclerView с пустым ArrayList
    private void setupRecyclerView() {
        if (cities == null) {
            cities = new ArrayList<>(); //debug
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
    public void onItemLongPressed(View view) {
    }

    @Override
    public void changeItem(TextView view) {
    }

    // приватный метод для смены фрагментов
    private void replaceFragment(CityDetailsData cityDetailsData) {
        CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        getParentFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }

    // устанавливаем слушатель на изменение текста в инпуте. Если количество символов >= 3,
    // отправляем запрос к Dadata для получения списка подсказок по городам. Полученный список
    // добавляем в ArrayList<CityGeoData> cities и переинициализируем RecyclerView
    private void setOnCityEditTextChangeBehaviour() {
        addCityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 >= 3) {
                    DadataRepo.getInstance().getAPI().loadCity(
                            "application/json",
                            BuildConfig.DADATA_API_KEY,
                            BuildConfig.DADATA_SECRET,
                            new BodyCityRequest(charSequence.toString()))
                            .enqueue(new Callback<CityRequest>() {
                                @Override
                                public void onResponse(@NonNull Call<CityRequest> call,
                                                       @NonNull Response<CityRequest> response) {
                                    if (response.body() != null && response.isSuccessful()) {
                                        cities.clear();
                                        for (int j = 0; j < response.body().getSuggestions().size(); j++) {
                                            int count = 0;
                                            if (response.body().getSuggestions().get(0).getValue() != null) {
                                                CityGeoData cityGeoData = new CityGeoData(
                                                        response.body().getSuggestions().get(j).getValue().replace("г ", ""),
                                                        response.body().getSuggestions().get(j).getData().getGeoLat(),
                                                        response.body().getSuggestions().get(j).getData().getGeoLon()
                                                );
                                                cities.add(cityGeoData);
                                                count++;
                                                if (count > 10) break;
                                            }
                                        }
                                        setupRecyclerView();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<CityRequest> call, @NonNull Throwable t) {
                                    //TODO
                                    Log.i("RV", "Ошибка");
                                }
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // слушатель на клик из полученных подсказок. По клику отправляем запрос loadWeather
    // и меняем фрагмент с погодой по данному району. Если подсказка не валидна, отображаем
    // алерт с ошибкой.
    @Override
    public void onItemClicked(View view, int position) {
        progressBar.setVisibility(View.VISIBLE);
        OpenWeatherRepo.getInstance().getAPI().loadWeather(cities.get(position).getLat(), cities.get(position).getLon(),
                BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            CityDetailsData cityDetailsData = new CityDetailsData(response.body());
                            replaceFragment(cityDetailsData);
                            NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                            navigationView.setCheckedItem(R.id.page_1);
                            historyHelper.insertTemperature(cityDetailsData);
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

}

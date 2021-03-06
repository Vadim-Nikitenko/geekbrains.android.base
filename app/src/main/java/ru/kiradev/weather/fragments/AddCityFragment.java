package ru.kiradev.weather.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.kiradev.weather.BuildConfig;
import ru.kiradev.weather.R;
import ru.kiradev.weather.model.CityDetailsData;
import ru.kiradev.weather.recycler_views.IRVOnItemClick;
import ru.kiradev.weather.recycler_views.RecyclerDataAdapter;
import ru.kiradev.weather.recycler_views.RecyclerFavoriteCityAdapter;
import ru.kiradev.weather.rest.DadataRepo;
import ru.kiradev.weather.rest.OpenWeatherRepo;
import ru.kiradev.weather.rest.entities.city.BodyCityRequest;
import ru.kiradev.weather.rest.entities.city.CityGeoData;
import ru.kiradev.weather.rest.entities.city.CityRequest;
import ru.kiradev.weather.rest.entities.weather.WeatherRequest;
import ru.kiradev.weather.room.App;
import ru.kiradev.weather.room.RoomHelper;
import ru.kiradev.weather.room.model.Favorites;

public class AddCityFragment extends Fragment implements IRVOnItemClick, Constants {

    private TextInputEditText addCityInput;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private RecyclerView recyclerCities;
    private RecyclerView recyclerFavoriteCity;
    private RecyclerDataAdapter recyclerDataAdapter;
    private RecyclerFavoriteCityAdapter recyclerFavoriteCityAdapter;
    public ArrayList<CityGeoData> citiesList;
    public ArrayList<CityDetailsData> favoriteCityDetailsDataList;
    private RoomHelper historyHelper;
    private SharedPreferences sharedPref;
    private List<Favorites> cities = new ArrayList<>();

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
        setOnCityEditTextChangeBehaviour();

        if (getArguments() != null) {
            String city = requireArguments().getString(KEY_CITY_NAME);
            Handler handler = new Handler();
            new Thread(() -> {
                App.getInstance().getEducationDao().deleteFavorites(city);
                handler.post(() -> {
                    getFavorites();
                });
            }).start();
        } else {
            getFavorites();
        }
    }

    //инициализация вьюх
    private void init(View view) {
        addCityInput = view.findViewById(R.id.addCityInput);
        recyclerCities = view.findViewById(R.id.recyclerCities);
        recyclerFavoriteCity = view.findViewById(R.id.recyclerFavoriteCity);
        emptyTextView = view.findViewById(R.id.emptyCityTextView);
        progressBar = view.findViewById(R.id.progressBar);
        historyHelper = new RoomHelper();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    //сворачиваем клавиатуру при клике на ОК (IME_ACTION_DONE) keyboard
    private void setOnAddCityInputEditorBehaviour(@NonNull View view) {
        addCityInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                View view1 = requireActivity().getCurrentFocus();
                if (view1 != null) {
                    hideKeyboard(view1);
                }
            }
            return true;
        });
    }

    // инициализируем RecyclerView с пустым ArrayList
    private void setupRecyclerView() {
        if (citiesList == null) {
            citiesList = new ArrayList<>();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (getContext());
        recyclerDataAdapter = new RecyclerDataAdapter(citiesList, this, this);
        recyclerCities.setLayoutManager(linearLayoutManager);
        if (recyclerCities.getItemDecorationCount() <= 0) {
            DividerItemDecoration decorator = new DividerItemDecoration(requireContext(),
                    LinearLayoutManager.VERTICAL);
            recyclerCities.addItemDecoration(decorator);
        }
        recyclerCities.setAdapter(recyclerDataAdapter);
    }

    private void setupRecyclerFavoriteCityAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (getContext());
        recyclerFavoriteCityAdapter = new RecyclerFavoriteCityAdapter(favoriteCityDetailsDataList, getParentFragmentManager());
        recyclerFavoriteCity.setLayoutManager(linearLayoutManager);
        recyclerFavoriteCity.setAdapter(recyclerFavoriteCityAdapter);
    }

    @Override
    public void onItemLongPressed(View view) {
    }

    @Override
    public void changeItem(TextView view) {
    }

    // приватный метод для смены фрагментов
    private void replaceFragment(CityDetailsData cityDetailsData) {
        CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData, new Bundle());
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
                                    Log.i("loadCity", "Response " + response.code());
                                    if (response.body() != null && response.isSuccessful()) {
                                        citiesList.clear();
                                        for (int j = 0; j < response.body().getSuggestions().size(); j++) {
                                            int count = 0;
                                            if (response.body().getSuggestions().get(0).getValue() != null) {
                                                CityGeoData cityGeoData = new CityGeoData(
                                                        response.body().getSuggestions().get(j).getValue().replace("г ", ""),
                                                        response.body().getSuggestions().get(j).getData().getGeoLat(),
                                                        response.body().getSuggestions().get(j).getData().getGeoLon()
                                                );
                                                citiesList.add(cityGeoData);
                                                count++;
                                                if (count > 10) break;
                                            }
                                        }
                                        setupRecyclerView();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<CityRequest> call, @NonNull Throwable t) {
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
        OpenWeatherRepo.getInstance().getAPI().loadWeather(citiesList.get(position).getLat(), citiesList.get(position).getLon(),
                BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        Log.i("loadWeather", "Response " + response.code());
                        if (response.body() != null && response.isSuccessful()) {
                            String lat = citiesList.get(position).getLat();
                            String lon = citiesList.get(position).getLon();
                            CityDetailsData cityDetailsData = new CityDetailsData(response.body(), lat, lon);
                            replaceFragment(cityDetailsData);
                            NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                            navigationView.setCheckedItem(R.id.page_1);
                            historyHelper.insertTemperature(cityDetailsData);
                            hideKeyboard(view);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(KEY_CURRENT_CITY_LAT, lat);
                            editor.putString(KEY_CURRENT_CITY_LON, lon);
                            editor.apply();
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

    private void hideKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getFavoritesCitiesData() {
        for (int i = 0; i < cities.size(); i++) {
            String lat = cities.get(i).lat;
            String lon = cities.get(i).lon;
            OpenWeatherRepo.getInstance().getAPI().loadWeather(lat, lon,
                    BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                    .enqueue(new Callback<WeatherRequest>() {
                        @Override
                        public void onResponse(@NonNull Call<WeatherRequest> call,
                                               @NonNull Response<WeatherRequest> response) {
                            Log.i("loadWeather", "Response " + response.code());
                            if (response.body() != null && response.isSuccessful()) {
                                CityDetailsData cityDetailsData = new CityDetailsData(response.body(), lat, lon);
                                if (favoriteCityDetailsDataList == null) {
                                    favoriteCityDetailsDataList = new ArrayList<>();
                                }
                                favoriteCityDetailsDataList.add(cityDetailsData);
                                setupRecyclerFavoriteCityAdapter();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<WeatherRequest> call, Throwable t) {
                            t.printStackTrace();
                        }

                        ;
                    });
        }
    }

    private void getFavorites() {
        Handler handler = new Handler();
        new Thread(() -> {
            List<Favorites> historiesNew = App.getInstance().getEducationDao().selectFavorites();
            handler.post(() -> {
                cities = historiesNew;
                getFavoritesCitiesData();
            });
        }).start();
    }

}

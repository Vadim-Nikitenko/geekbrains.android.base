package ru.kiradev.weather.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.kiradev.weather.BuildConfig;
import ru.kiradev.weather.R;
import ru.kiradev.weather.model.CityDetailsData;
import ru.kiradev.weather.rest.OpenWeatherRepo;
import ru.kiradev.weather.rest.entities.weather.WeatherRequest;
import ru.kiradev.weather.room.RoomHelper;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, Constants {

    private MapView googleMap;
    private LocationManager mLocManager;
    private GoogleMap mMap;
    private RoomHelper historyHelper;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initializeMap(savedInstanceState);
    }

    private void initializeMap(@Nullable Bundle savedInstanceState) {
        googleMap.onCreate(savedInstanceState);
        googleMap.getMapAsync(this);
        mLocManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        historyHelper = new RoomHelper();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    private void init(View view) {
        googleMap = view.findViewById(R.id.googleMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            mMap.setMyLocationEnabled(true);
            // Get last know location
            @SuppressLint("MissingPermission") final Location loc = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            // If location available
            if (loc != null) {
                // Create LatLng object for Maps
                LatLng target = new LatLng(loc.getLatitude(), loc.getLongitude());
                // Defines a camera move. An object of this type can be used to modify a map's camera
                // by calling moveCamera()
                moveCamera(target, 10F);
            }
            mMap.setOnMapClickListener(this::addMarker);

            // Enable my-location button
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable zoom controls
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    private void addMarker(LatLng location) {
        String lat = String.valueOf(location.latitude);
        String lon = String.valueOf(location.longitude);
        OpenWeatherRepo.getInstance().getAPI().loadWeather(lat, lon,
                BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        Log.i("loadWeather", "Response " + response.code());
                        if (response.body() != null && response.isSuccessful()) {
                            CityDetailsData cityDetailsData = new CityDetailsData(response.body(), lat, lon);
                            replaceFragment(cityDetailsData);
                            NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                            navigationView.setCheckedItem(R.id.page_1);
                            historyHelper.insertTemperature(cityDetailsData);

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
                                            (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequest> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void replaceFragment(CityDetailsData cityDetailsData) {
        CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData, new Bundle());
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        getParentFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }

    private void moveCamera(LatLng target, float zoom) {
        if (target == null || zoom < 1) return;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, zoom));
    }

    @Override
    public void onResume() {
        super.onResume();
        googleMap.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleMap.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleMap.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        googleMap.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        googleMap.onPause();
    }

}

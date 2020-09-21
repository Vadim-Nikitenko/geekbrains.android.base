package com.example.geekbrainsandroidweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geekbrainsandroidweather.custom_views.CurrentCityView;
import com.example.geekbrainsandroidweather.fragments.AddCityFragment;
import com.example.geekbrainsandroidweather.fragments.CitiesDetailsFragment;
import com.example.geekbrainsandroidweather.fragments.Constants;
import com.example.geekbrainsandroidweather.fragments.DevelopersInfoFragment;
import com.example.geekbrainsandroidweather.fragments.GoogleMapFragment;
import com.example.geekbrainsandroidweather.fragments.HistoryFragment;
import com.example.geekbrainsandroidweather.fragments.SettingsFragment;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.rest.OpenWeatherRepo;
import com.example.geekbrainsandroidweather.rest.entities.weather.WeatherRequest;
import com.example.geekbrainsandroidweather.room.RoomHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Constants {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ProgressBar progressBar;
    private CityDetailsData cityDetailsData;
    private String lat;
    private String lon;
    private ConstraintLayout mainLayout;
    private CurrentCityView favoriteCurrentCity;
    private ConstraintLayout drawerMenuHeader;

    private RoomHelper historyHelper;
    private BroadcastReceiver networkReceiver = new NetworkChangeReceiver();
    private SharedPreferences sharedPref;
    private LocationManager mLocManager = null;
    private FusedLocationProviderClient fusedLocationClient;
    private LocListener mLocListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setupActionBar();
        requestLocationPermission();
        checkSystemNavigation();
        setOnClickForSideMenuItems();
        setOnCurrentCityClickBehaviour();
    }

    private void setOnCurrentCityClickBehaviour() {
        favoriteCurrentCity.setOnClickListener(view -> {
            setCurrentCoordinates();
            sharedPref.edit().remove(KEY_CURRENT_CITY_LAT).remove(KEY_CURRENT_CITY_LON).apply();
            Bundle bundle = new Bundle();
            bundle.putBoolean(KEY_CHECKBOX_VISIBILITY, true);
            setCityFragment(bundle);
            drawer.close();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(CONNECTIVITY_CHANGE));
        initNotificationChannel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("1", "networkStateChanges", importance);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void init() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        progressBar = findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.mainLayout);
        favoriteCurrentCity = headerLayout.findViewById(R.id.favoriteCurrentCity);
        drawerMenuHeader = headerLayout.findViewById(R.id.drawerMenuHeader);
        historyHelper = new RoomHelper();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mLocManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mLocListener = new LocListener();
    }

    // настройка прозрачного бара
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        appBarLayout.setOutlineProvider(null);
        ViewCompat.setElevation(toolbar, 0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    // клики по пунктам меню drawer
    private void setOnClickForSideMenuItems() {
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.page_5) {
                replaceFragment(new GoogleMapFragment(), true);
                drawer.close();
            }
            if (item.getItemId() == R.id.page_4) {
                replaceFragment(new HistoryFragment(), true);
                drawer.close();
            }
            if (item.getItemId() == R.id.page_3) {
                replaceFragment(new DevelopersInfoFragment(), true);
                drawer.close();
            }
            if (item.getItemId() == R.id.page_2) {
                replaceFragment(new SettingsFragment(), true);
                drawer.close();
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addCity) {
            replaceFragment(new AddCityFragment(), true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCityFragment(Bundle bundle) {
        String lat = sharedPref.getString(KEY_CURRENT_CITY_LAT, this.lat);
        String lon = sharedPref.getString(KEY_CURRENT_CITY_LON, this.lon);
        if (lat != null && lon != null) {
            OpenWeatherRepo.getInstance().getAPI().loadWeather(lat, lon,
                    BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                    .enqueue(new Callback<WeatherRequest>() {
                        @Override
                        public void onResponse(@NonNull Call<WeatherRequest> call,
                                               @NonNull Response<WeatherRequest> response) {
                            Log.i("loadWeather", "Response " + response.code() + " setCityFragment()");
                            if (response.body() != null && response.isSuccessful()) {
                                cityDetailsData = new CityDetailsData(response.body(), lat, lon);
                                CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData, bundle);
                                replaceFragment(fragment, false);
                                historyHelper.insertTemperature(cityDetailsData);
                            } else {
                                showAlert();
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

    private void setMenuDrawerCurrentCity() {
        setCurrentCoordinates();
        if (lat != null && lon != null) {
            OpenWeatherRepo.getInstance().getAPI().loadWeather(lat, lon,
                    BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                    .enqueue(new Callback<WeatherRequest>() {
                        @Override
                        public void onResponse(@NonNull Call<WeatherRequest> call,
                                               @NonNull Response<WeatherRequest> response) {
                            Log.i("loadWeather", "Response " + response.code() + " setMenuDrawerCurrentCity()");
                            if (response.body() != null && response.isSuccessful()) {
                                cityDetailsData = new CityDetailsData(response.body(), lat, lon);
                                favoriteCurrentCity.setupViews(cityDetailsData, drawerMenuHeader);
                            } else {
                                showAlert();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<WeatherRequest> call, @NonNull Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
    }

    private void replaceFragment(Fragment fragment, boolean isAddedToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        if (isAddedToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        getSupportFragmentManager().popBackStack();
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isOpen()) {
            drawer.close();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    private void showAlert() {
        new AlertDialog.Builder(getApplicationContext(), R.style.AlertDialogCustom)
                .setTitle(R.string.error_message)
                .setMessage(R.string.try_later)
                .setIcon(R.drawable.ic_baseline_info_24)
                .setPositiveButton(R.string.ok,
                        (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.geo_msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.continue_action, (dialogInterface, i) -> ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            100))
                    .create()
                    .show();
        } else {
            setMenuDrawerCurrentCity();
        }
    }

    private void setCurrentCoordinates() {
        List<String> providers = mLocManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location l = mLocManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
                lon = String.valueOf(Objects.requireNonNull(l).getLongitude());
                lat = String.valueOf(l.getLatitude());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocListener == null) mLocListener = new LocListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                3000L, 1000.0F, mLocListener);

        fusedLocationClient.registerListener(mLocListener, "1");
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                lon = String.valueOf(location.getLongitude());
                lat = String.valueOf(location.getLatitude());
            } else {
                setCurrentCoordinates();
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                setMenuDrawerCurrentCity();
                Bundle bundle = new Bundle();
                bundle.putBoolean(KEY_CHECKBOX_VISIBILITY, true);
                setCityFragment(bundle);
            }
        });
    }

    @Override
    protected void onPause() {
        if (mLocListener != null) mLocManager.removeUpdates(mLocListener);
        super.onPause();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            boolean permissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted = false;
                    break;
                }
            }
            if (!permissionsGranted && !isGeoEnabled()) {
                recreate();
            }
        }
    }

    private void checkSystemNavigation() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mainLayout.getLayoutParams();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasBackKey) {
            params.setMargins(0, 0, 0, 0);
            mainLayout.setLayoutParams(params);
        }
    }

    private boolean isGeoEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // внутренни класс слушатель изменения локации
    private final class LocListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            lon = String.valueOf(Objects.requireNonNull(location).getLongitude());
            lat = String.valueOf(location.getLatitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            @SuppressLint("MissingPermission")
            Location l = mLocManager.getLastKnownLocation(provider);
            lon = String.valueOf(Objects.requireNonNull(l).getLongitude());
            lat = String.valueOf(l.getLatitude());
            setCityFragment(new Bundle());
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
}
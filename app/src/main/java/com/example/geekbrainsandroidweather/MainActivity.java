package com.example.geekbrainsandroidweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.geekbrainsandroidweather.fragments.AddCityFragment;
import com.example.geekbrainsandroidweather.fragments.CitiesDetailsFragment;
import com.example.geekbrainsandroidweather.fragments.Constants;
import com.example.geekbrainsandroidweather.fragments.DevelopersInfoFragment;
import com.example.geekbrainsandroidweather.fragments.HistoryFragment;
import com.example.geekbrainsandroidweather.fragments.SettingsFragment;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.rest.OpenWeatherRepo;
import com.example.geekbrainsandroidweather.rest.entities.weather.WeatherRequest;
import com.example.geekbrainsandroidweather.room.TemperatureHistoryHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

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

    private TemperatureHistoryHelper historyHelper;
    private BroadcastReceiver networkReceiver = new NetworkChangeReceiver();
    private SharedPreferences sharedPref;
    private LocationManager mLocManager = null;
    private LocListener mLocListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        requestLocationPermission();
        checkSystemNavigation();
        setupActionBar();
        setOnClickForSideMenuItems();
//        firebaseSync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            recreate();
        }
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
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        progressBar = findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.mainLayout);
        historyHelper = new TemperatureHistoryHelper();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mLocManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        mLocListener = new LocListener();
    }

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

    private void setOnClickForSideMenuItems() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_4) {
                    replaceFragment(new HistoryFragment(), R.id.fragmentContainer, true);
                    drawer.close();
                }
                if (item.getItemId() == R.id.page_3) {
                    replaceFragment(new DevelopersInfoFragment(), R.id.fragmentContainer, true);
                    drawer.close();
                }
                if (item.getItemId() == R.id.page_2) {
                    replaceFragment(new SettingsFragment(), R.id.fragmentContainer, true);
                    drawer.close();
                }
                if (item.getItemId() == R.id.page_1) {
                    setCurrentCoordinates();
                    sharedPref.edit().remove(KEY_CURRENT_CITY_LAT).remove(KEY_CURRENT_CITY_LON).apply();
                    setCityFragment();
                    drawer.close();
                }
                return true;
            }
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
            replaceFragment(new AddCityFragment(), R.id.fragmentContainer, true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCityFragment() {
        String lat = sharedPref.getString(KEY_CURRENT_CITY_LAT, this.lat);
        String lon = sharedPref.getString(KEY_CURRENT_CITY_LON, this.lon);
        if (lat != null && lon != null) {
            OpenWeatherRepo.getInstance().getAPI().loadWeather(lat, lon,
                    BuildConfig.WEATHER_API_KEY, LANG, UNITS)
                    .enqueue(new Callback<WeatherRequest>() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void onResponse(@NonNull Call<WeatherRequest> call,
                                               @NonNull Response<WeatherRequest> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                cityDetailsData = new CityDetailsData(response.body(), lat, lon);
                                CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData);
                                replaceFragment(fragment, R.id.fragmentContainer, false);

                                historyHelper.insertTemperature(cityDetailsData);
                            } else {
                                showAlert();
                            }
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Call<WeatherRequest> call, @NonNull Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }


    private void replaceFragment(Fragment fragment, int containerId, boolean isAddedToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
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
            navigationView.setCheckedItem(R.id.page_1);
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
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .create()
                .show();
    }

    private void requestLocationPermission() {
        if (!isGeoEnabled()) {
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.geo_msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.continue_action, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    100);
                        }
                    })
                    .create()
                    .show();
        } else {
            setCurrentCoordinates();
            setCityFragment();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000L, 1000.0F, mLocListener);
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


    private void firebaseSync() {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(BuildConfig.FIREBASE_APP_ID)
                .setProjectId(BuildConfig.FIREBASE_PROJECT_ID)
                .setApiKey(BuildConfig.FIREBASE_API_KEY)
                .build();
        FirebaseApp.initializeApp(this, options, BuildConfig.FIREBASE_APP_NAME);
    }

    private void checkSystemNavigation() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mainLayout.getLayoutParams();
        boolean hasMenuKey = ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            params.setMargins(0, 0, 0, 0);
            mainLayout.setLayoutParams(params);
        }
    }

    private boolean isGeoEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private final class LocListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            lon = String.valueOf(Objects.requireNonNull(location).getLongitude());
            lat = String.valueOf(location.getLatitude());
            setCityFragment();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { /* Empty */ }

        @Override
        public void onProviderEnabled(String provider) {
            @SuppressLint("MissingPermission")
            Location l = mLocManager.getLastKnownLocation(provider);
            lon = String.valueOf(Objects.requireNonNull(l).getLongitude());
            lat = String.valueOf(l.getLatitude());
            setCityFragment();
        }

        @Override
        public void onProviderDisabled(String provider) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

}
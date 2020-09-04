package com.example.geekbrainsandroidweather;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geekbrainsandroidweather.fragments.AddCityFragment;
import com.example.geekbrainsandroidweather.fragments.CitiesDetailsFragment;
import com.example.geekbrainsandroidweather.fragments.Constants;
import com.example.geekbrainsandroidweather.fragments.DevelopersInfoFragment;
import com.example.geekbrainsandroidweather.fragments.SettingsFragment;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.rest.OpenWeatherRepo;
import com.example.geekbrainsandroidweather.rest.entities.weather.WeatherRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initViews();
        setupActionBar();
        setCityFragment();
        setOnClickForSideMenuItems();

    }

    private void initViews() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        progressBar = findViewById(R.id.progressBar);
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
                if (item.getItemId() == R.id.page_3) {
                    replaceFragment(new DevelopersInfoFragment(), R.id.fragmentContainer, true);
                    drawer.close();
                }
                if (item.getItemId() == R.id.page_2) {
                    replaceFragment(new SettingsFragment(), R.id.fragmentContainer, true);
                    drawer.close();
                }
                if (item.getItemId() == R.id.page_1) {
                    replaceFragment(CitiesDetailsFragment.create(cityDetailsData), R.id.fragmentContainer, false);
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
        String defaultCity = "Moscow";
        OpenWeatherRepo.getInstance().getAPI().loadWeather(defaultCity,
                BuildConfig.WEATHER_API_KEY, "metric")
                .enqueue(new Callback<WeatherRequest>() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequest> call,
                                           @NonNull Response<WeatherRequest> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            cityDetailsData = new CityDetailsData(response.body());
                            CitiesDetailsFragment fragment = CitiesDetailsFragment.create(cityDetailsData);
                            replaceFragment(fragment, R.id.fragmentContainer, false);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.AlertDialogCustom);
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

}

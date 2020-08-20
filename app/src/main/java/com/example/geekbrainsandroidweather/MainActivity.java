package com.example.geekbrainsandroidweather;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.geekbrainsandroidweather.fragments.CitiesFragment;
import com.example.geekbrainsandroidweather.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkTheme();
        setContentView(R.layout.activity_main);

        CitiesFragment citiesFragment = new CitiesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.citiesDetailsContainer, citiesFragment);
        fragmentTransaction.commit();
    }

    private void checkTheme() {
        if (SettingsFragment.isDarkTheme) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

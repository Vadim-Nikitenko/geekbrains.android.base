package com.example.geekbrainsandroidweather;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geekbrainsandroidweather.fragments.CitiesDetailsFragment;

public class CitiesDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_details);

        if (savedInstanceState == null) {
            CitiesDetailsFragment details = new CitiesDetailsFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details)
                    .commit();
        } else {
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
    }



}

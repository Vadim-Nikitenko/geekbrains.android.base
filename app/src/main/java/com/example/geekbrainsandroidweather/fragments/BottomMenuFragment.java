package com.example.geekbrainsandroidweather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geekbrainsandroidweather.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomMenuFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setOnMenuClickBehaviour();
    }

    private void setOnMenuClickBehaviour() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_3) {
                    DevelopersInfoFragment developersInfoFragment = new DevelopersInfoFragment();
                    FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.citiesDetailsContainer, developersInfoFragment);
                    fragmentTransaction.commit();
                }
                if (item.getItemId() == R.id.page_2) {
                    SettingsFragment settingsFragment = new SettingsFragment();
                    FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.citiesDetailsContainer, settingsFragment);
                    fragmentTransaction.commit();
                }
                if (item.getItemId() == R.id.page_1) {
                    CitiesFragment citiesFragment = new CitiesFragment();
                    FragmentTransaction fragmentTransaction = requireFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.citiesDetailsContainer, citiesFragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }


    private void initViews(@NonNull View view) {
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
    }

}

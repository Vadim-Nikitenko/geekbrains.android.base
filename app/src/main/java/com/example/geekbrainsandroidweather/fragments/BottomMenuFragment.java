package com.example.geekbrainsandroidweather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geekbrainsandroidweather.DevelopersInfoActivity;
import com.example.geekbrainsandroidweather.MainActivity;
import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomMenuFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private final int requestCode = 12345;

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
                    if (!requireActivity().getClass().equals(DevelopersInfoActivity.class)) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setClass(requireContext(), DevelopersInfoActivity.class);
                        startActivity(intent);
                    }
                }
                if (item.getItemId() == R.id.page_2) {
                    if (!requireActivity().getClass().equals(SettingsActivity.class)) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setClass(requireContext(), SettingsActivity.class);
                        startActivityForResult(intent, requestCode);
                    }
                }
                if (item.getItemId() == R.id.page_1) {
                    if (!requireActivity().getClass().equals(MainActivity.class)) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setClass(requireContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });
    }


    private void initViews(@NonNull View view) {
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
    }

}

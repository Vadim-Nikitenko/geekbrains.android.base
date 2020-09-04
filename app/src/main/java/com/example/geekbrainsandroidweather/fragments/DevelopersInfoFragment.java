package com.example.geekbrainsandroidweather.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.geekbrainsandroidweather.R;

public class DevelopersInfoFragment extends Fragment {
    private TextView developersText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developers_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        requireActivity().findViewById(R.id.appBarLayout).setVisibility(View.VISIBLE);
    }

    private void initViews(View view) {
        developersText = view.findViewById(R.id.developersText);
    }

}
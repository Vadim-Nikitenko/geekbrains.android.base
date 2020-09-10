package com.example.geekbrainsandroidweather.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.recycler_views.RecyclerHistoryAdapter;
import com.example.geekbrainsandroidweather.room.App;
import com.example.geekbrainsandroidweather.room.TemperatureHistoryHelper;
import com.example.geekbrainsandroidweather.room.model.History;

import java.util.List;

public class HistoryFragment extends Fragment {
    private RecyclerHistoryAdapter recyclerHistoryAdapter;
    private RecyclerView historyList;
    private TemperatureHistoryHelper historyHelper;
    private Spinner spinner;
    private List<History> cities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setHistoryByDate();
        setupRecyclerView();
        setupSpinner();
    }


    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sort_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        setHistoryByDate();
                        setupRecyclerView();
                        break;
                    }
                    case 1: {
                        setHistoryByTemp();
                        setupRecyclerView();
                        break;
                    }
                    case 2: {
                        setHistoryByCity();
                        setupRecyclerView();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void init(@NonNull View view) {
        historyList = view.findViewById(R.id.historyList);
        spinner = view.findViewById(R.id.spinnerSort);
        historyHelper = new TemperatureHistoryHelper();
    }


    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (getContext());
        recyclerHistoryAdapter = new RecyclerHistoryAdapter(cities);
        historyList.setLayoutManager(linearLayoutManager);
        if (historyList.getItemDecorationCount() <= 0) {
            DividerItemDecoration decorator = new DividerItemDecoration(requireContext(),
                    LinearLayoutManager.VERTICAL);
            historyList.addItemDecoration(decorator);
        }
        historyList.setAdapter(recyclerHistoryAdapter);
    }

    private void setHistoryByDate() {
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<History> historiesNew = App.getInstance().getEducationDao().selectHistoryByDate();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cities = historiesNew;
                        setupRecyclerView();
                    }
                });
            }
        }).start();
    }

    private void setHistoryByTemp() {
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<History> historiesNew = App.getInstance().getEducationDao().selectHistoryByTemp();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cities = historiesNew;
                        setupRecyclerView();
                    }
                });
            }
        }).start();
    }

    private void setHistoryByCity() {
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<History> historiesNew = App.getInstance().getEducationDao().selectHistoryByCity();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cities = historiesNew;
                        setupRecyclerView();
                    }
                });
            }
        }).start();
    }

}

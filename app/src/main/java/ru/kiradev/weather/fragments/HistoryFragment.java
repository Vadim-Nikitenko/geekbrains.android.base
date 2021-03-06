package ru.kiradev.weather.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.kiradev.weather.R;
import ru.kiradev.weather.recycler_views.RecyclerHistoryAdapter;
import ru.kiradev.weather.room.App;
import ru.kiradev.weather.room.RoomHelper;
import ru.kiradev.weather.room.model.History;

public class HistoryFragment extends Fragment {
    private RecyclerHistoryAdapter recyclerHistoryAdapter;
    private RecyclerView historyList;
    private RoomHelper historyHelper;
    private Spinner spinner;
    private ImageButton deleteHistoryImageButton;
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
        setupRecyclerView();
        setupSpinner();
        setOnDeleteHistoryClickBehaviour();
    }

    private void setOnDeleteHistoryClickBehaviour() {
        deleteHistoryImageButton.setOnClickListener(view -> {
            Handler handler = new Handler();
            RoomHelper roomHelper = new RoomHelper();
            new Thread(() -> {
                roomHelper.deleteAllHistory();
                handler.post(() -> {
                    setHistoryByDate();
                    setupRecyclerView();
                });
            }).start();
        });
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
        deleteHistoryImageButton = view.findViewById(R.id.deleteHistoryImageButton);
        historyHelper = new RoomHelper();
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

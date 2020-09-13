package com.example.geekbrainsandroidweather.recycler_views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.rest.OpenWeatherHelper;
import com.example.geekbrainsandroidweather.room.model.History;

import java.util.List;

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder> {
    private List<History> historyList;

    // конструктор. Адаптер используется в активити или фрагменте
    public RecyclerHistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    // Обращаемся к системному сервису LayoutInflater
    // соединяем каждый элемент массива с layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_history, parent,
                false);
        return new ViewHolder(view);
    }

    // Отображение даннных в view ViewHolder-а
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupViews(historyList.get(position).date,
                historyList.get(position).city,
                historyList.get(position).temperature + "°");
    }

    // возвращает количество элементов в массиве
    @Override
    public int getItemCount() {
        return historyList == null ? 0 : historyList.size();
    }

    // класс для работы с одной view
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView historyDay;
        private TextView historyCity;
        private TextView historyTemp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            historyDay = itemView.findViewById(R.id.historyDay);
            historyCity = itemView.findViewById(R.id.historyCity);
            historyTemp = itemView.findViewById(R.id.historyTemp);
        }

        void setupViews(String day, String temperature, String state) {
            historyDay.setText(day);
            historyCity.setText(temperature);
            historyTemp.setText(state);
        }

    }

}

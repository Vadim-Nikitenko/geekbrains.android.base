package ru.kiradev.weather.recycler_views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.kiradev.weather.R;
import ru.kiradev.weather.room.model.History;

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
        holder.setupViews(parseDate(historyList.get(position).date),
                historyList.get(position).city,
                historyList.get(position).temperature + "°");
    }

    private String parseDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());
        return sdf.format(date);
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

        void setupViews(String day, String city, String temp) {
            historyDay.setText(day);
            historyCity.setText(city);
            historyTemp.setText(temp);
        }

    }

}

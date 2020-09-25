package ru.kiradev.weather.recycler_views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.kiradev.weather.R;
import ru.kiradev.weather.model.ForecastDayData;

public class RecyclerForecastAdapter extends RecyclerView.Adapter<RecyclerForecastAdapter.ViewHolder> {
    private ArrayList<ForecastDayData> forecastDayDataList;

    // конструктор. Адаптер используется в активити или фрагменте
    public RecyclerForecastAdapter(ArrayList<ForecastDayData> forecastDayDataList) {
        this.forecastDayDataList = forecastDayDataList;
    }

    // Обращаемся к системному сервису LayoutInflater
    // соединяем каждый элемент массива с layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_forecast, parent,
                false);
        return new ViewHolder(view);
    }

    // Отображение даннных в view ViewHolder-а
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupViews(forecastDayDataList.get(position).getDay(),
                forecastDayDataList.get(position).getImage(),
                forecastDayDataList.get(position).getTemperature(),
                forecastDayDataList.get(position).getState());
    }

    // возвращает количество элементов в массиве
    @Override
    public int getItemCount() {
        return forecastDayDataList == null ? 0 : forecastDayDataList.size();
    }

    // класс для работы с одной view
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dayTextView;
        private ImageView stateImageView;
        private TextView temperatureImageView;
        private TextView stateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            stateImageView = itemView.findViewById(R.id.stateImageView);
            temperatureImageView = itemView.findViewById(R.id.temperatureImageView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
        }

        void setupViews(String day, String image, String temperature, String state) {
            dayTextView.setText(day);
            temperatureImageView.setText(temperature);
            stateTextView.setText(state);
            Picasso.get().load(image).into(stateImageView);
        }

    }

}

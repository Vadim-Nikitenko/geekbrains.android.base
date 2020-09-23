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
import ru.kiradev.weather.model.HourlyForecastData;

public class RecyclerHourlyDataAdapter extends RecyclerView.Adapter<RecyclerHourlyDataAdapter.ViewHolder> {
    private ArrayList<HourlyForecastData> dataArrayList;
    private HourlyForecastData data;

    public RecyclerHourlyDataAdapter(ArrayList<HourlyForecastData> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public RecyclerHourlyDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_hourly_layout, parent,
                false);
        return new RecyclerHourlyDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHourlyDataAdapter.ViewHolder holder, int position) {
        data = dataArrayList.get(position);
        holder.setHourlyData(data);
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView hourlyTime;
        private ImageView hourlyStateImg;
        private TextView hourlyTemperature;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hourlyTime = itemView.findViewById(R.id.hourlyTime);
            hourlyStateImg = itemView.findViewById(R.id.hourlyStateImg);
            hourlyTemperature = itemView.findViewById(R.id.hourlyTemperature);
        }

        void setHourlyData(HourlyForecastData data) {
            hourlyTime.setText(data.getTime());
            Picasso.get().load(data.getStateImage()).into(hourlyStateImg);
            hourlyTemperature.setText(data.getTemperature());
        }

    }
}

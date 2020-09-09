package com.example.geekbrainsandroidweather.recycler_views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geekbrainsandroidweather.R;
import com.example.geekbrainsandroidweather.rest.entities.city.CityGeoData;

import java.util.ArrayList;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ViewHolder> {
    private ArrayList<CityGeoData> data;
    private final IRVOnItemClick onItemClickCallback;
    private final IRVOnItemClick changeItem;

    // конструктор. Адаптер используется в активити или фрагменте
    public RecyclerDataAdapter(ArrayList<CityGeoData> data, IRVOnItemClick onItemClickCallback, IRVOnItemClick changeItem) {
        this.data = data;
        this.onItemClickCallback = onItemClickCallback;
        this.changeItem = changeItem;
    }

    // Обращаемся к системному сервису LayoutInflater
    // соединяем каждый элемент массива с layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout, parent,
                false);
        return new ViewHolder(view);
    }

    // Отображение даннных в view ViewHolder-а
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = data.get(position).getCity();
        holder.setTextToTextView(text);
        holder.setOnClickForItem(text, position);
        holder.changeView();
        holder.setOnImageButtonClickBehaviour(text);
    }

    // возвращает количество элементов в массиве
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    // класс для работы с одной view
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageButton removeCityBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemTextView);
            removeCityBtn = itemView.findViewById(R.id.removeCityBtn);
        }

        void setTextToTextView(String text) {
            textView.setText(text);
        }

        void setOnImageButtonClickBehaviour(String text) {
            removeCityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove(text);
                }
            });
        }

        void setOnClickForItem(final String text, final int position) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickCallback != null) {
                        onItemClickCallback.onItemClicked(view, position);
                    }
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemClickCallback != null) {
                        onItemClickCallback.onItemLongPressed(view);
                    }
                    return false;
                }
            });
        }

        void changeView() {
            changeItem.changeItem(textView);
        }
    }

    public void remove(final String text) {
        if (data.size() > 0) {
            int m = 0;
            for (int i = 1; i < data.size(); i++) {
                if (data.get(i).equals(text)) {
                    m = i;
                }
            }
            data.remove(m);
            notifyItemRemoved(m);
        }
    }

}

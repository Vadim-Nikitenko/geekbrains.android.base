package com.example.geekbrainsandroidweather.recycler_views;

import android.view.View;
import android.widget.TextView;

public interface IRVOnItemClick {
    void onItemClicked(View view, int position);
    void onItemLongPressed(View view);
    void changeItem(TextView view);
}

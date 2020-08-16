package com.example.geekbrainsandroidweather;

import android.widget.TextView;

public interface IRVOnItemClick {
    void onItemClicked(String text, int position);

    void changeItem(TextView view);
}

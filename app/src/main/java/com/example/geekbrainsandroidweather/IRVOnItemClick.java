package com.example.geekbrainsandroidweather;

import android.view.View;
import android.widget.TextView;

public interface IRVOnItemClick {
    void onItemClicked(View view, String text, int position);
    void onItemLongPressed(View view);
    void changeItem(TextView view);
}

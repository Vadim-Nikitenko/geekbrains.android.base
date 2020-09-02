package com.example.geekbrainsandroidweather.model;

import com.example.geekbrainsandroidweather.fragments.Constants;

import java.util.Locale;

public class ForecastDayData implements Constants {
    private String day;
    private String image;
    private String state;
    private String temperature;

    public String getDay() {
        return day;
    }

    public ForecastDayData withDay(String day) {
        this.day = String.format(Locale.getDefault(), "%s", day);
        return this;
    }

    public String getImage() {
        return image;
    }

    public ForecastDayData withImage(String image) {
        this.image = String.format(Locale.getDefault(), BASE_IMAGE_URL + "%s", image + IMAGE_FORMAT);
        return this;
    }

    public String getTemperature() {
        return temperature;
    }

    public ForecastDayData withTemperature(float temperature) {
        this.temperature = String.format(Locale.getDefault(), "%.0f°", temperature);
        return this;
    }

    public String getState() {
        return state;
    }

    public ForecastDayData withState(String state) {
        this.state = String.format(Locale.getDefault(), "%s", state);
        return this;
    }
}

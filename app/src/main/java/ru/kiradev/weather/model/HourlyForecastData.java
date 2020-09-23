package ru.kiradev.weather.model;

import ru.kiradev.weather.fragments.Constants;

import java.io.Serializable;
import java.util.Locale;

public class HourlyForecastData implements Serializable, Constants {
    private String time;
    private String stateImage;
    private String temperature;


    public String getTime() {
        return time;
    }

    public HourlyForecastData withTime(String time) {
        this.time = String.format(Locale.getDefault(), "%s", time).substring(11, 16);
        return this;
    }

    public String getStateImage() {
        return stateImage;
    }

    public HourlyForecastData withStateImage(String stateImage) {
        this.stateImage = String.format(Locale.getDefault(), BASE_IMAGE_URL + "%s", stateImage)
                + IMAGE_FORMAT;;
        return this;
    }

    public String getTemperature() {
        return temperature;
    }

    public HourlyForecastData withTemperature(float temperature) {
        this.temperature = String.format(Locale.getDefault(), "%.0f", temperature) + "°";
        return this;
    }
}

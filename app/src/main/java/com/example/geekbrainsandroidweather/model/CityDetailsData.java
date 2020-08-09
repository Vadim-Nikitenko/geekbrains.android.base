package com.example.geekbrainsandroidweather.model;

import java.io.Serializable;

public class CityDetailsData implements Serializable {
    private String cityName;
    private int position;
    private int temperature;
    private String state;
    private String dayAndNightTemperature;

    public CityDetailsData withCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public CityDetailsData withPosition(int position) {
        this.position = position;
        return this;
    }

    public CityDetailsData withTemperature(int temperature) {
        this.temperature = temperature;
        return this;
    }

    public CityDetailsData withState(String state) {
        this.state = state;
        return this;
    }

    public CityDetailsData withDayAndNightTemperature(String dayAndNightTemperature) {
        this.dayAndNightTemperature = dayAndNightTemperature;
        return this;
    }

    public String getCityName() {
        return cityName;
    }

    public int getPosition() {
        return position;
    }

    public int getTemperature() {
        return temperature;
    }


    public String getState() {
        return state;
    }

    public String getDayAndNightTemperature() {
        return dayAndNightTemperature;
    }
}

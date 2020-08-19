package com.example.geekbrainsandroidweather.model;

import java.io.Serializable;

public class CityDetailsData implements Serializable {
    private String cityName;
    private int position;
    private String temperature;
    private String state;

    private String pressure;
    private String humidity;
    private String windSpeed;
    private String dayAndNightTemperature;
    private String weatherMainState;

    public CityDetailsData withCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public CityDetailsData withPressure(String pressure) {
        this.pressure = pressure;
        return this;
    }
    public CityDetailsData withHumidity(String humidity) {
        this.humidity = humidity;
        return this;
    }
    public CityDetailsData withWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
        return this;
    }

    public CityDetailsData withPosition(int position) {
        this.position = position;
        return this;
    }

    public CityDetailsData withTemperature(String temperature) {
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

    public CityDetailsData withWeatherMainState(String weatherMainState) {
        this.weatherMainState = weatherMainState;
        return this;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getCityName() {
        return cityName;
    }


    public int getPosition() {
        return position;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getState() {
        return state;
    }

    public String getDayAndNightTemperature() {
        return dayAndNightTemperature;
    }

    @Override
    public String toString() {
        return "CityDetailsData{" +
                "cityName='" + cityName + '\'' +
                ", position=" + position +
                ", temperature='" + temperature + '\'' +
                ", state='" + state + '\'' +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", dayAndNightTemperature='" + dayAndNightTemperature + '\'' +
                '}';
    }

    public String getWeatherMainState() {
        return weatherMainState;
    }
}

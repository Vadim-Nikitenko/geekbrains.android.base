package com.example.geekbrainsandroidweather.model;

import com.example.geekbrainsandroidweather.fragments.Constants;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CityDetailsData implements Serializable, Constants {
    private String cityName;
    private int position;
    private String temperature;
    private String state;
    private String icon;
    private String feelsLikeTemperature;
    private String pressure;
    private String humidity;
    private String windSpeed;
    private String dayAndNightTemperature;
    private String weatherMainState;
    private String cloudy;

    private String sunriseAndSunset;
    private String windDegrees;

    public CityDetailsData withCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public CityDetailsData withIcon(String icon) {
        this.icon = String.format(Locale.getDefault(), BASE_IMAGE_URL + "%s", icon) + IMAGE_FORMAT;
        return this;
    }

    public CityDetailsData withPressure(int pressure) {
        this.pressure = String.format(Locale.getDefault(), " %d", pressure) + " hPa";
        return this;
    }

    public CityDetailsData withHumidity(int humidity) {
        this.humidity = String.format(Locale.getDefault(), " %d", humidity) + "%";;
        return this;
    }

    public CityDetailsData withWindSpeed(float windSpeed) {
        this.windSpeed = String.format(Locale.getDefault(), "%.0f", windSpeed) + " m.s.";
        return this;
    }

    public CityDetailsData withPosition(int position) {
        this.position = position;
        return this;
    }

    public CityDetailsData withTemperature(float temperature) {
        this.temperature = String.format(Locale.getDefault(), "%.0f", temperature) + "°";
        return this;
    }

    public CityDetailsData withState(String state) {
        this.state = String.format(Locale.getDefault(), "%s", state);
        return this;
    }

    public CityDetailsData withDayAndNightTemperature(String dayAndNightTemperature) {
        this.dayAndNightTemperature = dayAndNightTemperature;
        return this;
    }

    public CityDetailsData withWeatherMainState(String weatherMainState) {
        this.weatherMainState = String.format(Locale.getDefault(), "%s", weatherMainState);
        return this;
    }

    public CityDetailsData withFeelsLikeTemperature(float feelsLike) {
        this.feelsLikeTemperature = "Feels like " + String.format(Locale.getDefault(), "%.0f", feelsLike) + "°";
        return this;
    }

    public CityDetailsData withCloudy(int cloudy) {
        this.cloudy = "Cloudy: " + String.format(Locale.getDefault(), "%s", cloudy) + "%";
        return this;
    }

    public CityDetailsData withSunriseAndSunset(long sunrise, long sunset) {
        Date dateSunrise = new Date(sunrise*1000L);
        Date dateSunset = new Date(sunset*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        String rise = sdf.format(dateSunrise);
        String set = sdf.format(dateSunset);
        String sunriseAndSunset = "Sunrise: " + rise + " / " + "Sunset: " + set;
        this.sunriseAndSunset = sunriseAndSunset;
        return this;
    }

    public CityDetailsData withWindDegrees(int windDegrees) {
        this.windDegrees = "Direction " + String.format(Locale.getDefault(), "%s", windDegrees) + "°";
        return this;
    }

    public String getCloudy() {
        return cloudy;
    }

    public String getWindDegrees() {
        return windDegrees;
    }

    public String getSunriseAndSunset() {
        return sunriseAndSunset;
    }

    public String getPressure() {
        return "Pressure: " + pressure;
    }

    public String getHumidity() {
        return "Humidity: " + humidity;
    }

    public String getWindSpeed() {
        return "Wind speed: " +windSpeed;
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

    public String getIcon() {
        return icon;
    }

    public String getFeelsLikeTemperature() {
        return feelsLikeTemperature;
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

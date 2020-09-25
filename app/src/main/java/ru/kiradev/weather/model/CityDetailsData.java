package ru.kiradev.weather.model;

import ru.kiradev.weather.fragments.Constants;
import ru.kiradev.weather.rest.entities.weather.WeatherRequest;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private String defaultIcon;
    private String lat;
    private String lon;
    private long shift;

    public CityDetailsData(WeatherRequest weatherRequest, String lat, String lon) {
        this
                .withCityName(weatherRequest.getName())
                .withTemperature(weatherRequest.getMain().getTemp())
                .withPressure(weatherRequest.getMain().getPressure())
                .withHumidity(weatherRequest.getMain().getHumidity())
                .withWindSpeed(weatherRequest.getWind().getSpeed())
                .withState(weatherRequest.getWeather().get(0).getDescription())
                .withIcon(weatherRequest.getWeather().get(0).getIcon())
                .withFeelsLikeTemperature(weatherRequest.getMain().getFeelsLike())
                .withCloudy(weatherRequest.getClouds().getAll())
                .withWindDegrees(weatherRequest.getWind().getDeg())
                .withSunriseAndSunset(weatherRequest.getSys().getSunrise(), weatherRequest.getSys().getSunset())
                .withWeatherMainState(weatherRequest.getWeather().get(0).getMain())
                .withLat(lat).withLon(lon)
                .withShift(weatherRequest.getTimezone())
                .withDefaultIcon(weatherRequest.getWeather().get(0).getIcon());
    }

    public CityDetailsData(String city, float temperature, String bg, String icon) {
        this
                .withCityName(city)
                .withTemperature(temperature)
                .withDefaultIcon(bg)
                .withIcon(icon);
    }

    public String getLat() {
        return lat;
    }

    public CityDetailsData withLat(String lat) {
        this.lat = lat;
        return this;
    }

    public String getLon() {
        return lon;
    }

    public CityDetailsData withLon(String lon) {
        this.lon = lon;
        return this;
    }

    public String getDefaultIcon() {
        return defaultIcon;
    }

    public CityDetailsData withDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
        return this;
    }

    public CityDetailsData withCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public CityDetailsData withIcon(String icon) {
        this.icon = String.format(Locale.getDefault(), BASE_IMAGE_URL + "%s", icon) + IMAGE_FORMAT;
        return this;
    }

    public CityDetailsData withPressure(int pressure) {
        this.pressure = String.format(Locale.getDefault(), " %.0f", pressure * 0.75) + " мм.рт.ст.";
        return this;
    }

    public CityDetailsData withHumidity(int humidity) {
        this.humidity = String.format(Locale.getDefault(), " %d", humidity) + "%";
        ;
        return this;
    }

    public CityDetailsData withWindSpeed(float windSpeed) {
        this.windSpeed = String.format(Locale.getDefault(), "%.0f", windSpeed) + " м.с.";
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
        this.feelsLikeTemperature = "Ощущается как " + String.format(Locale.getDefault(), "%.0f", feelsLike) + "°";
        return this;
    }

    public CityDetailsData withCloudy(int cloudy) {
        this.cloudy = "Облачность: " + String.format(Locale.getDefault(), "%s", cloudy) + "%";
        return this;
    }

    public CityDetailsData withSunriseAndSunset(long sunrise, long sunset) {
        Date dateSunrise = new Date(sunrise * 1000L);
        Date dateSunset = new Date(sunset * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String rise = sdf.format(dateSunrise);
        String set = sdf.format(dateSunset);
        String sunriseAndSunset = "Восход: " + rise + " / " + "Закат: " + set;
        this.sunriseAndSunset = sunriseAndSunset;
        return this;
    }

    public CityDetailsData withWindDegrees(int windDegrees) {
        this.windDegrees = "Направление " + String.format(Locale.getDefault(), "%s", windDegrees) + "°";
        return this;
    }

    public long getShift() {
        return shift;
    }

    public CityDetailsData withShift(long shift) {
        this.shift = shift;
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
        return "Давление: " + pressure;
    }

    public String getHumidity() {
        return "Влажность: " + humidity;
    }

    public String getWindSpeed() {
        return "Скорость ветра: " + windSpeed;
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

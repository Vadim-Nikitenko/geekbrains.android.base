package com.example.geekbrainsandroidweather.rest;


import com.example.geekbrainsandroidweather.rest.entities.forecast.ForecastRequest;
import com.example.geekbrainsandroidweather.rest.entities.weather.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String city,
                                     @Query("appid") String keyApi,
                                     @Query("units") String units);


    @GET("data/2.5/forecast")
    Call<ForecastRequest> loadForecast(@Query("q") String city,
                                       @Query("appid") String keyApi,
                                       @Query("units") String units);
}

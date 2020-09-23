package ru.kiradev.weather.rest;


import ru.kiradev.weather.rest.entities.forecast.ForecastRequest;
import ru.kiradev.weather.rest.entities.weather.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("lat") String lat,
                                     @Query("lon") String lon,
                                     @Query("appid") String keyApi,
                                     @Query("lang") String lang,
                                     @Query("units") String units);

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeatherByCity(@Query("q") String city,
                                           @Query("appid") String keyApi,
                                           @Query("lang") String lang,
                                           @Query("units") String units);


    @GET("data/2.5/forecast")
    Call<ForecastRequest> loadForecast(@Query("q") String city,
                                       @Query("appid") String keyApi,
                                       @Query("lang") String lang,
                                       @Query("units") String units);

}

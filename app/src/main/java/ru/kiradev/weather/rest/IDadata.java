package ru.kiradev.weather.rest;

import ru.kiradev.weather.rest.entities.city.BodyCityRequest;
import ru.kiradev.weather.rest.entities.city.CityRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IDadata {

    @POST("suggestions/api/4_1/rs/suggest/address")
    Call<CityRequest> loadCity(@Header("Content-Type") String contentType,
                               @Header("Authorization") String apiKey,
                               @Header("X-Secret") String secretKey,
                               @Body BodyCityRequest body);

}

package com.example.geekbrainsandroidweather.rest;

import com.example.geekbrainsandroidweather.rest.entities.city.BodyCityRequest;
import com.example.geekbrainsandroidweather.rest.entities.city.CityRequest;

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

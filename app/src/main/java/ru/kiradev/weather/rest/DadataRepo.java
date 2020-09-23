package ru.kiradev.weather.rest;

import ru.kiradev.weather.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DadataRepo {
    private static DadataRepo instance = null;
    private IDadata API;

    private DadataRepo() {
        API = createAdapter();
    }

    public static DadataRepo getInstance() {
        if (instance == null) {
            instance = new DadataRepo();
        }

        return instance;
    }

    public IDadata getAPI() {
        return API;
    }

    private IDadata createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_DADATA)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(IDadata.class);
    }
}

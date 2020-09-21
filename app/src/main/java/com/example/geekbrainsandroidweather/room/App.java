package com.example.geekbrainsandroidweather.room;

import android.app.Application;

import androidx.room.Room;

import com.example.geekbrainsandroidweather.room.dao.TemperatureHistoryDao;
import com.example.geekbrainsandroidweather.room.model.Favorites;

public class App extends Application {

    private static App instance;

    private TemperatureHistoryDatabase db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(
                getApplicationContext(),
                TemperatureHistoryDatabase.class,
                "temperature_history")
                .build();
    }

    public TemperatureHistoryDao getEducationDao() {
        return db.getTemperatureHistoryDao();
    }
}

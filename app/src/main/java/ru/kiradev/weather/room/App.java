package ru.kiradev.weather.room;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import ru.kiradev.weather.room.dao.TemperatureHistoryDao;

public class App extends Application {

    private static App instance;

    private TemperatureHistoryDatabase db;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
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

package com.example.geekbrainsandroidweather.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.geekbrainsandroidweather.room.dao.TemperatureHistoryDao;
import com.example.geekbrainsandroidweather.room.model.Favorites;
import com.example.geekbrainsandroidweather.room.model.History;

@Database(entities = {History.class, Favorites.class}, version = 1)
public abstract class TemperatureHistoryDatabase extends RoomDatabase {
    public abstract TemperatureHistoryDao getTemperatureHistoryDao();
}

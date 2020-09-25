package ru.kiradev.weather.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.kiradev.weather.room.dao.TemperatureHistoryDao;
import ru.kiradev.weather.room.model.Favorites;
import ru.kiradev.weather.room.model.History;

@Database(entities = {History.class, Favorites.class}, version = 1)
public abstract class TemperatureHistoryDatabase extends RoomDatabase {
    public abstract TemperatureHistoryDao getTemperatureHistoryDao();
}

package com.example.geekbrainsandroidweather.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.geekbrainsandroidweather.room.model.History;

import java.util.List;
import java.util.Set;

// Описание объекта, обрабатывающего данные
// @Dao - доступ к данным
// В этом классе описывается, как будет происходить обработка данных
@Dao
public interface TemperatureHistoryDao {

    // Метод для добавления студента в базу данных
    // @Insert - признак добавления
    // onConflict - что делать, если такая запись уже есть
    // В данном случае просто заменим ее
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(History history);

    // Метод для замены данных студента
    @Update
    void updateHistory(History history);

    // Удалим данные студента
    @Delete
    void deleteHistory(History history);

    // Удалим данные студента, просто зная ключ
    @Query("SELECT * FROM history order by date desc LIMIT 30")
    List<History> selectHistoryByDate();

    @Query("SELECT * FROM history order by city asc, date desc LIMIT 30")
    List<History> selectHistoryByCity();

    @Query("SELECT * FROM history order by temperature desc, city desc  LIMIT 30")
    List<History> selectHistoryByTemp();


}

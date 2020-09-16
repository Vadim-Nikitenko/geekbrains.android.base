package com.example.geekbrainsandroidweather.room.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

// Таблица со списком электронных почт студента
// связана по полям id со стороны таблицы student и
// student_id со стороны таблицы email (внешний ключ)
// При удалении студента, и все почтовые адреса тоже удаляются (CASCADE)
@Entity(indices = {@Index(value = {"city", "date"},
        unique = true)})
public class History {

    public History(String city, int temperature, String  date) {
        this.city = city;
        this.temperature = temperature;
        this.date = date;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String city;
    public int temperature;
    public String date;


}

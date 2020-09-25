package ru.kiradev.weather.room.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"city"},
        unique = true)})
public class Favorites {

    public Favorites(String city, String lat, String  lon) {
        this.city = city;
        this.lat = lat;
        this.lon = lon;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String city;
    public String lat;
    public String lon;

}

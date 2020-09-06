package com.example.geekbrainsandroidweather.rest.entities.city;

public class CityGeoData {
    private String city;
    private String lat;
    protected String lon;

    public CityGeoData(String city, String lat, String lon) {
        this.city = city;
        this.lat = lat;
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "CityGeoData{" +
                "city='" + city + '\'' +
                '}';
    }
}

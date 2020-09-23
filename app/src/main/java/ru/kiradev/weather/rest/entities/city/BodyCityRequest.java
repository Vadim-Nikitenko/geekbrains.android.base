package ru.kiradev.weather.rest.entities.city;

public class BodyCityRequest {
    private String query;

    public BodyCityRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}

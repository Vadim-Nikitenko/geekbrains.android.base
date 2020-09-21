package com.example.geekbrainsandroidweather.room;

import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.room.model.Favorites;
import com.example.geekbrainsandroidweather.room.model.History;

import java.util.List;

public class RoomHelper {

    public void insertTemperature(CityDetailsData cityDetailsData) {
        new Thread(() -> {
            History history = new History(cityDetailsData.getCityName(),
                    Integer.parseInt(cityDetailsData.getTemperature().replace("°", "")), System.currentTimeMillis());
            App.getInstance().getEducationDao().insertHistory(history);
        }).start();
    }

    public void insertFavorite(CityDetailsData cityDetailsData) {
        new Thread(() -> {
            Favorites favorite = new Favorites(
                    cityDetailsData.getCityName(),
                    cityDetailsData.getLat(),
                    cityDetailsData.getLon());

            App.getInstance().getEducationDao().insertFavorites(favorite);
        }).start();
    }

    public void deleteFavorite(String city) {
        new Thread(() -> {
            App.getInstance().getEducationDao().deleteFavorites(city);
        }).start();
    }

    public List<Favorites> getFavorites() {
        return App.getInstance().getEducationDao().selectFavorites();
    }

    public List<History> getHistory() {
        return App.getInstance().getEducationDao().selectHistoryByDate();
    }
    public void deleteAllHistory() {
        App.getInstance().getEducationDao().deleteAllHistory();
    }

}

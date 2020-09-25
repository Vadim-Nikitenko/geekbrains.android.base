package ru.kiradev.weather.room;

import ru.kiradev.weather.model.CityDetailsData;
import ru.kiradev.weather.room.model.Favorites;
import ru.kiradev.weather.room.model.History;

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

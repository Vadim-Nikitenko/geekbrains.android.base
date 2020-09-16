package com.example.geekbrainsandroidweather.room;

import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.rest.OpenWeatherHelper;
import com.example.geekbrainsandroidweather.room.model.History;

import java.util.List;

public class TemperatureHistoryHelper {

    public void insertTemperature(CityDetailsData cityDetailsData) {
        new Thread(() -> {
            History history = new History(cityDetailsData.getCityName(),
                    Integer.parseInt(cityDetailsData.getTemperature().replace("°", "")),
                    OpenWeatherHelper.parseDate(System.currentTimeMillis()));
            App.getInstance().getEducationDao().insertHistory(history);
        }).start();
    }

    public List<History> getHistory() {

        return App.getInstance().getEducationDao().selectHistoryByDate();
    }
}

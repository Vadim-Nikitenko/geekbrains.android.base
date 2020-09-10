package com.example.geekbrainsandroidweather.rest;

import android.annotation.SuppressLint;

import com.example.geekbrainsandroidweather.fragments.Constants;
import com.example.geekbrainsandroidweather.model.ForecastDayData;
import com.example.geekbrainsandroidweather.model.HourlyForecastData;
import com.example.geekbrainsandroidweather.rest.entities.forecast.ForecastRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class OpenWeatherHelper implements Constants {
    public static ArrayList<String> daysOfWeek = new ArrayList<>(Arrays.asList("Вс.", "Пн.", "Вт.", "Ср.", "Чт.", "Пт.", "Сб."));

    public ArrayList<HourlyForecastData> setHourlyData(ForecastRequest forecastRequest) {
        ArrayList<HourlyForecastData> hourlyForecastDataList = new ArrayList<>();
        final int HOURS_INTERVAL_A_DAY = 8;
        for (int i = 0; i < HOURS_INTERVAL_A_DAY; i++) {
            HourlyForecastData hourlyForecastData = new HourlyForecastData()
                    .withStateImage(forecastRequest.getList().get(i).getWeather().get(0).getIcon())
                    .withTemperature(forecastRequest.getList().get(i).getMain().getTemp())
                    .withTime(forecastRequest.getList().get(i).getDtTxt());
            hourlyForecastDataList.add(hourlyForecastData);
        }
        return hourlyForecastDataList;
    }

    public ArrayList<ForecastDayData> setForecastData(ForecastRequest forecastRequest) {
        ArrayList<ForecastDayData> weatherForTheWeek = new ArrayList<>();
        final int HOURS_INTERVAL_A_DAY = 8;
        for (int i = HOURS_INTERVAL_A_DAY; i < forecastRequest.getList().size(); i += 8) {
            weatherForTheWeek.add(new ForecastDayData()
                    .withDay(parseDate(forecastRequest.getList().get(i).getDtTxt()))
                    .withTemperature(forecastRequest.getList().get(i).getMain().getTemp())
                    .withImage(forecastRequest.getList().get(i).getWeather().get(0).getIcon())
                    .withState(forecastRequest.getList().get(i).getWeather().get(0).getDescription()));
        }
        return weatherForTheWeek;
    }

    public static String parseDate(String date) {
        final int CHARS_TO_REMOVE_COUNT = 10;
        Calendar cal = GregorianCalendar.getInstance();
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM", Locale.getDefault());
            Date date1 = format.parse(date);
            cal.setTime(Objects.requireNonNull(date1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        date = daysOfWeek.get(dayOfWeek) + " " + date.substring(5, CHARS_TO_REMOVE_COUNT);
        return date;
    }

    public static String parseDate(long date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return simpleDateFormat.format(calendar.getTime());
    }

    public String getDayAndNightTemperature(ForecastRequest forecastRequest) {
        float dayTemp = forecastRequest.getList().get(0).getMain().getTempMax();
        float nightTemp = forecastRequest.getList().get(0).getMain().getTempMin();
        final int HOURS_INTERVALS_IN_ONE_DAY = 8;
        for (int i = 1; i < HOURS_INTERVALS_IN_ONE_DAY; i++) {
            if (forecastRequest.getList().get(i).getMain().getTempMax() > dayTemp) {
                dayTemp = forecastRequest.getList().get(i).getMain().getTempMax();
            }
            if (forecastRequest.getList().get(i).getMain().getTempMax() < nightTemp) {
                nightTemp = forecastRequest.getList().get(i).getMain().getTempMin();
            }
        }
        return String.format(Locale.getDefault(),
                "%.0f° / %.0f°", dayTemp, nightTemp);
    }

}

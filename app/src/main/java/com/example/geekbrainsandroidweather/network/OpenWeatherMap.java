package com.example.geekbrainsandroidweather.network;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.geekbrainsandroidweather.BuildConfig;
import com.example.geekbrainsandroidweather.fragments.Constants;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.model.ForecastDayData;
import com.example.geekbrainsandroidweather.model.HourlyForecastData;
import com.example.geekbrainsandroidweather.model.forecast.ForecastRequest;
import com.example.geekbrainsandroidweather.model.weather.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class OpenWeatherMap implements Constants {
    public static CityDetailsData cityDetailsData;
    public static ArrayList<ForecastDayData> weatherForTheWeek;
    public static ArrayList<HourlyForecastData> hourlyForecastList;
    public static int responseCode;
    private ArrayList<String> daysOfWeek = new ArrayList<>(Arrays.asList("Sun.", "Mon.", "Tue.", "Wen.", "Sur.", "Fri.", "Sat."));

    public void makeRequest(String cityName) {
        try {
            Thread t1 = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                public void run() {
                    try {
                        Gson gson = new Gson(); // преобразование данных запроса в модель
                        String weatherResult = getInputStreamData(new URL(
                                getWeatherURL(cityName) + BuildConfig.WEATHER_API_KEY), null);
                        final WeatherRequest weatherRequest = gson.fromJson(weatherResult, WeatherRequest.class);
                        String forecastResult = getInputStreamData(new URL(
                                getForecastUrl(cityName) + BuildConfig.WEATHER_API_KEY), null);
                        final ForecastRequest forecastRequest = gson.fromJson(forecastResult, ForecastRequest.class);

                        setWeatherData(weatherRequest, forecastRequest);
                        setForecastData(forecastRequest);
                        setHourlyData(forecastRequest);

                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                        responseCode = 500;
                    }
                }
            });
            t1.start();
            t1.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
            responseCode = 501;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected String getInputStreamData(URL uri, HttpsURLConnection urlConnection) throws IOException {
        urlConnection = (HttpsURLConnection) uri.openConnection();
        responseCode = urlConnection.getResponseCode();
        urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
        urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
        return getLines(in);
    }

    private URL getWeatherURL(String cityName) throws MalformedURLException {
        return new URL(BASE_URL + "weather?q=" + cityName + "&units=metric&appid=");
    }

    private URL getForecastUrl(String cityName) throws MalformedURLException {
        return new URL(BASE_URL + "forecast?q=" + cityName + "&units=metric&appid=");
    }

    private String getLines(BufferedReader reader) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;

        while (true) {
            try {
                tempVariable = reader.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawData.toString();
    }

    private void setHourlyData(ForecastRequest forecastRequest) {
        hourlyForecastList = new ArrayList<>();
        final int HOURS_INTERVAL_A_DAY = 8;
        for (int i = 0; i < HOURS_INTERVAL_A_DAY; i++) {
            HourlyForecastData hourlyForecastData = new HourlyForecastData()
                    .withStateImage(forecastRequest.getList().get(i).getWeather().get(0).getIcon())
                    .withTemperature(forecastRequest.getList().get(i).getMain().getTemp())
                    .withTime(forecastRequest.getList().get(i).getDtTxt());
            hourlyForecastList.add(hourlyForecastData);
        }

    }

    private void setWeatherData(WeatherRequest weatherRequest, ForecastRequest forecastRequest) {
        cityDetailsData = new CityDetailsData()
                .withCityName(weatherRequest.getName())
                .withTemperature(weatherRequest.getMain().getTemp())
                .withPressure(weatherRequest.getMain().getPressure())
                .withHumidity(weatherRequest.getMain().getHumidity())
                .withWindSpeed(weatherRequest.getWind().getSpeed())
                .withState(weatherRequest.getWeather()[0].getDescription())
                .withIcon(weatherRequest.getWeather()[0].getIcon())
                .withFeelsLikeTemperature(weatherRequest.getMain().getFeelsLike())
                .withDayAndNightTemperature(getDayAndNightTemperature(forecastRequest))
                .withCloudy(weatherRequest.getClouds().getAll())
                .withWindDegrees(weatherRequest.getWind().getDeg())
                .withSunriseAndSunset(weatherRequest.getSys().getSunrise(), weatherRequest.getSys().getSunset())
                .withWeatherMainState(weatherRequest.getWeather()[0].getMain());
    }

    public void setForecastData(ForecastRequest forecastRequest) {
        weatherForTheWeek = new ArrayList<>();
        final int HOURS_INTERVAL_A_DAY = 8;
        for (int i = HOURS_INTERVAL_A_DAY; i < forecastRequest.getList().size(); i += 8) {
            weatherForTheWeek.add(new ForecastDayData()
                    .withDay(parseDate(forecastRequest.getList().get(i).getDtTxt()))
                    .withTemperature(forecastRequest.getList().get(i).getMain().getTemp())
                    .withImage(forecastRequest.getList().get(i).getWeather().get(0).getIcon())
                    .withState(forecastRequest.getList().get(i).getWeather().get(0).getMain()));
        }
    }

    private String parseDate(String date) {
        final int CHARS_TO_REMOVE_COUNT = 9;
        Calendar cal = GregorianCalendar.getInstance();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = format.parse(date);
            cal.setTime(Objects.requireNonNull(date1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        date = daysOfWeek.get(dayOfWeek) + " " + date.substring(5, CHARS_TO_REMOVE_COUNT);
        return date;
    }

    private String getDayAndNightTemperature(ForecastRequest forecastRequest) {
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

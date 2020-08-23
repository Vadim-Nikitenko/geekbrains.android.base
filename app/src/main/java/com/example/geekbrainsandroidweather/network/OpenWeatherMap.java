package com.example.geekbrainsandroidweather.network;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.geekbrainsandroidweather.BuildConfig;
import com.example.geekbrainsandroidweather.fragments.Constants;
import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.model.HourlyForecastData;
import com.example.geekbrainsandroidweather.model.forecast.ForecastRequest;
import com.example.geekbrainsandroidweather.model.weather.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class OpenWeatherMap implements Constants {
    private static CityDetailsData cityDetailsData;
    public static ArrayList<String> weatherForTheWeek;
    public static ArrayList<HourlyForecastData> hourlyForecastList;
    public static int responseCode;
    public static final int FORECAST_DAYS = 5;

    public OpenWeatherMap() {
    }

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
                        getWeatherData(weatherRequest);


                        String forecastResult = getInputStreamData(new URL(
                                getForecastUrl(cityName) + BuildConfig.WEATHER_API_KEY), null);
                        final ForecastRequest forecastRequest = gson.fromJson(forecastResult, ForecastRequest.class);
                        getForecastData(forecastRequest);
                        getHourlyData(forecastRequest);

                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    }
                }
            });
            t1.start();
            t1.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected String getInputStreamData(URL uri, HttpsURLConnection urlConnection) throws IOException {
        urlConnection = (HttpsURLConnection) uri.openConnection();
        responseCode = urlConnection.getResponseCode();
        urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
        urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
        String result = getLines(in);
        return result;
    }

    private URL getWeatherURL(String cityName) throws MalformedURLException {
        return new URL(BASE_URL + "weather?q=" + cityName + "&units=metric&appid=");
    }

    private URL getForecastUrl(String cityName) throws MalformedURLException {
        return new URL(BASE_URL + "forecast?q=" + cityName + "&units=metric&appid=");
    }


    public CityDetailsData getCityDetailsData() {
        return cityDetailsData;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    private void getHourlyData(ForecastRequest forecastRequest) {
        hourlyForecastList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String temperature = Math.round(Float.parseFloat(String.format(Locale.getDefault(),
                    "%.2f", forecastRequest.getList().get(i).getMain().getTemp()))) + "°";
            String icon = String.format(Locale.getDefault(),
                    BASE_IMAGE_URL + "%s", forecastRequest.getList().get(i).getWeather().get(0).getIcon())
                    + IMAGE_FORMAT;
            String time = String.format(Locale.getDefault(),
                    "%s", forecastRequest.getList().get(i).getDtTxt()).substring(11, 16);

            HourlyForecastData hourlyForecastData = new HourlyForecastData(time, icon, temperature);
            hourlyForecastList.add(hourlyForecastData);
        }

    }

    private void getWeatherData(WeatherRequest weatherRequest) {
        String temperatureValue = Math.round(Float.parseFloat(String.format(Locale.getDefault(),
                "%.2f", weatherRequest.getMain().getTemp()))) + "°";
        String pressureText = String.format(Locale.getDefault(),
                " %d", weatherRequest.getMain().getPressure());
        String humidityStr = String.format(Locale.getDefault(),
                " %d", weatherRequest.getMain().getHumidity());
        String windSpeedStr = " " + Math.round(Float.parseFloat(String.format(Locale.getDefault(),
                "%f", weatherRequest.getWind().getSpeed()))) + " m.s.";
        String state = String.format(Locale.getDefault(),
                "%s", weatherRequest.getWeather()[0].getDescription());
        String dayAndNightTemperature = String.format(Locale.getDefault(),
                "%.0f° / %.0f°", weatherRequest.getMain().getTempMin(),
                weatherRequest.getMain().getTempMax());
        String weatherMainState = String.format(Locale.getDefault(),
                "%s", weatherRequest.getWeather()[0].getMain());
        String icon = String.format(Locale.getDefault(),
                BASE_IMAGE_URL + "%s", weatherRequest.getWeather()[0].getIcon())
                + IMAGE_FORMAT;

        cityDetailsData = new CityDetailsData()
                .withCityName(weatherRequest.getName())
                .withTemperature(temperatureValue)
                .withPressure(pressureText)
                .withHumidity(humidityStr)
                .withWindSpeed(windSpeedStr)
                .withState(state)
                .withIcon(icon)
                .withDayAndNightTemperature(dayAndNightTemperature)
                .withWeatherMainState(weatherMainState);
    }

    public static void getForecastData(ForecastRequest forecastRequest) {
        weatherForTheWeek = new ArrayList<>();
        weatherForTheWeek.clear();
        for (int i = 0; i < FORECAST_DAYS; i++) {
            String date = String.format(Locale.getDefault(),
                    "%s", forecastRequest.getList().get(i).getDtTxt());
            String dayAndNightTemperature = String.format(Locale.getDefault(),
                    "%.0f° / %.0f°", forecastRequest.getList().get(i).getMain().getTempMin(),
                    forecastRequest.getList().get(i).getMain().getTempMax());
            String state = String.format(Locale.getDefault(),
                    "%s", forecastRequest.getList().get(i).getWeather().get(0).getMain());
            final int CHARS_TO_REMOVE_COUNT = 3;
            weatherForTheWeek.add(removeLastChars(date, CHARS_TO_REMOVE_COUNT) + "  " + dayAndNightTemperature + "  " + state);
        }
    }

    public static String removeLastChars(String str, int chars) {
        final int BEGIN_INDEX = 0;
        return str.substring(BEGIN_INDEX, str.length() - chars);
    }


}

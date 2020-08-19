package com.example.geekbrainsandroidweather.network;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.geekbrainsandroidweather.model.CityDetailsData;
import com.example.geekbrainsandroidweather.model.weather.WeatherRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class Network {
    private static final String TAG = "WEATHER";
    private static CityDetailsData cityDetailsData;
    private final String API_KEY = "5f1d9d03941da3156da553bbcb7740c4";

    public Network(String cityName) {
        makeRequest(cityName);
    }

    public void makeRequest(String cityName) {
        try {
            final URL uri = new URL(getWeatherURL(cityName) + API_KEY);
            Thread t1 = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                public void run() {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                        urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                        String result = getLines(in);
                        // преобразование данных запроса в модель
                        Gson gson = new Gson();
                        final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                        // Возвращаемся к основному потоку
                        cityDetailsData = getWeatherData(weatherRequest);
                        Log.i("cityDetailsData", cityDetailsData.toString());
                    } catch (Exception e) {
                        Log.e(TAG, "Fail connection", e);
                        e.printStackTrace();
                    } finally {
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                }
            });
            t1.start();
            t1.join();
        } catch (MalformedURLException | InterruptedException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private String getWeatherURL(String cityName) {
        return "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=";
    }

    public CityDetailsData getCityDetailsData() {
        return cityDetailsData;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    private CityDetailsData getWeatherData(WeatherRequest weatherRequest) {

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
                "%.2f° / %.2f°", weatherRequest.getMain().getTempMin(),
                weatherRequest.getMain().getTempMax());
        String weatherMainState = String.format(Locale.getDefault(),
                "%s", weatherRequest.getWeather()[0].getMain());

        return new CityDetailsData()
                .withCityName(weatherRequest.getName())
                .withTemperature(temperatureValue)
                .withPressure(pressureText)
                .withHumidity(humidityStr)
                .withWindSpeed(windSpeedStr)
                .withState(state)
                .withDayAndNightTemperature(dayAndNightTemperature)
                .withWeatherMainState(weatherMainState);
    }

}

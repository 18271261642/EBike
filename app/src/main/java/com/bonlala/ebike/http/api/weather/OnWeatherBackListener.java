package com.bonlala.ebike.http.api.weather;

/**
 * Created by Admin
 * Date 2022/4/11
 */
public interface OnWeatherBackListener {

    void backCurrWeather(WeatherApi.WeatherBean weatherBean);
}

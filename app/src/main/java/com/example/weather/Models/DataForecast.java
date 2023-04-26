package com.example.weather.Models;

public class DataForecast {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(String wind_speed) {
        this.wind_speed = wind_speed;
    }

    public DataForecast(String time, String temperature, String icon, String wind_speed) {
        this.temperature = temperature;
        this.time = time;
        this.icon = icon;
        this.wind_speed = wind_speed;
    }

    private String time;
    private String icon;
    private String wind_speed;
}

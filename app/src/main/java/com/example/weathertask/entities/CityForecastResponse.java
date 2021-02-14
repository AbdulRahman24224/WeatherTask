package com.example.weathertask.entities;

public class CityForecastResponse {
    public CityForecastResponse() { }
    String minTemperature;
    String maxTempTemperature;
    String description;
    String windSpeed;

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTempTemperature() {
        return maxTempTemperature;
    }

    public void setMaxTempTemperature(String maxTempTemperature) {
        this.maxTempTemperature = maxTempTemperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    Long dateTime ;


    String error;
    String cityName ;

    @Override
    public String toString() {
        return "CityForecastResponse{" +
                "minTemperature='" + minTemperature + '\'' +
                ", maxTempTemperature='" + maxTempTemperature + '\'' +
                ", description='" + description + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", dateTime=" + dateTime +
                ", error='" + error + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }

}

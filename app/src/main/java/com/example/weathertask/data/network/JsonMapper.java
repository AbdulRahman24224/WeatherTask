package com.example.weathertask.data.network;

import android.util.Log;

import com.example.weathertask.entities.CityForecastResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonMapper {

    public JsonMapper() {
    }


    public CityForecastResponse getCityWeatherFromJson(JSONObject response , String cityName ) {

        CityForecastResponse cityResponse = new CityForecastResponse();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            Log.d("JSON-success", jsonObject.toString());

            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObject = (JSONObject) weatherArray.get(0);

            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject wind = jsonObject.getJSONObject("wind");

            cityResponse.setDescription(weatherObject.getString("description"));
            cityResponse.setMaxTempTemperature(String.valueOf(main.getDouble("temp_max")));
            cityResponse.setMinTemperature(String.valueOf(main.getDouble("temp_min")));
            cityResponse.setWindSpeed(String.valueOf(wind.getDouble("speed")));
            cityResponse.setCityName(jsonObject.getString("name"));

            return cityResponse;
        } catch (JSONException e) {
            Log.d("JSON-Failure", "error");
            e.printStackTrace() ;
            cityResponse.setError("format exception");
            cityResponse.setCityName(cityName);
            return cityResponse;
        }
    }

    public ArrayList<CityForecastResponse> getCityForecastFromJson(JSONObject response) {

        ArrayList<CityForecastResponse> responseArrayList = new ArrayList<CityForecastResponse>();
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            Log.d("JSON-success", jsonObject.toString());

            JSONArray weatherArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < weatherArray.length(); i++) {
                try {
                    CityForecastResponse cityResponse = new CityForecastResponse();
                    JSONObject forecastObject = (JSONObject) weatherArray.get(i);

                    JSONObject main = forecastObject.getJSONObject("main");
                    JSONObject wind = forecastObject.getJSONObject("wind");
                    JSONArray objectWeatherArray = forecastObject.getJSONArray("weather");
                    JSONObject weatherObject = (JSONObject) objectWeatherArray.get(0);
                    Long datetime = forecastObject.getLong("dt");

                    cityResponse.setDateTime(datetime);
                    cityResponse.setDescription(weatherObject.getString("description"));
                    cityResponse.setMaxTempTemperature(String.valueOf(main.getDouble("temp_max")));
                    cityResponse.setMinTemperature(String.valueOf(main.getDouble("temp_min")));
                    cityResponse.setWindSpeed(String.valueOf(wind.getDouble("speed")));
                    responseArrayList.add(cityResponse);
                } catch (JSONException e) {
                    CityForecastResponse cityResponse = new CityForecastResponse();
                    cityResponse.setError("failed itme parsing");
                    responseArrayList.add(cityResponse);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseArrayList;
    }
}

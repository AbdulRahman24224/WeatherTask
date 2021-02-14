package com.example.weathertask.data.repository;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.weathertask.entities.CityForecastResponse;
import com.example.weathertask.data.network.JsonMapper;
import com.example.weathertask.data.network.VolleyClient;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;

public class WeatherRepository {

    // Todo inject Application Context
 private JsonMapper jsonMapper = new JsonMapper();

    private String baseUrl = "http://api.openweathermap.org/data/2.5/";

    public WeatherRepository() {
    }

    public @io.reactivex.rxjava3.annotations.NonNull Observable<CityForecastResponse> requestCityWeather(String cityName, Context context) {
        return Observable.create(singleSubscriber -> {
            String url = baseUrl + "weather?q=" + cityName + "&appid=bd5a862a5796e6ff18a590d0608c5210";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            CityForecastResponse cityResponse = jsonMapper.getCityWeatherFromJson(response , cityName);
                            singleSubscriber.onNext(cityResponse);

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CityForecastResponse cityResponse = new CityForecastResponse();
                            String errorText = "";
                            if (error.getLocalizedMessage() == null) errorText = "Request Error";
                            else errorText = error.getLocalizedMessage();
                            cityResponse.setError(errorText);
                            cityResponse.setCityName(cityName);
                            singleSubscriber.onNext(cityResponse);
                        }
                    });

            VolleyClient.getInstance(context).addToRequestQueue(jsonObjectRequest);
        });
    }


    public @io.reactivex.rxjava3.annotations.NonNull Observable<ArrayList<CityForecastResponse>> requestCityForecast(double lat, double lon, Context context) {
        return Observable.create(singleSubscriber -> {
            String url = baseUrl + "forecast?lat=" + lat + "&lon=" + lon + "&appid=bd5a862a5796e6ff18a590d0608c5210";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            ArrayList<CityForecastResponse> cityResponse = jsonMapper.getCityForecastFromJson(response);
                            singleSubscriber.onNext(cityResponse);

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CityForecastResponse cityResponse = new CityForecastResponse();
                            String errorText = "";
                            if (error.getLocalizedMessage() == null) errorText = "Request Error";
                            else errorText = error.getLocalizedMessage();
                            cityResponse.setError(errorText);
                            ArrayList<CityForecastResponse> responseErrorArrayList = new ArrayList<CityForecastResponse>();
                            responseErrorArrayList.add(cityResponse);
                            singleSubscriber.onNext(responseErrorArrayList);
                        }
                    });

            VolleyClient.getInstance(context).addToRequestQueue(jsonObjectRequest);
        });
    }


}

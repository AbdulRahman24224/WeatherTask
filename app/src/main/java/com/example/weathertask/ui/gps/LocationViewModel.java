package com.example.weathertask.ui.gps;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weathertask.entities.CityForecastResponse;
import com.example.weathertask.data.repository.WeatherRepository;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LocationViewModel extends AndroidViewModel {

    // todo inject context
    private MutableLiveData<ArrayList<CityForecastResponse>> cityForecastLD = new MutableLiveData<ArrayList<CityForecastResponse>>();

    public MutableLiveData<String> getErrorLD() {
        return errorLD;
    }

    private MutableLiveData<String> errorLD = new MutableLiveData<String>();

    public MutableLiveData<Boolean> getProgressLD() {
        return progressLD;
    }

    private MutableLiveData<Boolean> progressLD = new MutableLiveData<Boolean>();
    private WeatherRepository repository = new WeatherRepository();
    private CompositeDisposable disposables = new CompositeDisposable();

    Context context  = getApplication().getApplicationContext() ;

    public LocationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<CityForecastResponse>> getCitiesLD() {
        return cityForecastLD;
    }

    void onNewCoordinate(double lat , double lng) {

                Disposable obsSearch =repository.requestCityForecast(lat,lng , context)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(s ->{ progressLD.postValue(true);} )
                        .subscribe(o -> {
                            Log.d("onSuccess" , o.toString());
                            cityForecastLD.postValue(o);
                            progressLD.postValue(false);
                                }
                                ,
                                throwable -> {
                                    Log.d("onError" , throwable.getLocalizedMessage());
                                    progressLD.postValue(false);
                                });
              disposables.add(obsSearch);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();

    }

}

package com.example.weathertask.ui.search;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.weathertask.entities.CityForecastResponse;
import com.example.weathertask.R;
import com.example.weathertask.data.repository.WeatherRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;


public class SearchViewModel extends AndroidViewModel {


    private MutableLiveData<CityForecastResponse> citiesLD = new MutableLiveData<CityForecastResponse>();

    public MutableLiveData<String> getErrorLD() {
        return errorLD;
    }
    private MutableLiveData<String> errorLD = new MutableLiveData<String>();
    private WeatherRepository repository = new WeatherRepository();
    private CompositeDisposable disposables = new CompositeDisposable();
    public MutableLiveData<Boolean> getProgressLD() {
        return progressLD;
    }

    private MutableLiveData<Boolean> progressLD = new MutableLiveData<Boolean>();
    // todo inject context
    Context context = getApplication().getApplicationContext();

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<CityForecastResponse> getCitiesLD() {
        return citiesLD;
    }

    void onSearchButtonClicked(String textInput) {

        if (matchesRegex(textInput)) {
            @Nullable
            List<String> cities = validateCitiesCount(textInput);
            if (cities != null) {
                Disposable obsSearch = BehaviorSubject.fromIterable(cities)
                        .flatMap(new Function<String, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(String s) throws Throwable {

                                return repository.requestCityWeather(s, context);
                            }
                        })
                        .zipWith(Observable.interval(300, TimeUnit.MILLISECONDS), (item, interval) -> item)
                        .doOnSubscribe(s->{progressLD.postValue(true);})
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                                    Log.d("subscribe", ((CityForecastResponse) o).toString());
                                    progressLD.postValue(false);
                                    citiesLD.postValue((CityForecastResponse) o);
                                },
                                throwable -> {
                                    Log.d("onError", throwable.getLocalizedMessage());
                                    progressLD.postValue(false);
                                });
                disposables.add(obsSearch);
            } else errorLD.postValue(context.getString(R.string.error_cities_count));

        } else errorLD.postValue(context.getString(R.string.error_city_input_format));

    }

    Boolean matchesRegex(String textInput) {
        return textInput.matches("^[\\wa-z A-Z\\w,\\w]*$");
    }

    List<String> validateCitiesCount(String textInput) {

        List<String> citiesList = Arrays.asList(textInput.split(","));
        ArrayList<String> cittiesArrayList = new ArrayList<String>(citiesList);

        for (int i = 0; i < cittiesArrayList.size(); i++) {
            if (cittiesArrayList.get(i).trim().isEmpty()) cittiesArrayList.remove(i);
        }

        if (cittiesArrayList.size() >= 3 && cittiesArrayList.size() <= 7) return cittiesArrayList;
        else return null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();

    }

}

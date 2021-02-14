package com.example.weathertask.ui.search;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weathertask.entities.CityForecastResponse;
import com.example.weathertask.databinding.FragmentCitiesWeatherBinding;
import com.example.weathertask.helper.Utils;
import com.example.weathertask.ui.gps.ForecastAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    FragmentCitiesWeatherBinding binding = null;
    ForecastAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCitiesWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        observeViewModel();
        initUI();
    }

    private void observeViewModel() {
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        mViewModel.getCitiesLD().observe(getViewLifecycleOwner(), new Observer<CityForecastResponse>() {
            @Override
            public void onChanged(CityForecastResponse cityResponse) {
                Log.d("LDCity" , cityResponse.toString()) ;
             adapter.addItem(cityResponse);
            }
        });

        mViewModel.getErrorLD().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Utils.toast(requireContext() , error);
            }
        });

        mViewModel.getProgressLD().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)binding.progressBar.setVisibility(View.VISIBLE); else  binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initUI() {

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.onSearchButtonClicked(binding.etCityName.getText().toString()) ;
                adapter.clear();
            }
        });

        // set up the RecyclerView
        binding.rvWeather.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new ForecastAdapter(new ArrayList<CityForecastResponse>());
        binding.rvWeather.setAdapter(adapter);
    }

}
package com.example.weathertask.ui.gps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathertask.entities.CityForecastResponse;
import com.example.weathertask.databinding.ItemWeatherBinding;
import com.example.weathertask.helper.Utils;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    public ArrayList<CityForecastResponse> forecastArrayList;

    public  ForecastAdapter(ArrayList<CityForecastResponse> forecastArrayList) {
        this.forecastArrayList = forecastArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemWeatherBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(forecastArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return forecastArrayList.size();
    }
    public void addItems (ArrayList<CityForecastResponse> items){
        forecastArrayList = items;
        notifyDataSetChanged();
    }

    public void addItem (CityForecastResponse item){
        forecastArrayList.add(item) ;
        notifyDataSetChanged();
    }
    public void clear(){
        forecastArrayList= new ArrayList<CityForecastResponse>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemWeatherBinding binding;

        public ViewHolder(ItemWeatherBinding itemWeatherBinding) {
            super(itemWeatherBinding.getRoot());
            binding =itemWeatherBinding ;
        }

      void bind (CityForecastResponse obj){

          if (obj.getError() != null) {
              binding.fields.setVisibility(View.GONE);
              binding.tvError.setVisibility(View.VISIBLE);
              binding.tvCityName.setText(obj.getCityName());
          }else {
              binding.tvError.setVisibility(View.GONE);
              binding.fields.setVisibility(View.VISIBLE);
              binding.tvMin.setText(obj.getMinTemperature());
              binding.tvMax.setText(obj.getMaxTempTemperature());
              binding.tvDescription.setText(obj.getDescription());
              binding.tvWind.setText(obj.getWindSpeed());
              if (obj.getDateTime()!=null){
                  binding.tvCityName.setText(Utils.toDate(obj.getDateTime()));
              }else { binding.tvCityName.setText(obj.getCityName()); }


          }


      }
    }
}

package com.example.weathertask.ui.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weathertask.entities.CityForecastResponse;
import com.example.weathertask.R;
import com.example.weathertask.databinding.FragmentCityForecastBinding;
import com.example.weathertask.helper.LocationUtils;
import com.example.weathertask.helper.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.util.ArrayList;

public class LocationFragment extends Fragment {

    private LocationViewModel mViewModel;
    FragmentCityForecastBinding binding = null;
    FusedLocationProviderClient mFusedLocationProviderClient = null;
    LocationRequest mLocationRequest = null;
    ForecastAdapter adapter = null;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCityForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        observeViewModel();
        initUI();
    }

    private void observeViewModel() {
        mViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        mViewModel.getCitiesLD().observe(getViewLifecycleOwner(), new Observer<ArrayList<CityForecastResponse>>() {
            @Override
            public void onChanged(ArrayList<CityForecastResponse> cityForecastArraylist) {
                Log.d("LDonSuccess", cityForecastArraylist.toString());
                adapter.addItems(cityForecastArraylist);

            }
        });


        mViewModel.getErrorLD().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Utils.toast(requireActivity(), error);
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

        binding.btnCaptureLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationUpdates();
                adapter.clear();
            }
        });

        // set up the RecyclerView
        binding.rvForecast.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new ForecastAdapter(new ArrayList<CityForecastResponse>());
        binding.rvForecast.setAdapter(adapter);
    }

    private void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        requestLoationUpdate();
    }

    @SuppressLint("MissingPermission")
    private void requestLoationUpdate() {
        if (checkLocationPermission() && LocationUtils.isGPSEnabled(requireContext())) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                    Looper.myLooper());
        } else {
            askForPermission();
        }

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            actOnNewLocation(locationResult.getLastLocation());
        }
    };

    void actOnNewLocation(Location location) {

        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        Log.d("newCoo", String.valueOf(latitude) + String.valueOf(longitude));
       Address address=  LocationUtils.getAddressfromCoordinates(requireContext() , latitude , longitude) ;
       if (address!=null && address.getAdminArea()!=null ) binding.tvCityName.setText(address.getAdminArea());
        stopLocationUpdates();
        mViewModel.onNewCoordinate(latitude, longitude);
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(requireContext())
                        .setTitle("Asking for location permission")
                        .setMessage("give location permission")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                askForPermission();
                            }
                        })
                        .create()
                        .show();


            } else {
                askForPermission();
            }
            return false;
        } else {
            return true;
        }
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        startLocationUpdates();
                    }

                } else {
                    Utils.toast(requireActivity(), getString(R.string.per_location_needed));
                }
                return;
            }

        }
    }


}
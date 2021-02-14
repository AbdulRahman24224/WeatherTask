package com.example.weathertask.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.weathertask.R;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class LocationUtils {


    public static  boolean isGPSEnabled(Context context) {
        LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.enable_GPS)
                    .setMessage("To Get Your location info you need to open gps")
                    .setPositiveButton("open", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    })
                    .create()
                    .show();
        }
        return enabled;
    }

    public static Address getAddressfromCoordinates(Context context, Double latitude, Double longitude){
        Address address=null ;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1) ;
            if (addresses != null) {
                address = addresses.get(0) ;

                Log.d("My Current address", address.toString());
            } else {
                Log.d("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }

        return address ;
    }

}

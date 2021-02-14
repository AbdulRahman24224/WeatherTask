package com.example.weathertask.helper;

import android.content.Context;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {
    public static  void toast(Context context , String message) {
    Toast.makeText(context, message,
    Toast.LENGTH_LONG).show();
    }

    public static   String toDate(long timestamp) {
        Date date = new Date(timestamp * 1000);
        return new SimpleDateFormat("EEEE K a").format(date);
    }
}

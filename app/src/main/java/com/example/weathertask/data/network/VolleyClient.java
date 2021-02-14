package com.example.weathertask.data.network;

import android.content.Context;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyClient {
    private static VolleyClient mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private VolleyClient(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyClient getInstance(Context context){

        if(mInstance == null){
            mInstance = new VolleyClient(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue(){

        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public<T> void addToRequestQueue(Request<T> request){

        getRequestQueue().add(request);
    }
}
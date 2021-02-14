package com.example.weathertask.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.weathertask.R;
import com.example.weathertask.ui.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }
}
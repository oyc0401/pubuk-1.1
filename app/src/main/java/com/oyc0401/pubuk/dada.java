package com.oyc0401.pubuk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.oyc0401.pubuk.databinding.ActivityDadaBinding;
import com.oyc0401.pubuk.databinding.ActivityMainBinding;

public class dada extends AppCompatActivity {



private ActivityDadaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada);
        binding=ActivityDadaBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);







    }
}
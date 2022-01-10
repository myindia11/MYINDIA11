package com.example.india11.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.india11.R;

public class SplashScreen extends AppCompatActivity {
    ImageView logo, appName, splashBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this,MainActivity.class));
            finish();
        }, 1500);

    }
}
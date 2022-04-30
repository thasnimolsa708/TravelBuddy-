package com.example.hotelmanagement.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelmanagement.R;
import com.example.hotelmanagement.data.prefrence.SessionManager;
import com.example.hotelmanagement.ui.hotel.HomeActivity;
import com.example.hotelmanagement.ui.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT = 2000;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sessionManager.isLoggedin()) {
                    Intent i;

                        i = new Intent(SplashActivity.this,
                                HomeActivity.class);
                    startActivity(i);
                    finishAffinity();
                } else {
                    Intent i = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                    finishAffinity();
                    //the current activity will get finished.
                }
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
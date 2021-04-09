package com.example.mail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mail.MainActivity;
import com.example.mail.R;

public class Splashscreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT =4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(new Runnable() {

                                      @Override
                                      public void run() {
                                          Intent intent= new Intent(Splashscreen.this, MainActivity.class  );
                                          startActivity(intent);
                                          finish();
                                      }
                                  },
                SPLASH_TIME_OUT);
    }
}
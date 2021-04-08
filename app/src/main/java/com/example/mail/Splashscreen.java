package com.example.mail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splashscreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT =4000;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
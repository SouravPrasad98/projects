package com.example.mail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mail.MainActivity;
import com.example.mail.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splashscreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(new Runnable() {

                                      @Override
                                      public void run() {
                                          startActivity(new Intent(Splashscreen.this, Wholeseller_LoginActivity.class));
                                            finish();
                                      }
                                  },
                SPLASH_TIME_OUT);
    }
}



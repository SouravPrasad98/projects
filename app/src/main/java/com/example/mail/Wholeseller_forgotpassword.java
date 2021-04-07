package com.example.mail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Wholeseller_forgotpassword extends AppCompatActivity {
    private ImageButton backbutt;
    private EditText username;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller_forgotpassword);

        backbutt = findViewById(R.id.backbutt);
        username = findViewById(R.id.username);
        login = findViewById(R.id.login);

        backbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

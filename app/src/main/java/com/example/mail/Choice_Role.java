package com.example.mail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Choice_Role extends AppCompatActivity {
    private TextView textView4;
    private Button button5, button6, button7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice__role);

     textView4 = findViewById(R.id.textView4);
     button5 = findViewById(R.id.button5);
     button6 = findViewById(R.id.button6);
     button7 = findViewById(R.id.button7);

button5.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
startActivity(new Intent(Choice_Role.this, Wholeseller_LoginActivity.class));
    }
});
    }
}
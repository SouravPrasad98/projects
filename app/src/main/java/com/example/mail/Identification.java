package com.example.mail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class Identification extends AppCompatActivity {
    EditText phone;
    Button register;

    CountryCodePicker ccp;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);


        phone= findViewById(R.id.phone);//t1


        register = findViewById(R.id.Next);//b1
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phone);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(getApplicationContext(), PhoneNAuthen.class);
                intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
                //registration with firebase


            }

        });




    }
}
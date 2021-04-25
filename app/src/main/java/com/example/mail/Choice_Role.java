package com.example.mail;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Choice_Role extends AppCompatActivity {
    private TextView textView4;
    private Button button5, button6, button7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice__role);
        init();
     textView4 = findViewById(R.id.textView4);
     button5 = findViewById(R.id.button5);
     button6 = findViewById(R.id.button6);
     button7 = findViewById(R.id.button7);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Customer_login.class);
                startActivity(intent);
            }
        });

     button6.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(getApplicationContext(),Retailer_login.class);
             startActivity(intent);
         }
     });

    button7.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
startActivity(new Intent(Choice_Role.this, Wholeseller_login.class));
    }
});
    }
    private void init(){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.SEND_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
        .check();
    }
}

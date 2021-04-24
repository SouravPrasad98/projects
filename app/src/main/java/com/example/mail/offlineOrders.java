package com.example.mail;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class offlineOrders extends AppCompatActivity {
    private DatePicker datePicker;
    private FirebaseAuth fauth;
    private TextView time_retailer;
    private Button placeOrderRetailer;
    private int timeHour , timeMinutes;
    String Date ;
    String Time;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_orders);

        time_retailer = findViewById(R.id.Time_Retailer);
        placeOrderRetailer = findViewById(R.id.PlaceOderRetailer);
        fauth = FirebaseAuth.getInstance();
        datePicker = findViewById(R.id.datePicker);

        datePicker.setMinDate(System.currentTimeMillis()-1000);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int i, int i2, int i1) {
                String date = (i1) + "/" + i2 + "/" +  i;
                Date = date;
            }
        });


        time_retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        offlineOrders.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                timeHour = hourOfDay;
                                timeMinutes = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0 , 0 ,0,timeHour,timeMinutes);
                                time_retailer.setText(DateFormat.format("hh:mm aa",calendar));
                                String time = hourOfDay + ":" + minute;
                                Time = time;
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(timeHour,timeMinutes);
                timePickerDialog.show();
            }
        });

        placeOrderRetailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOderOfflineRetailer();
            }
        });
    }

    private void placeOderOfflineRetailer() {
       Intent intent = new Intent(Intent.ACTION_INSERT);
       intent.setData(CalendarContract.Events.CONTENT_URI);
       intent.putExtra(CalendarContract.Events.TITLE, "Your ORDER");
       intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Your order will be delivered at your doorstep");
       intent.putExtra(CalendarContract.Instances.START_DAY,Date);

        intent.putExtra(CalendarContract.Events.HAS_ALARM,1);

        intent.putExtra(CalendarContract.Events.ALL_DAY,0);
        intent.putExtra(CalendarContract.Reminders.MINUTES,30);
       intent.putExtra(Intent.EXTRA_EMAIL,"alishanext@gmail.com");
       intent.putExtra(Intent.EXTRA_PHONE_NUMBER,"9467524776");

       if(intent.resolveActivity(getPackageManager())!= null){
           startActivity(intent);
    }
    else{
        Toast.makeText(offlineOrders.this,"CAlender App not found or supported in your device", LENGTH_SHORT).show();
    }
}}
package com.example.mail;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPromotionCodeActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private EditText promoCodeEt, promoDescriptionEt, promoPriceEt, minimumOrderPriceEt;
    private TextView expireDateTv;
    private Button addBtn;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promotion_code);


        backBtn =  findViewById(R.id.backBtn);
        promoCodeEt =  findViewById(R.id.promoCodeEt);
        promoDescriptionEt =  findViewById(R.id.promoDescriptionEt);
        promoPriceEt =  findViewById(R.id.promoPriceEt);
        minimumOrderPriceEt =  findViewById(R.id.minimumOrderPriceEt);
        expireDateTv =  findViewById(R.id.expireDateTv);
        addBtn =  findViewById(R.id.addBtn);

        firebaseAuth = FirebaseAuth.getInstance();
//        progressDialog.setTitle("Please Wait");
//        progressDialog.setCanceledOnTouchOutside(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        expireDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickDialog();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }




    private void datePickDialog() {
        Calendar c = Calendar.getInstance();
        int nYear = c.get(Calendar.YEAR);
        int nMonth = c.get(Calendar.MONTH);
        int nDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DecimalFormat nFormat = new DecimalFormat("00");
                String pDay = nFormat.format(dayOfMonth);
                String pMonth = nFormat.format(monthOfYear);
                String pYear = ""+ year;
                String pDate = pDay + "/" + pMonth + "/" + pYear;

                expireDateTv.setText(pDate);
            }
        }, nYear, nMonth, nDay);

        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

    }

    private String description, promoCode, promoPrice, minimumOrderPrice, expireDate;
    private void inputData() {
        promoCode = promoCodeEt.getText().toString().trim();
        description = promoDescriptionEt.getText().toString().trim();
        promoPrice = promoPriceEt.getText().toString().trim();
        minimumOrderPrice = minimumOrderPriceEt.getText().toString().trim();
        expireDate = expireDateTv.getText().toString().trim();

        if(TextUtils.isEmpty(promoCode)){
            Toast.makeText(this, "Enter discount Code...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter description...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(promoPrice)){
            Toast.makeText(this, "Enter promotion Price...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(minimumOrderPrice)){
            Toast.makeText(this, "Enter minimum Order Price...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(expireDate)){
            Toast.makeText(this, "Choose expiry Date...", Toast.LENGTH_SHORT).show();
            return;
        }

        addDataToDb();

    }

    private void addDataToDb() {
        String timestamp = "" + System.currentTimeMillis();

        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("id", "" + timestamp);
        hashmap.put("timestamp", "" + timestamp);
        hashmap.put("description", "" + description);
        hashmap.put("promoCode", "" + promoCode);
        hashmap.put("promoPrice", "" + promoPrice);
        hashmap.put("minimumOrderPrice", "" + minimumOrderPrice);
        hashmap.put("expireDate", "" + expireDate);

        DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.child(firebaseAuth.getUid()).child("Promotions").child(timestamp)
                .setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //progressDialog.dismiss();
                        Toast.makeText(AddPromotionCodeActivity.this,"Promotion Code added...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // progressDialog.dismiss();
                        Toast.makeText(AddPromotionCodeActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
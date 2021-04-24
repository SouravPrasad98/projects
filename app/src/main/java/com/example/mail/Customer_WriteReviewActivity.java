package com.example.mail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Customer_WriteReviewActivity extends AppCompatActivity {
private String Shopuid;
private ImageButton backBtn;
private TextView shopNameTv;
private ImageView profileIv;
private RatingBar ratingBar;
private EditText reviewEt;
private FirebaseAuth firebaseAuth;
    private FloatingActionButton submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        profileIv = findViewById(R.id.profileIv);
        backBtn = findViewById(R.id.backBtn);
        shopNameTv = findViewById(R.id.shopNameTv);
        ratingBar = findViewById(R.id.ratingBar);
        reviewEt= findViewById(R.id.reviewEt);
        submitBtn= findViewById(R.id.submitBtn);

        firebaseAuth = FirebaseAuth.getInstance();

        Shopuid = getIntent().getStringExtra("uid");
        loadMyReview();
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
                Intent intent = new Intent(Customer_WriteReviewActivity.this, Customer_activity.class);
                startActivity(intent);
            }
        });
    }

    private void loadShopInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Retailer");
                ref.child(Shopuid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String shopName = "" + snapshot.child("bussinessname").getValue();
                        String shopImage = "" + snapshot.child("profileimage").getValue();

                        shopNameTv.setText(shopName);
                        try{
                            Picasso.get().load(shopImage).placeholder(R.drawable.ic_baseline_store_24).into(profileIv);
                        }
                        catch (Exception e){
                            profileIv.setImageResource(R.drawable.ic_baseline_store_24);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadMyReview() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Retailer");
        ref.child(Shopuid).child("Ratings").child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String uid = ""+ snapshot.child("uid").getValue();
                            String ratings = ""+ snapshot.child("ratings").getValue();
                            String review = ""+ snapshot.child("review").getValue();
                            String timestamp = ""+ snapshot.child("timestamp").getValue();

                            float myRating = Float.parseFloat(ratings);
                            ratingBar.setRating(myRating);
                            reviewEt.setText(review);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void inputData() {
        String ratings = "" +ratingBar.getRating();
        String review = reviewEt.getText().toString().trim();
        String timestamp = "" + System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", firebaseAuth.getUid());
        hashMap.put("ratings", ""+ ratings);
        hashMap.put("review", ""+ review);
        hashMap.put("timestamp", ""+timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Retailer");
        ref.child(Shopuid).child("Ratings").child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Customer_WriteReviewActivity.this, "Review Published Successfully...", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Customer_WriteReviewActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });



    }
}
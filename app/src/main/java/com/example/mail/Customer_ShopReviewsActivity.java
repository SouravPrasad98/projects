package com.example.mail;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Customer_ShopReviewsActivity extends AppCompatActivity {
private String shopUid;

    private TextView ratingsTv;
   private ImageView profileIv;
   private FirebaseAuth firebaseAuth;
   private AdapterReview adapterReview;
   private ArrayList<ModelReview> reviewArrayList;
   private ImageButton reviewsBtn,addproduct;
   private RatingBar ratingBar;
   private TextView shopNameTv;
   private RecyclerView reviewsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_reviews);
        shopUid = getIntent().getStringExtra("uid");
        ratingsTv = findViewById(R.id.ratingsTv);
        profileIv = findViewById(R.id.profileIv);
        ratingBar =  findViewById(R.id.ratingBar);
        reviewsRv = findViewById(R.id.reviewsRv);
        shopNameTv = findViewById(R.id.shopNameTv);

        firebaseAuth= FirebaseAuth.getInstance();
        loadShopDetails();
        loadReviews();


    }
    private float ratingSum = 0;
    private void loadReviews() {
        reviewArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Retailer");
        ref.child(shopUid).child("Ratings")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reviewArrayList.clear();
                        ratingSum = 0;
                        for (DataSnapshot ds: snapshot.getChildren()){
                            float rating = Float.parseFloat(""+ ds.child("ratings").getValue());
                            ratingSum = ratingSum+rating;

                            ModelReview modelReview = ds.getValue(ModelReview.class);
                            reviewArrayList.add(modelReview);
                        }

                        adapterReview = new AdapterReview(Customer_ShopReviewsActivity.this, reviewArrayList);
                        reviewsRv.setAdapter(adapterReview);

                        long numberOfReviews = snapshot.getChildrenCount();
                        float avgRating = ratingSum/numberOfReviews;

                        ratingsTv.setText(String.format("%.2f",avgRating) + "[" + numberOfReviews + "]");
                        ratingBar.setRating(avgRating);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Retailer");
        ref.child(shopUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String shopname = "" + snapshot.child("bussinessname").getValue();
                        String profileimage = "" +snapshot.child("profileimage").getValue();

                        shopNameTv.setText(shopname);
                        try{
                            Picasso.get().load(profileimage).placeholder(R.drawable.ic_baseline_store_24).into(profileIv);
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
}
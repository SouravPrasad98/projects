package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mail.common.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Retailer_main_activity1 extends AppCompatActivity {

    private TextView bussnm,profile_name,productstab,orderstab;
    private ImageButton logoutbt, addproduct;
    private ImageView profileIv;
    private long backpressedTime;
    private Button showproducts;
    private RelativeLayout productsRl, ordersRl;
    private RecyclerView orderRv;
    private ArrayList<ModelOrderRetailer> orderList;
    private AdapterOrderRetailer adapterOrderRetailer;

    private FirebaseAuth firebaseAuth;

    private TextView others,Biscuits,beverages,breakfastdairy,eggmeat,frozenfood,
            fruitsandveg,foodgrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_main_activity1);

        productstab= findViewById(R.id.productstab);
        showproducts = findViewById(R.id.showproducts);
        bussnm = findViewById(R.id.bussnm);
        logoutbt = findViewById(R.id.logoutbt);
        productsRl = findViewById(R.id.productsRl);
        ordersRl = findViewById(R.id.ordersRl);
        orderRv = findViewById(R.id.orderRv);
        addproduct = findViewById(R.id.addproduct);
        profile_name = findViewById(R.id.profile_name);
        profileIv = findViewById(R.id.profileIv);
        orderstab = findViewById(R.id.orderstab);
        others =findViewById(R.id.others);
        firebaseAuth = FirebaseAuth.getInstance();
        Biscuits =findViewById(R.id.Biscuits);
        beverages =findViewById(R.id.beverages);
        breakfastdairy =findViewById(R.id.breakfastdairy);
        eggmeat =findViewById(R.id.eggmeat);
        frozenfood =findViewById(R.id.frozenfood);
        fruitsandveg =findViewById(R.id.fruitsandveg);
        foodgrain =findViewById(R.id.foodgrain);

        checkuser();


        productstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductsUi();
            }
        });

        orderstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrdersUi();

            }
        });

        showproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Showall_Retailerproducts.class);
                startActivity(intent);
            }
        });


        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Retailer_activity.class);
                intent.putExtra("Category", "Others");
                startActivity(intent);
            }
        });

        logoutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkuser();
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Retailer_main_activity1.this,Retailer_addproduct.class));

            }
        });

        Biscuits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Retailer_activity.class);
                intent.putExtra("Category", "Biscuits Snacks and Chocolates");
                startActivity(intent);
            }
        });

        foodgrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Retailer_activity.class);
                intent.putExtra("Category", "Foodgrains, Oil and Masala");
                startActivity(intent);
            }
        });

        frozenfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Retailer_activity.class);
                intent.putExtra("Category", "Frozen Food");
                startActivity(intent);
            }
        });

        fruitsandveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Retailer_activity.class);
                intent.putExtra("Category", "Fuits and Vegetables");
                startActivity(intent);
            }
        });


        eggmeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Retailer_activity.class);
                intent.putExtra("Category", "Eggs,Meat,Fish");
                startActivity(intent);
            }
        });

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Retailer_activity.class);
                intent.putExtra("Category", "Beverages");
                startActivity(intent);
            }
        });


        breakfastdairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Retailer_main_activity1.this,Retailer_activity.class);
                intent.putExtra("Category", "Breakfast and Dairy");
                startActivity(intent);
            }
        });


    }

    private void showProductsUi() {
        productsRl.setVisibility(View.VISIBLE);
        ordersRl.setVisibility(View.GONE);
        productstab.setTextColor(getResources().getColor(R.color.black));
        productstab.setBackgroundResource(R.drawable.shaperec01);
        orderstab.setTextColor(getResources().getColor(R.color.white));
        orderstab.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }


    private void showOrdersUi() {

        ordersRl.setVisibility(View.VISIBLE);
        productsRl.setVisibility(View.GONE);
        orderstab.setTextColor(getResources().getColor(R.color.black));
        orderstab.setBackgroundResource(R.drawable.shaperec01);
        productstab.setTextColor(getResources().getColor(R.color.white));
        productstab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void checkuser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user==null){
            startActivity(new Intent(Retailer_main_activity1.this, Choice_Role.class));
            finish();
        }
        else
        {
            loadmyinfo();
        }

    }
    private void loadmyinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Retailer");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name = "" + ds.child("bussinessname").getValue();
                            String accountype = "" + ds.child("accounttype").getValue();
                            String profilename = ""+ ds.child("name").getValue();
                            String profileimage= "" + ds.child("profileimage").getValue();

                            Constants.wname= "" + ds.child("name").getValue();
                            Constants.waddress= ""+ ds.child("address").getValue();
                            Constants.wemail= "" + ds.child("email").getValue();
                            Constants.wphonenumber= "" + ds.child("phonenumber").getValue();
                            Constants.wcountry= "" + ds.child("country").getValue();
                            Constants.wcity= "" + ds.child("city").getValue();
                            Constants.wlatitude= "" + ds.child("latitude").getValue();
                            Constants.wlongitude= "" + ds.child("longitude").getValue();
                            Constants.wdeliveryfee= "" + ds.child("deliveryfee").getValue();
                            Constants.wbussinessname= "" + ds.child("bussinessname").getValue();
                            Constants.wstate= "" + ds.child("state").getValue();
                            Constants.wuid= "" + ds.child("uid").getValue();
                            Constants.wprofileimage= "" + ds.child("profileimage").getValue();



                            bussnm.setText(name + "("+accountype+")");
                            profile_name.setText(profilename);
                            try{
                                Picasso.get().load(profileimage).placeholder(R.drawable.ic_baseline_person_24).into(profileIv);
                            }
                            catch (Exception e){
                                profileIv.setImageResource(R.drawable.ic_baseline_person_24);
                            }
                            loadOrders();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Retailer_main_activity1.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });
    }

    private void loadOrders() {

        orderList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String uid = "" + ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders").child("Orders");
                    ref.orderByChild("orderBy").equalTo(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        for(DataSnapshot ds: dataSnapshot.getChildren())
                                        {
                                            ModelOrderRetailer modelOrderRetailer = ds.getValue(ModelOrderRetailer.class);
                                            orderList.add(modelOrderRetailer);
                                        }
                                        adapterOrderRetailer = new AdapterOrderRetailer(Retailer_main_activity1.this,orderList);
                                        orderRv.setAdapter(adapterOrderRetailer);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {


        if(backpressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            Intent intent= new Intent(Retailer_main_activity1.this, Retailer_login.class);
            startActivity(intent);
            return;
        }
        else{
            Toast.makeText(getBaseContext(), "Press Back again to exit", Toast.LENGTH_SHORT).show();
        }
        backpressedTime= System.currentTimeMillis();

    }
}



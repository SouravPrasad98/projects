package com.example.mail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mail.common.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Customer_activity extends AppCompatActivity {

    private TextView bussnm,profile_name,productstab,orderstab;
    private ImageButton logoutbt, addproduct,settingsBtn;
    private ImageView profileIv;
    private long backpressedTime;
    private Button showproducts;
    private RelativeLayout productsRl;
//    private RecyclerView orderRv;
//    private ArrayList<ModelOrderRetailer> orderList;
//    private AdapterOrderRetailer adapterOrderRetailer;

    private FirebaseAuth firebaseAuth;

    private TextView others,Biscuits,beverages,breakfastdairy,eggmeat,frozenfood,
            fruitsandveg,foodgrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_activity);

        productstab= findViewById(R.id.productstab);
        showproducts = findViewById(R.id.showproducts);
        bussnm = findViewById(R.id.bussnm);
        logoutbt = findViewById(R.id.logoutbt);
        productsRl = findViewById(R.id.productsRl);
        settingsBtn = findViewById(R.id.settingsBtn);
        //orderRv = findViewById(R.id.orderRv);
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





        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_activity.this,Customer_activity2.class);
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



        Biscuits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_activity.this,Customer_activity2.class);
                intent.putExtra("Category", "Biscuits Snacks and Chocolates");
                startActivity(intent);
            }
        });

        foodgrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_activity.this,Customer_activity2.class);
                intent.putExtra("Category", "Foodgrains, Oil and Masala");
                startActivity(intent);
            }
        });

        frozenfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_activity.this,Customer_activity2.class);
                intent.putExtra("Category", "Frozen Food");
                startActivity(intent);
            }
        });

        fruitsandveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_activity.this,Customer_activity2.class);
                intent.putExtra("Category", "Fuits and Vegetables");
                startActivity(intent);
            }
        });


        eggmeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_activity.this,Customer_activity2.class);
                intent.putExtra("Category", "Eggs,Meat,Fish");
                startActivity(intent);
            }
        });

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_activity.this,Customer_activity2.class);
                intent.putExtra("Category", "Beverages");
                startActivity(intent);
            }
        });


        breakfastdairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_activity.this,Customer_activity2.class);
                intent.putExtra("Category", "Breakfast and Dairy");
                startActivity(intent);
            }
        });
            settingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Customer_activity.this,SettingsActivity.class));
                }
            });

    }

    private void showProductsUi() {
        productsRl.setVisibility(View.VISIBLE);

        productstab.setTextColor(getResources().getColor(R.color.black));
        productstab.setBackgroundResource(R.drawable.shaperec01);
        orderstab.setTextColor(getResources().getColor(R.color.white));
        orderstab.setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }



    private void checkuser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user==null){
            startActivity(new Intent(Customer_activity.this, Choice_Role.class));
            finish();
        }
        else
        {
            loadmyinfo();
        }

    }
    private void loadmyinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            String profilename = ""+ ds.child("name").getValue();
                            String profileimage= "" + ds.child("profileimage").getValue();


                            profile_name.setText(profilename);
                            try{
                                Picasso.get().load(profileimage).placeholder(R.drawable.ic_baseline_person_24).into(profileIv);
                            }
                            catch (Exception e){
                                profileIv.setImageResource(R.drawable.ic_baseline_person_24);
                            }
                            //loadOrders();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Customer_activity.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });
    }

//    private void loadOrders() {
//
//        orderList = new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                orderList.clear();
//                for(DataSnapshot ds : dataSnapshot.getChildren())
//                {
//                    String uid = "" + ds.getRef().getKey();
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders").child("Orders");
//                    ref.orderByChild("orderBy").equalTo(firebaseAuth.getUid())
//                            .addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if(dataSnapshot.exists())
//                                    {
//                                        for(DataSnapshot ds: dataSnapshot.getChildren())
//                                        {
//                                            ModelOrderRetailer modelOrderRetailer = ds.getValue(ModelOrderRetailer.class);
//                                            orderList.add(modelOrderRetailer);
//                                        }
//                                        adapterOrderRetailer = new AdapterOrderRetailer(Retailer_main_activity1.this,orderList);
//                                        orderRv.setAdapter(adapterOrderRetailer);
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    @Override
    public void onBackPressed() {


        if(backpressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            Intent intent= new Intent(Customer_activity.this, Retailer_login.class);
            startActivity(intent);
            return;
        }
        else{
            Toast.makeText(getBaseContext(), "Press Back again to exit", Toast.LENGTH_SHORT).show();
        }
        backpressedTime= System.currentTimeMillis();

    }
}



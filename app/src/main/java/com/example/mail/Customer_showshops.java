package com.example.mail;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mail.Retailer.WholesellerListItem;
import com.example.mail.common.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Customer_showshops extends AppCompatActivity {
    private Customer_AdapterRetailerShops adapterWholesellerShops;
    private TextView productstab,orderstab;
    private ImageView profileIv;

    private RelativeLayout productsRl, ordersRl;
    private RecyclerView productsRv,orderRv;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelOrderRetailer> orderList;
    private Customer_AdapterOrderRetailer adapterOrderRetailer;



    private ArrayList<WholesellerListItem> wholesellerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_showshops);
        productstab= findViewById(R.id.productstab);
        orderRv= findViewById(R.id.orderRv);
        //logoutbt = findViewById(R.id.logoutbt);
        profileIv = findViewById(R.id.profileIv);
        productsRv = findViewById(R.id.productsRv);
        profileIv = findViewById(R.id.profileIv);
        firebaseAuth = FirebaseAuth.getInstance();
        orderstab = findViewById(R.id.orderstab);
        productsRl =  findViewById(R.id.shopsRl);
        ordersRl = findViewById(R.id.ordersRl);

        Bundle extras =   getIntent().getExtras();
        if(extras!=null)
        {
            Map<String, WholesellerListItem> wholesellerListItems = new HashMap<>();
           // wholesellerListItems =(List<WholesellerListItem>) extras.get("WholesellerList");
            wholesellerListItems = (Map<String, WholesellerListItem>)extras.getSerializable("RetailerList");

            wholesellerList = new ArrayList<>(wholesellerListItems.values());
            System.out.println(wholesellerList);
        }
        showShopUi();
//        loadmyinfo();
        loadOrders();
        loadAllShops();



        orderstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrdersUi();

            }
        });

        productstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShopUi();
            }
        });

//        logoutbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firebaseAuth.signOut();
//
//            }
//        });



    }
    private void loadmyinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer");
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


//                            try{
//                                Picasso.get().load(profileimage).placeholder(R.drawable.ic_baseline_person_24).into(profileIv);
//                            }
//                            catch (Exception e){
//                                profileIv.setImageResource(R.drawable.ic_baseline_person_24);
//                            }
                            loadOrders();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Customer_showshops.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });
    }

    private void loadOrders() {

        orderList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CustomerOnlineOrders");
        ref.orderByChild("orderBy").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds: snapshot.getChildren())
                            {
                                ModelOrderRetailer modelOrderRetailer = ds.getValue(ModelOrderRetailer.class);
                                orderList.add(modelOrderRetailer);
                            }
                            adapterOrderRetailer = new Customer_AdapterOrderRetailer(Customer_showshops.this,orderList);
                            orderRv.setAdapter(adapterOrderRetailer);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }




    private void loadAllShops() {
//        productList =new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wholeseller");
//        reference.child(firebaseAuth.getUid()).child("Products").child("wholesellerList")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        productList.clear();
//                        for(DataSnapshot ds: snapshot.getChildren()){
//                            RetailerProductModel retailerProductModel = ds.getValue(RetailerProductModel.class);
//                            productList.add(retailerProductModel);
//                        }
//                        adapterWholesellerShops = new AdapterWholesellerShops(Retailer_showshops.this, productList);
//                        productsRv.setAdapter(adapterWholesellerShops);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        Location locationA = new Location("");
        Location locationB = new Location("");
        Location locationC = new Location("");
//        fixed location for retailer
        locationA.setLatitude(Double.parseDouble(Constants.wlatitude));
        locationA.setLongitude(Double.parseDouble(Constants.wlongitude));

        Collections.sort(wholesellerList, new Comparator<WholesellerListItem>() {
            @Override
            public int compare(WholesellerListItem o1, WholesellerListItem o2) {
                locationB.setLatitude(Double.parseDouble(o1.getLatitude()));
                locationB.setLongitude(Double.parseDouble(o1.getLongitude()));
                locationC.setLatitude(Double.parseDouble(o2.getLatitude()));
                locationC.setLongitude(Double.parseDouble(o2.getLongitude()));
                double dist1 =  locationA.distanceTo(locationB);
                double dist2 =  locationA.distanceTo(locationC);
                return Double.compare(dist1, dist2);
            }
        });

        adapterWholesellerShops = new Customer_AdapterRetailerShops(Customer_showshops.this, wholesellerList);
        productsRv.setAdapter(adapterWholesellerShops);
    }

    private void showShopUi() {
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


}


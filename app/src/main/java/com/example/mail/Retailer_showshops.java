package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mail.Retailer.WholesellerListItem;
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
import java.util.List;

public class Retailer_showshops extends AppCompatActivity {
    private AdapterWholesellerShops adapterWholesellerShops;
    private TextView productstab,orderstab,shopnear;
    private ImageButton logoutbt;
    private ImageView profileIv;

    private RelativeLayout productsRl, ordersRl;
    private RecyclerView productsRv;
    private ArrayList<RetailerProductModel> productList;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_showshops);
        productstab= findViewById(R.id.productstab);

        shopnear = findViewById(R.id.shopnear);
        logoutbt = findViewById(R.id.logoutbt);
        profileIv = findViewById(R.id.profileIv);
        productsRv = findViewById(R.id.productsRv);
        profileIv = findViewById(R.id.profileIv);
        firebaseAuth = FirebaseAuth.getInstance();
        orderstab = findViewById(R.id.orderstab);
        productsRl = findViewById(R.id.productsRl);
        ordersRl = findViewById(R.id.ordersRl);
        Bundle extras =   getIntent().getExtras();
        if(extras!=null)
        {
            List<WholesellerListItem> wholesellerListItems = new ArrayList<>();
           // wholesellerListItems =(List<WholesellerListItem>) extras.get("WholesellerList");
         // wholesellerListItems = extras.
        }
        showShopUi();
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

        logoutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();

            }
        });



    }



    private void loadAllShops() {
        productList =new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wholeseller");
        reference.child(firebaseAuth.getUid()).child("Products").child("wholesellerList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            RetailerProductModel retailerProductModel = ds.getValue(RetailerProductModel.class);
                            productList.add(retailerProductModel);
                        }
                        adapterWholesellerShops = new AdapterWholesellerShops(Retailer_showshops.this, productList);
                        productsRv.setAdapter(adapterWholesellerShops);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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


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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Wholeseller_activity extends AppCompatActivity {
    private AdapterProductWholeseller adapterProductWholeseller;
    private TextView bussnm,profile_name,productstab,orderstab,filteredproductsTv;
    private ImageButton logoutbt, addproduct,filterProductbtn;
    private ImageView profileIv;
    private EditText searchProductEt;
    private RelativeLayout productsRl, ordersRl;
    private RecyclerView productsRv;
    private ArrayList<ModelProduct> productList;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller_activity);
        productstab= findViewById(R.id.productstab);
        bussnm = findViewById(R.id.bussnm);
        filteredproductsTv = findViewById(R.id.filteredproductsTv);
        filterProductbtn = findViewById(R.id.filterProductbtn);
        searchProductEt = findViewById(R.id.searchProductEt);
        logoutbt = findViewById(R.id.logoutbt);
        addproduct = findViewById(R.id.addproduct);
        profile_name = findViewById(R.id.profile_name);
        productsRv = findViewById(R.id.productsRv);
        profileIv = findViewById(R.id.profileIv);
        firebaseAuth = FirebaseAuth.getInstance();
        orderstab = findViewById(R.id.orderstab);
        productsRl = findViewById(R.id.productsRl);
        ordersRl = findViewById(R.id.ordersRl);
        checkuser();
        showProductsUi();
        loadAllProducts();

        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   try{
                       adapterProductWholeseller.getFilter().filter(s);
                   }
                   catch (Exception e){
                     e.printStackTrace();
                   }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        orderstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrdersUi();

            }
        });
        filterProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Wholeseller_activity.this);
                builder.setTitle("Choose Category:")
                        .setItems(Wholeseller_categories.options1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selected = Wholeseller_categories.options1[which];
                                filteredproductsTv.setText(selected);
                                if (selected.equals("All")){
                                    loadAllProducts();
                                }
                                else{
                                    loadFilteredProducts(selected);
                                }


                            }
                        })
                .show();
            }
        });

        productstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductsUi();
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
                startActivity(new Intent(Wholeseller_activity.this,Wholeseller_addproduct.class));

            }
        });

    }

    private void loadFilteredProducts(String selected) {
        productList =new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wholeseller");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String productCategory = "" + ds.child("productcategory").getValue();
                            if(selected.equals(productCategory)){
                                ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                                productList.add(modelProduct);

                            }

                        }
                         adapterProductWholeseller = new AdapterProductWholeseller(Wholeseller_activity.this, productList);
                        productsRv.setAdapter(adapterProductWholeseller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllProducts() {
        productList =new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wholeseller");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productList.add(modelProduct);
                        }
                         adapterProductWholeseller = new AdapterProductWholeseller(Wholeseller_activity.this, productList);
                        productsRv.setAdapter(adapterProductWholeseller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
            startActivity(new Intent(Wholeseller_activity.this, Choice_Role.class));
            finish();
        }
        else
        {
            loadmyinfo();
        }

    }

    private void loadmyinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = "" + ds.child("bussinessname").getValue();
                            String accountype = "" + ds.child("accounttype").getValue();
                            String profilename = ""+ ds.child("name").getValue();
                            String profileimage= "" + ds.child("profileimage").getValue();

                            bussnm.setText(name + "("+accountype+")");
                            profile_name.setText(profilename);
                            try{
                                Picasso.get().load(profileimage).placeholder(R.drawable.ic_baseline_person_24).into(profileIv);
                            }
                            catch (Exception e){
                                profileIv.setImageResource(R.drawable.ic_baseline_person_24);
                            }

                        }
                    }

                    @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Wholeseller_activity.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });
    }
}


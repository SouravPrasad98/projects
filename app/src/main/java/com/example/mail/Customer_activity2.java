package com.example.mail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class Customer_activity2 extends AppCompatActivity {
    private Customeradapter2 customeradapter2;
    private TextView bussnm,profile_name,productstab,filteredproductsTv;
    private ImageButton logoutbt, addproduct,filterProductbtn;
    private ImageView profileIv, nextIv;
    private EditText searchProductEt;
    private RelativeLayout productsRl;
    private RecyclerView productsRv;
    private ArrayList<RetailerProductModel> productList;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_activity2);
        productstab = findViewById(R.id.productstab);
        nextIv = findViewById(R.id.nextIv);
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
        productsRl = findViewById(R.id.productsRl);

        checkuser();
        showProductsUi();
//        loadAllProducts();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String category = extras.getString("Category");
            loadFilteredProducts(category);
        }
     searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

           @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* try{
                    retaileradapter2.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        filterProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Customer_activity2.this);
                builder.setTitle("Choose Category:")
                        .setItems(Wholeseller_categories.options1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selected = Wholeseller_categories.options1[which];
                                filteredproductsTv.setText(selected);
                                if (selected.equals("All")){
//                                    loadAllProducts();
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



    }

    private void loadFilteredProducts(String selected) {
        productList =new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RetailerProducts");
        reference.orderByChild("productcategory").equalTo(selected)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            RetailerProductModel modelProduct = ds.getValue(RetailerProductModel.class);
                            productList.add(modelProduct);

                        }
                        customeradapter2 = new Customeradapter2(Customer_activity2.this, productList);
                        productsRv.setAdapter(customeradapter2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

//    private void loadAllProducts() {
//        productList =new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Retailer");
//        reference.child(firebaseAuth.getUid()).child("RetailerProducts")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        productList.clear();
//                        for(DataSnapshot ds: snapshot.getChildren()){
//                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
//                            productList.add(modelProduct);
//                        }
//                        adapterProductWholeseller = new AdapterProductWholeseller(Retailer_activity.this, productList);
//                        productsRv.setAdapter(adapterProductWholeseller);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//    }

    private void showProductsUi() {
        productsRl.setVisibility(View.VISIBLE);
        productstab.setTextColor(getResources().getColor(R.color.black));
        productstab.setBackgroundResource(R.drawable.shaperec01);

    }

    private void checkuser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user==null){
            startActivity(new Intent(Customer_activity2.this, Choice_Role.class));
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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

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
                        Toast.makeText(Customer_activity2.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });
    }
}


package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mail.common.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Wholeseller_main_activity1 extends AppCompatActivity {

    private TextView bussnm,profile_name,productstab,orderstab,filteredOrdersTv;
    private ImageButton logoutbt, addproduct,settingsBtn,filterOrderBtn;
    private ImageView profileIv;
    private long backpressedTime;
    private EditText searchOrderEt;
    private Button showproducts;
    private RelativeLayout productsRl, ordersRl;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private RecyclerView orderRv;
    private AdapterOrderWholeseller adapterOrderWholeseller;
    private ArrayList<ModelOrderWholeseller> modelOrderWholesellerArrayList;

    private TextView others,Biscuits,beverages,breakfastdairy,eggmeat,frozenfood,
            fruitsandveg,foodgrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller_main_activity1);
        filterOrderBtn = findViewById(R.id.filterOrderBtn);
        filteredOrdersTv = findViewById(R.id.filteredOrdersTv);
        searchOrderEt = findViewById(R.id.searchOrderEt);
        productstab= findViewById(R.id.productstab);
        showproducts = findViewById(R.id.showproducts);
        bussnm = findViewById(R.id.bussnm);
        settingsBtn = findViewById(R.id.settingsBtn);
        orderRv = findViewById(R.id.orderRv);
        logoutbt = findViewById(R.id.logoutbt);
        productsRl = findViewById(R.id.productsRl);
        ordersRl = findViewById(R.id.ordersRl);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
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
        loadAllOrders();
        showOrdersUi();
        showProductsUi();


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
                Intent intent = new Intent(Wholeseller_main_activity1.this,Showall_products.class);
                startActivity(intent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wholeseller_main_activity1.this, SettingsActivity.class));
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wholeseller_main_activity1.this,Wholeseller_activity.class);
                intent.putExtra("Category", "Others");
                startActivity(intent);
            }
        });

        logoutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makemeOffline();

            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wholeseller_main_activity1.this,Wholeseller_addproduct.class));

            }
        });

        Biscuits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wholeseller_main_activity1.this,Wholeseller_activity.class);
                intent.putExtra("Category", "Biscuits Snacks and Chocolates");
                startActivity(intent);
            }
        });

        foodgrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wholeseller_main_activity1.this,Wholeseller_activity.class);
                intent.putExtra("Category", "Foodgrains, Oil and Masala");
                startActivity(intent);
            }
        });

        frozenfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wholeseller_main_activity1.this,Wholeseller_activity.class);
                intent.putExtra("Category", "Frozen Food");
                startActivity(intent);
            }
        });

        fruitsandveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wholeseller_main_activity1.this,Wholeseller_activity.class);
                intent.putExtra("Category", "Fuits and Vegetables");
                startActivity(intent);
            }
        });


        eggmeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wholeseller_main_activity1.this,Wholeseller_activity.class);
                intent.putExtra("Category", "Eggs,Meat,Fish");
                startActivity(intent);
            }
        });

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wholeseller_main_activity1.this,Wholeseller_activity.class);
                intent.putExtra("Category", "Beverages");
                startActivity(intent);
            }
        });


        breakfastdairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wholeseller_main_activity1.this,Wholeseller_activity.class);
                intent.putExtra("Category", "Breakfast and Dairy");
                startActivity(intent);
            }
        });
filterOrderBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String[] options = {"All","In Progress","Completed","Cancelled"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Wholeseller_main_activity1.this);
        builder.setTitle("Filter Orders:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            filteredOrdersTv.setText("Showing All");
                            adapterOrderWholeseller.getFilter().filter("");
                        }
                        else{
                            String optionClicked = options[which];
                            filteredOrdersTv.setText("Showing" + optionClicked+ "orders");
                            adapterOrderWholeseller.getFilter().filter(optionClicked);
                        }
                    }
                });
    }
});

    }

    private void loadAllOrders() {
        modelOrderWholesellerArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders");
        ref.orderByChild("orderTo").equalTo(Constants.wuid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelOrderWholesellerArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelOrderWholeseller modelOrderWholeseller = ds.getValue(ModelOrderWholeseller.class);
                            modelOrderWholesellerArrayList.add(modelOrderWholeseller);
                        }
                        adapterOrderWholeseller = new AdapterOrderWholeseller(Wholeseller_main_activity1.this, modelOrderWholesellerArrayList);
                        orderRv.setAdapter(adapterOrderWholeseller);
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

    private void makemeOffline() {
        progressDialog.setMessage("Logging Out...");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseAuth.signOut();
                        checkuser();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Wholeseller_main_activity1.this, "error", Toast.LENGTH_LONG).show();

                    }
                });
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
            startActivity(new Intent(Wholeseller_main_activity1.this, Choice_Role.class));
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

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Wholeseller_main_activity1.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });
    }

    @Override
    public void onBackPressed() {


        if(backpressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            Intent intent= new Intent(Wholeseller_main_activity1.this, Wholeseller_login.class);
            startActivity(intent);
            return;
        }
        else{
            Toast.makeText(getBaseContext(), "Press Back again to exit", Toast.LENGTH_SHORT).show();
        }
        backpressedTime= System.currentTimeMillis();

    }
}



package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class ShopdetailsActivity extends AppCompatActivity {

    private TextView bussnm,phonenum,emaill,address,filteredproductsTv;
    private ImageButton mapBt, callBt,filterProductbtn,addproduct;
    private ImageView profileIv;
    private EditText searchProductEt;
    private RelativeLayout productsRl, ordersRl;
    private RecyclerView productsRv;
    private ArrayList<ModelProduct> productList;
    private FirebaseAuth firebaseAuth;
private String uid;
private String myLatitude,myLongitude;
private String shopLatitude,shopLongitude,shopName,shopPhone,shopAddress,shopEmail;
private AdapterProductShopDetails adapterProductShopDetails;
private ArrayList<ModelCartItem> cartItemList;
private AdaptercartItem adaptercartItem;
public String deliveryFee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdetails);
        phonenum= findViewById(R.id.phonenum);
        addproduct = findViewById(R.id.addproduct);
        bussnm = findViewById(R.id.bussnm);
        filteredproductsTv = findViewById(R.id.filteredproductsTv);
        filterProductbtn = findViewById(R.id.filterProductbtn);
        searchProductEt = findViewById(R.id.searchProductEt);
        emaill = findViewById(R.id.emaill);
        address = findViewById(R.id.address);
        mapBt = findViewById(R.id.mapBt);
        productsRv = findViewById(R.id.productsRv);
        profileIv = findViewById(R.id.profileIv);
        firebaseAuth = FirebaseAuth.getInstance();
        callBt = findViewById(R.id.callBt);
        productsRl = findViewById(R.id.productsRl);
        ordersRl = findViewById(R.id.ordersRl);
        uid = getIntent().getStringExtra("uid");
        firebaseAuth = FirebaseAuth.getInstance();

        loadmyinfo();
        loadShopdetails();
        loadAllProducts();

        deleteCartData();


        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterProductShopDetails.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        callBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialphone();
            }
        });

        mapBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmap();
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCartDialog();

            }
        });


        filterProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopdetailsActivity.this);
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



    }

    private void deleteCartData() {

        EasyDB easyDB =EasyDB.init(this, "ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                .addColumn(new Column("Item_PId", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
                .doneTableColumn();
        easyDB.deleteAllDataFromTable();

    }

    public double allTotalPrice  = 0.00;
    public TextView sTotalTv,dFeeTv,allTotalPriceTv;
    private void showCartDialog() {
        cartItemList = new ArrayList<>();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_cart, null);
        TextView shopNameTv = view.findViewById(R.id.shopNameTV);
        RecyclerView cartItemsRv = view.findViewById(R.id.cartItemsRv);
        TextView sTotalTv = view.findViewById(R.id.sTotalTv);
        TextView dFeeTv = view.findViewById(R.id.dFeeTv);
        TextView allTotalPriceTv = view.findViewById(R.id.totalTv);
        Button checkoutBt = view.findViewById(R.id.checkoutBt);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        shopNameTv.setText(shopName);

        EasyDB easyDB =EasyDB.init(this, "ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                .addColumn(new Column("Item_PId", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
                .doneTableColumn();

        Cursor res = easyDB.getAllData();
        while(res.moveToNext()){
            String id = res.getString(1);
            String pId = res.getString(2);
            String name = res.getString(3);
            String price = res.getString(4);
            String cost = res.getString(5);
            String quantity = res.getString(6);

            allTotalPrice = allTotalPrice + Double.parseDouble(cost);

            ModelCartItem modelCartItem = new ModelCartItem(""+id,
                    ""+ pId,
                    ""+name,
                    ""+price,
                    ""+cost,
                    ""+ quantity
            );

            cartItemList.add(modelCartItem);

        }

        adaptercartItem = new AdaptercartItem(this, cartItemList);
        cartItemsRv.setAdapter(adaptercartItem);
        dFeeTv.setText("$"+ deliveryFee);
        sTotalTv.setText("$"+String.format("%.2f", allTotalPrice));
        allTotalPriceTv.setText("$"+(allTotalPrice + Double.parseDouble(deliveryFee.replace("$", ""))));

        AlertDialog dialog =builder.create();
        dialog.show();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                allTotalPrice = 0.00;
            }
        });

    }
    private void openmap() {
        String address = "http://maps.google.com/maps?saddr=" + myLatitude +","+myLongitude +"&daddr=" +shopLatitude + "," + shopLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(address));
        startActivity(intent);


    }

    private void dialphone() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(shopPhone))));
        Toast.makeText(this, ""+shopPhone, Toast.LENGTH_SHORT).show();
    }

    private void loadShopdetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        shopName = "" + snapshot.child("bussinessname").getValue();
                        shopPhone= ""+ snapshot.child("phonenumber").getValue();
                        shopEmail ="" + snapshot.child("email").getValue();
                        shopAddress=""+snapshot.child("address").getValue();
                        shopLatitude=""+snapshot.child("latitude").getValue();
                        shopLongitude = "" +snapshot.child("longitude").getValue();
                        deliveryFee = "" + snapshot.child("deliveryfree").getValue();
                        String profileimage = "" + snapshot.child("profileimage").getValue();

                        bussnm.setText(shopName);
                        phonenum.setText(shopPhone);
                        emaill.setText(shopEmail);
                        address.setText(shopAddress);
                        try {
                            Picasso.get().load(profileimage).into(profileIv);
                        }
                        catch (Exception e){

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadFilteredProducts(String selected) {
        productList =new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wholeseller");
        reference.child(uid).child("Products")
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
                        adapterProductShopDetails = new AdapterProductShopDetails(ShopdetailsActivity.this, productList);
                        productsRv.setAdapter(adapterProductShopDetails);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadAllProducts() {
        productList =new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wholeseller");
        reference.child(uid).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productList.add(modelProduct);
                        }
                        adapterProductShopDetails = new AdapterProductShopDetails(ShopdetailsActivity.this, productList);
                        productsRv.setAdapter(adapterProductShopDetails);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


//    private void checkuser(){
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        if(user==null){
//            startActivity(new Intent(ShopdetailsActivity.this, Choice_Role.class));
//            finish();
//        }
//        else
//        {
//            loadmyinfo();
//        }
//
//    }

    private void loadmyinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Retailer");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            //Constants.wname= "" + ds.child("name").getValue();
                            Constants.waddress = "" + ds.child("address").getValue();
                            Constants.wemail = "" + ds.child("email").getValue();
                            Constants.wphonenumber = "" + ds.child("phonenumber").getValue();
                            //Constants.wcountry= "" + ds.child("country").getValue();
                            Constants.wcity = "" + ds.child("city").getValue();
                            myLatitude = "" + ds.child("latitude").getValue();
                            myLongitude = "" + ds.child("longitude").getValue();
                            //Constants.wdeliveryfee= "" + ds.child("deliveryfee").getValue();
                            Constants.wbussinessname = "" + ds.child("bussinessname").getValue();
                            //Constants.wstate= "" + ds.child("state").getValue();
                            Constants.wuid = "" + ds.child("uid").getValue();
                            Constants.wprofileimage = "" + ds.child("profileimage").getValue();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShopdetailsActivity.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });


    }
}

package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mail.common.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderdetailsRetailerActivity extends AppCompatActivity {
    private String orderTo, orderId,orderBy, latitude, longitude, deliveryfee,address;
    private ImageButton backbutt,writeReviewBtn;
    private TextView orderIdTv, dateTv, orderStatusTv, shopNameTv, amountTv, totalItemsTv, addressTv;
    private RecyclerView itemsRv;
    private FirebaseAuth firebaseAuth;
 private ArrayList<ModelOrderedItem> orderedItemArrayList;
 private AdapterOrderedItem adapterOrderedItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_retailer);

        writeReviewBtn = findViewById(R.id.writeReviewBtn);
        backbutt = findViewById(R.id.backBtn);
        dateTv = findViewById(R.id.dateTv);
        orderStatusTv = findViewById(R.id.orderStatusTv);
        shopNameTv = findViewById(R.id.shopNameTv);
        orderIdTv = findViewById(R.id.orderIdTv);
        amountTv = findViewById(R.id.amountTv);
        totalItemsTv = findViewById(R.id.totalItemsTv);
        addressTv = findViewById(R.id.addressTv);
        itemsRv = findViewById(R.id.itemsRv);


        Intent intent = getIntent();
        orderTo = intent.getStringExtra("orderTo");
        orderId = intent.getStringExtra("orderId");
        orderBy = intent.getStringExtra("orderBy");

        firebaseAuth = FirebaseAuth.getInstance();
        loadShopInfo();
        loadOrderDetails();
        loadOrderedItems();

        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderdetailsRetailerActivity.this, WriteReviewActivity.class);
                intent1.putExtra("uid", orderTo);
                startActivity(intent1);
                finish();
            }
        });

//        backbutt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }



    private void loadOrderedItems() {
        orderedItemArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders");
        ref.child(orderId).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       orderedItemArrayList.clear();
                       for (DataSnapshot ds: snapshot.getChildren()){
                           ModelOrderedItem modelOrderedItem = ds.getValue(ModelOrderedItem.class);

                           orderedItemArrayList.add(modelOrderedItem);
                       }
                       adapterOrderedItem = new AdapterOrderedItem(OrderdetailsRetailerActivity.this, orderedItemArrayList);
                       itemsRv.setAdapter(adapterOrderedItem);

                       totalItemsTv.setText(""+snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



    private void loadShopInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.child(orderTo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String shopName = "" + snapshot.child("bussinessname").getValue();
                        shopNameTv.setText(shopName);
                        deliveryfee = ""+ snapshot.child("deliveryfee").getValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadOrderDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders");
        ref.child(orderId)
    .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String orderBy1 = "" + snapshot.child("orderBy").getValue();
                String orderTo = "" + snapshot.child("orderTo").getValue();
                String orderTime = "" + snapshot.child("orderTime").getValue();
                String orderstatus = "" + snapshot.child("orderStatus").getValue();
                String orderCost = "" + snapshot.child("orderCost").getValue();
                String orderId = "" + snapshot.child("orderId").getValue();


//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Retailer");
//                reference.child(orderBy)
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                address = ""+ snapshot.child("address").getValue();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(orderTime));
                String formatedDate = DateFormat.format("dd/MM/yyyy hh:mm a", calendar).toString();

                if (orderstatus.equals("In Progress")) {
                    orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else if (orderstatus.equals("Completed")) {
                    orderStatusTv.setTextColor(getResources().getColor(R.color.middlegreen));
                } else if (orderstatus.equals("Cancelled")) {
                    orderStatusTv.setTextColor(getResources().getColor(R.color.tomatored));
                }

                orderIdTv.setText(orderId);
                addressTv.setText(Constants.waddress);
                orderStatusTv.setText(orderstatus);
                amountTv.setText("$" + orderCost + "[Including delivery fee $" + deliveryfee+"]");
                dateTv.setText(formatedDate);

//                findAddress(latitude, longitude);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    private void findAddress(String latitude, String longitude) {
//        double lat = Double.parseDouble(latitude);
//        double lon = Double.parseDouble(longitude);
//
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            addresses = geocoder.getFromLocation(lat, lon, 1);
//            String address = addresses.get(0).getAddressLine(0);
//            addressTv.setText(address);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
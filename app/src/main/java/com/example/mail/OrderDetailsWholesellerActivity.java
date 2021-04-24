package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mail.common.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrderDetailsWholesellerActivity extends AppCompatActivity {
    String orderId, orderTo, orderBy;
    private ImageButton writeReviewBtn, editBtn, mapBtn;
    private TextView nameTv, phoneTv, totalItemsTv, amountTv, addressTv, orderIdTv, dateTv, orderStatusTv;
    private FirebaseAuth firebaseAuth;
    String slatitude, slongitude, dlatitude, dlongitude;
    private RecyclerView itemsRv;
    private ArrayList<ModelOrderedItem> orderedItemArrayList;
    private AdapterOrderedItem adapterOrderedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_wholeseller);

        orderBy = getIntent().getStringExtra("orderBy");
        orderId = getIntent().getStringExtra("orderId");
        orderTo = getIntent().getStringExtra("orderTo");

        writeReviewBtn = findViewById(R.id.writeReviewBtn);
        editBtn = findViewById(R.id.editBtn);
        itemsRv = findViewById(R.id.itemsRv);
        mapBtn = findViewById(R.id.mapBtn);
        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        totalItemsTv = findViewById(R.id.totalItemsTv);
        amountTv = findViewById(R.id.amountTv);
        addressTv = findViewById(R.id.addressTv);
        orderIdTv = findViewById(R.id.orderIdTv);
        dateTv = findViewById(R.id.dateTv);
        orderStatusTv = findViewById(R.id.orderStatusTv);

        firebaseAuth = FirebaseAuth.getInstance();
        loadmyInfo();
        loadBuyerInfo();
        loadOrderDetails();
        loadOrderedItems();

       editBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               editOrderStatusDialog();
           }
       });

        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmap();
            }
        });

    }

    private void editOrderStatusDialog() {
        String[] options = {"All","In Progress","Completed","Cancelled"};
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsWholesellerActivity.this);
        builder.setTitle("Edit Order Status")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String selectedOption = options[i];
                        editOrderStatusDialog(selectedOption);
                    }
                })
                .show();
    }

    private void editOrderStatusDialog(String selectedOption) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus", ""+ selectedOption);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders");
        reference.child(orderId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OrderDetailsWholesellerActivity.this, "Order is now"+ selectedOption, Toast.LENGTH_SHORT).show();

                        if (selectedOption.equals("In Progress")) {
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        } else if (selectedOption.equals("Completed")) {
                            orderStatusTv.setTextColor(getResources().getColor(R.color.middlegreen));
                        } else if (selectedOption.equals("Cancelled")) {
                            orderStatusTv.setTextColor(getResources().getColor(R.color.tomatored));
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailsWholesellerActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });

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
                        adapterOrderedItem = new AdapterOrderedItem(OrderDetailsWholesellerActivity.this, orderedItemArrayList);
                        itemsRv.setAdapter(adapterOrderedItem);

                        totalItemsTv.setText(""+snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadOrderDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RetailerOnlineOrders");
        reference.child(orderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String orderBy1 = "" + snapshot.child("orderBy").getValue();
                        String orderTo = "" + snapshot.child("orderTo").getValue();
                        String orderTime = "" + snapshot.child("orderTime").getValue();
                        String orderstatus = "" + snapshot.child("orderStatus").getValue();
                        String orderCost = "" + snapshot.child("orderCost").getValue();
                        String orderId = "" + snapshot.child("orderId").getValue();

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
                        amountTv.setText("$" + orderCost);
                        dateTv.setText(formatedDate);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadmyInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Wholeseller");
        reference.child(orderTo)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        slatitude = "" + snapshot.child("latitude").getValue();
                        slongitude = "" + snapshot.child("longitude").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void openmap() {
        String address = "http://maps.google.com/maps?saddr=" + slatitude +","+slongitude +"&daddr=" + dlatitude + "," + dlongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        startActivity(intent);

    }

    private void loadBuyerInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Retailer");
        reference.child(orderBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dlatitude = "" + snapshot.child("latitude").getValue();
                        dlongitude = "" + snapshot.child("longitude").getValue();
                        String name = "" + snapshot.child("name").getValue();
                        String phone = "" + snapshot.child("phonenumber").getValue();

                        phoneTv.setText(phone);
                        nameTv.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
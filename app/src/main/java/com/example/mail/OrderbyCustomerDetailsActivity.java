package com.example.mail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mail.common.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrderbyCustomerDetailsActivity extends AppCompatActivity {
    String orderId, orderTo, orderBy;
    private ImageButton writeReviewBtn, editBtn, mapBtn;
    private TextView nameTv, phoneTv, totalItemsTv, amountTv, addressTv, orderIdTv, dateTv, orderStatusTv;
    private FirebaseAuth firebaseAuth;
    String slatitude, slongitude, dlatitude, dlongitude;
    private RecyclerView itemsRv;
    private ArrayList<ModelOrderedItem> orderedItemArrayList;
    private AdapterbyCustomerOrderedItem adapterbyCustomerOrderedItem;

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
                Intent intent = new Intent(OrderbyCustomerDetailsActivity.this, RetailerShopReviewsActivity.class);
                intent.putExtra("uid", "" + firebaseAuth.getUid());
                startActivity(intent);

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
        String[] options = {"All", "In Progress", "Completed", "Cancelled"};
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderbyCustomerDetailsActivity.this);
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
        hashMap.put("orderStatus", "" + selectedOption);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerOnlineOrders");
        reference.child(orderId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        String message = "Order is now" + selectedOption;
                        Toast.makeText(OrderbyCustomerDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                        String phoneNumber = phoneTv.getText().toString();

                        String mssg = "";
                        if (selectedOption.equals("In Progress")) {
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                            mssg = new String("Dear Customer " + nameTv.getText().toString() + ", Your order status for order id :  " + orderId +
                                    "has been updated to \'In Progress\'");
                        } else if (selectedOption.equals("Completed")) {
                            orderStatusTv.setTextColor(getResources().getColor(R.color.middlegreen));
                            mssg = new String("Dear Customer " + nameTv.getText().toString() + ",  Your order status for order id :  " + orderId +
                                    "has been updated to \'Completed\'");
                        } else if (selectedOption.equals("Cancelled")) {
                            orderStatusTv.setTextColor(getResources().getColor(R.color.tomatored));
                            mssg = new String("Dear Customer " + nameTv.getText().toString() + ",  Your order status for order id :  " + orderId +
                                    "has been updated to \'Cancelled\'");
                        }
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, mssg, null, null);
//                        prepareNotificationMessage(orderId,message);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderbyCustomerDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void loadOrderedItems() {
        orderedItemArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CustomerOnlineOrders");
        ref.child(orderId).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderedItemArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelOrderedItem modelOrderedItem = ds.getValue(ModelOrderedItem.class);

                            orderedItemArrayList.add(modelOrderedItem);
                        }
                        adapterbyCustomerOrderedItem = new AdapterbyCustomerOrderedItem(OrderbyCustomerDetailsActivity.this, orderedItemArrayList);
                        itemsRv.setAdapter(adapterbyCustomerOrderedItem);

                        totalItemsTv.setText(""+snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadOrderDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CustomerOnlineOrders");
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Retailer");
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Customer");
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
    private void prepareNotificationMessage(String  orderId ,String message)
    {
        String NOTIFICATION_TOPIC = "/topics" + Constants.FCM_TOPIC;
        String NOTIFICATION_TITLE =  "Your Order" + orderId;
        String NOTIFICATION_MESSAGE = "" + message;
        String NOTIFICATION_TYPE = "OrderStatusChanged";

        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();

        try {
            notificationBodyJo.put("notificationType",NOTIFICATION_TYPE);
            notificationBodyJo.put("buyerUid",orderBy);
            notificationBodyJo.put("sellerlUid",firebaseAuth.getUid());//shopUid
            notificationBodyJo.put("orderId",orderId);
            notificationBodyJo.put("notificationTitle",NOTIFICATION_TITLE);
            notificationBodyJo.put("notificationMessage",NOTIFICATION_MESSAGE);

            notificationJo.put("to",NOTIFICATION_TOPIC);
            notificationJo.put("data",notificationBodyJo);
        }
        catch (Exception e)
        {
            Toast.makeText(this,""+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        sendFcmNotification(notificationJo);
    }

    private void sendFcmNotification(JSONObject notificationJo) {

        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {




            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","key=" + Constants.FCM_KEY);



                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

}
package com.example.mail.Retailer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mail.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class retailerItemsIHave extends AppCompatActivity {
    RecyclerView rec;
    RetailerItemsFirebaseAdapter adapter;
    private FirebaseUser user;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_items_i_have);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();

        rec= (RecyclerView)findViewById(R.id.retailerItemsRecyclerview);
        rec.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RetailerAddItemHelper> options =
                new FirebaseRecyclerOptions.Builder<RetailerAddItemHelper>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Retailer/"+userID), RetailerAddItemHelper.class)
                        .build();
        adapter = new RetailerItemsFirebaseAdapter(options);
        rec.setAdapter(adapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
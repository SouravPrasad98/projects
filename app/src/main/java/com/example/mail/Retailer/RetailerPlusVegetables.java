package com.example.mail.Retailer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.example.mail.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RetailerPlusVegetables extends AppCompatActivity {
    private ElegantNumberButton onion, potato , tomato, carrot;
    private int onionq=0,potatoq=0,tomatoq=0,carrotq=0;
    private EditText onionp,potatop,tomatop,carrotp;
    private SwipeButton vegButton;
    private TextView back;
    private FirebaseUser user;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_plus_vegetables);
        onion=(ElegantNumberButton)findViewById(R.id.onionRetailerVegetable);
        potato=(ElegantNumberButton)findViewById(R.id.potatoRetailerVegetables);
        tomato=(ElegantNumberButton)findViewById(R.id.tomatoRetailerVegetables);
        carrot=(ElegantNumberButton)findViewById(R.id.carrotRetailerVegetables);
        vegButton=(SwipeButton) findViewById(R.id.buttonRetailerVegetables);

        onionp=(EditText)findViewById(R.id.onionRetailerVegPrice);
        potatop=(EditText)findViewById(R.id.potatoRetailerVegPrice);
        tomatop=(EditText)findViewById(R.id.tomatoRetailerVegPrice);
        carrotp=(EditText)findViewById(R.id.carrotRetailerVegPrice);
        back=(TextView)findViewById(R.id.retailerVegetablesBack);

        user= FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        onion.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                onionq= Integer.parseInt(onion.getNumber());
            }
        });
        potato.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                potatoq= Integer.parseInt(potato.getNumber());
            }
        });
        tomato.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                tomatoq= Integer.parseInt(tomato.getNumber());
            }
        });
        carrot.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                carrotq= Integer.parseInt(carrot.getNumber());
            }
        });
        vegButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("Retailer/"+userID);

                int op= Integer.parseInt(onionp.getText().toString().trim());
                int pp= Integer.parseInt(potatop.getText().toString().trim());
                int tp= Integer.parseInt(tomatop.getText().toString().trim());
                int cp= Integer.parseInt(carrotp.getText().toString().trim());

                RetailerAddItemHelper ob=new RetailerAddItemHelper(op,onionq,1,"Onion");
                reference.child("Onion").setValue(ob);
                RetailerAddItemHelper ob2=new RetailerAddItemHelper(pp,potatoq,1,"Potato");
                reference.child("Potato").setValue(ob2);
                RetailerAddItemHelper ob3=new RetailerAddItemHelper(tp,tomatoq,1,"Tomato");
                reference.child("Tomato").setValue(ob3);
                RetailerAddItemHelper ob4=new RetailerAddItemHelper(cp,carrotq,1,"Carrot");
                reference.child("Carrot").setValue(ob4);

                Toast.makeText(getBaseContext(),"Successfully Added",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
package com.example.mail.Retailer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mail.MainActivity;
import com.example.mail.R;
import com.example.mail.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RetailerBottomAccount extends Fragment {


    public RetailerBottomAccount() {
        // Required empty public constructor
    }
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
   private Button itemsIHave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_retailer_bottom_account, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("User");
        userID=user.getUid();
        final TextView Name=(TextView)view.findViewById(R.id.retailerProfileName);
        final TextView Email=(TextView)view.findViewById(R.id.retailerProfileEmail);
        final TextView Number=(TextView)view.findViewById(R.id.retailerProfileNumber);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profile=snapshot.getValue(User.class);
                if(profile!=null)
                {
                    String name=profile.username;
                    String email=profile.email;
                    String number=profile.phone;

                    Name.setText("Hey! "+name);
                    Email.setText(email);
                    Number.setText(number);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });

        Button logout=(Button)view.findViewById(R.id.retailerLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        itemsIHave=(Button)view.findViewById(R.id.retailerAccountItems);
        itemsIHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), retailerItemsIHave.class);
                startActivity(intent);

            }
        });

        return view;
    }
}
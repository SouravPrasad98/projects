package com.example.mail.Retailer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.mail.R;

public class RetailerBottomPlus extends Fragment {



    public RetailerBottomPlus() {
        // Required empty public constructor
    }

private RelativeLayout retailerPulse, retailerVegetables;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_retailer_bottom_plus, container, false);
        retailerPulse=(RelativeLayout)view.findViewById(R.id.retailerPulses);
        retailerVegetables=(RelativeLayout)view.findViewById(R.id.retailerVegetables);

        retailerPulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),RetailerPlusPulses.class);
                startActivity(intent);
            }
        });

        retailerVegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),RetailerPlusVegetables.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
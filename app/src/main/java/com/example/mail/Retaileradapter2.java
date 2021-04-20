package com.example.mail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Retaileradapter2 extends RecyclerView.Adapter<Retaileradapter2.HolderRetailer>implements Filterable {
    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public Retaileradapter2.HolderRetailer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Retaileradapter2.HolderRetailer holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HolderRetailer extends RecyclerView.ViewHolder{


        private ImageView productIconIv;
        private TextView productname;



        public HolderRetailer(@NonNull View itemView) {
            super(itemView);
            productIconIv = itemView.findViewById(R.id.productIconIv);
            productname = itemView.findViewById(R.id.productname);

        }
    }
}

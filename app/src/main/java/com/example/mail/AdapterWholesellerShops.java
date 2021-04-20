package com.example.mail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class AdapterWholesellerShops extends RecyclerView.Adapter<AdapterWholesellerShops.HolderWholesellerShops> {
    private Context context;
    public ArrayList<RetailerProductModel> productList, filterList;
    public AdapterWholesellerShops(Context context, ArrayList<RetailerProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

        @NonNull
    @Override
    public AdapterWholesellerShops.HolderWholesellerShops onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_wholeseller_shops,parent,false);
            return new AdapterWholesellerShops.HolderWholesellerShops(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWholesellerShops.HolderWholesellerShops holder, int position) {
        RetailerProductModel retailerProductModel = productList.get(position);


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class HolderWholesellerShops extends RecyclerView.ViewHolder {
        private ImageView profileIv;
        private TextView closeIv,shopaddress,shopname,discountedpriceEt,productprice,productquantity;
        private RatingBar ratingbar;

        public HolderWholesellerShops(@NonNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileIv);
            closeIv = itemView.findViewById(R.id.closeIv);
            shopname = itemView.findViewById(R.id.shopname);
            shopaddress = itemView.findViewById(R.id.shopaddress);
            ratingbar = itemView.findViewById(R.id.ratingbar);
            productquantity = itemView.findViewById(R.id.productquantity);
            productprice = itemView.findViewById(R.id.productprice);
            discountedpriceEt = itemView.findViewById(R.id.discountedpriceEt);



        }
    }
}
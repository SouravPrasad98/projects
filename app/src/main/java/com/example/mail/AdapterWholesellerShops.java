package com.example.mail;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mail.Retailer.WholesellerListItem;
import com.example.mail.common.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class AdapterWholesellerShops extends RecyclerView.Adapter<AdapterWholesellerShops.HolderWholesellerShops> {
    private Context context;
    public ArrayList<WholesellerListItem> productList, filterList;
    public AdapterWholesellerShops(Context context, ArrayList<WholesellerListItem> productList) {
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
        WholesellerListItem retailerProductModel = productList.get(position);
        String addresss = retailerProductModel.getAddress();
        String priceproduct = retailerProductModel.getPrice();
        String bussname = retailerProductModel.getBussinessname();
        String quant = retailerProductModel.getQuantity();
        String disprice = retailerProductModel.getDiscountprice();
        String image = retailerProductModel.getIcon();
        String disnote = retailerProductModel.getDiscountnote();
        String shoopstat = retailerProductModel.getShopopen();
        String on =retailerProductModel.getOnline();



        holder.shopname.setText(bussname);
        if(Integer.parseInt(quant) > 0) {
            holder.productquantity.setText(quant);
        }
        else
        {
            holder.productquantity.setText("Not in stock!");
        }
        holder.productprice.setText(priceproduct);
        holder.shopaddress.setText(addresss);
        holder.discountedpriceEt.setText(disprice);
        if(shoopstat.equals("true")){
            holder.closeIv.setVisibility(View.GONE);
        }
        else {
            holder.closeIv.setVisibility(View.VISIBLE);
        }

        try{
            Picasso.get().load(image).placeholder(R.drawable.ic_baseline_store_24).into(holder.profileIv);
        }
        catch (Exception e){
            holder.profileIv.setImageResource(R.drawable.ic_baseline_store_24);
        }

        Location locationA = new Location("");
        Location locationB = new Location("");
        locationA.setLatitude(Double.parseDouble(Constants.wlatitude));
        locationA.setLongitude(Double.parseDouble(Constants.wlongitude));
        locationB.setLatitude(Double.parseDouble(retailerProductModel.getLatitude()));
        locationB.setLongitude(Double.parseDouble(retailerProductModel.getLongitude()));

        holder.distance.setText("Distance : " + String.format("%.2f", locationA.distanceTo(locationB)/1000) + " km");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class HolderWholesellerShops extends RecyclerView.ViewHolder {
        private ImageView profileIv;
        private TextView closeIv,shopaddress,shopname,productprice,productquantity,discountedpriceEt, distance;
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
            distance = itemView.findViewById(R.id.distance);


        }
    }
}
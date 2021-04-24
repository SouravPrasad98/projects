package com.example.mail;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Customer_AdapterRetailerShops extends RecyclerView.Adapter<Customer_AdapterRetailerShops.HolderWholesellerShops> {
    private Context context;
    public ArrayList<WholesellerListItem> productList, filterList;
    public Customer_AdapterRetailerShops(Context context, ArrayList<WholesellerListItem> productList) {
        this.context = context;
        this.productList = productList;
    }

        @NonNull
    @Override
    public Customer_AdapterRetailerShops.HolderWholesellerShops onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_wholeseller_shops,parent,false);
            return new Customer_AdapterRetailerShops.HolderWholesellerShops(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Customer_AdapterRetailerShops.HolderWholesellerShops holder, int position) {
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
        String id = retailerProductModel.getUid();
        String percost = retailerProductModel.getPerunitCost();

        loadReviews(retailerProductModel, holder);

        holder.perunitcost.setText("Rs. " + priceproduct);
        holder.shopname.setText(bussname);
        if(Integer.parseInt(quant) > 0) {
            holder.productquantity.setText("Quantity : " + quant);
        }
        else
        {
            holder.productquantity.setText("Not in stock! Available in 4 days");
        }
        holder.productprice.setText(priceproduct);
        holder.shopaddress.setText(addresss);
        holder.discountedpriceEt.setText(disprice);
//        if(shoopstat.equals("true")){
//            holder.closeIv.setVisibility(View.GONE);
//        }
//        else {
//            holder.closeIv.setVisibility(View.VISIBLE);
//        }

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Customer_RetailerShopdetailsActivity.class);
                intent.putExtra("uid", id);
                context.startActivity(intent);
            }
        });



    }

    private float ratingSum=0;
    private void loadReviews(WholesellerListItem retailerProductModel, HolderWholesellerShops holder) {

        String shopid = retailerProductModel.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Retailer");
        ref.child(shopid).child("Ratings")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        ratingSum = 0;
                        for (DataSnapshot ds: snapshot.getChildren()){
                            float rating = Float.parseFloat(""+ ds.child("ratings").getValue());
                            ratingSum = ratingSum+rating;

                        }

                        long numberOfReviews = snapshot.getChildrenCount();
                        float avgRating = ratingSum/numberOfReviews;

                        holder.ratingbar.setRating(avgRating);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }




    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class HolderWholesellerShops extends RecyclerView.ViewHolder {
        private ImageView profileIv;
        private TextView closeIv,shopaddress,shopname,productprice,productquantity,discountedpriceEt,perunitcost, distance;
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
            perunitcost = itemView.findViewById(R.id.perunitcost);


        }
    }
}
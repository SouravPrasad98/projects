package com.example.mail;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductWholeseller extends RecyclerView.Adapter<AdapterProductWholeseller.HolderProductWholeseller> implements Filterable {
    private FilterProduct_wholeseller filter;

    private Context context;
    public ArrayList<ModelProduct> productList, filterList;
    public AdapterProductWholeseller(Context context, ArrayList<ModelProduct> productList){
        this.context = context;
        this.productList = productList;
        this.filterList = filterList;
    }


    @NonNull
    @Override
    public HolderProductWholeseller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_seller,parent,false);
        return new HolderProductWholeseller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductWholeseller holder, int position) {
        ModelProduct modelProduct = productList.get(position);
        String id = modelProduct.getProductId();
        String uid = modelProduct.getUid();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountnote();
        String discountPrice = modelProduct.getDiscountprice();
        String productCategory = modelProduct.getProductcategory();
        String prodectDescription = modelProduct.getProductdescription();
        String icon = modelProduct.getProductIcon();
        String quantity = modelProduct.getProductquantity();
        String title = modelProduct.getProductTitle();
        String timestamp = modelProduct.getTimestamp();
        String productPrice = modelProduct.getProductprice();

        holder.productname.setText(title);
        holder.productquantity.setText(quantity);
        holder.discountnote.setText(discountNote);
        holder.productprice.setText("$"+productPrice);
        holder.discountedpriceEt.setText("$"+discountPrice);
        if(discountAvailable.equals("true")){
           holder.discountedpriceEt.setVisibility(View.VISIBLE);
           holder.discountnote.setVisibility(View.VISIBLE);
           holder.productprice.setPaintFlags(holder.productprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.discountedpriceEt.setVisibility(View.GONE);
            holder.discountnote.setVisibility(View.GONE);
        }

        try{
            Picasso.get().load(icon).placeholder(R.drawable.cart_24).into(holder.productIconIv);
        }

        catch (Exception e){
            holder.productIconIv.setImageResource(R.drawable.cart_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new FilterProduct_wholeseller(this, filterList);
        }
        return filter;
    }

    class HolderProductWholeseller extends RecyclerView.ViewHolder{


        private ImageView productIconIv;
        private TextView productname,discountnote,productquantity,productprice,discountedpriceEt;

        public HolderProductWholeseller(@NonNull View itemView) {
            super(itemView);
            productIconIv = itemView.findViewById(R.id.productIconIv);
            discountedpriceEt = itemView.findViewById(R.id.discountedpriceEt);
            discountnote = itemView.findViewById(R.id.discountnote);
            productquantity = itemView.findViewById(R.id.productquantity);
            productprice = itemView.findViewById(R.id.productprice);
            productname = itemView.findViewById(R.id.productname);
            }
    }
}

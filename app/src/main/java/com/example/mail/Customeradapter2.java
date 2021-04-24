package com.example.mail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class Customeradapter2 extends RecyclerView.Adapter<Customeradapter2.HolderRetailer> {
   private Context context;
   public ArrayList<RetailerProductModel> productList;
   public Customeradapter2(Context context, ArrayList<RetailerProductModel> productList)
   {
       this.context = context;
       this.productList = productList;

   }



    @NonNull
    @Override
    public Customeradapter2.HolderRetailer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.retailer_row_products, parent, false);
       return new HolderRetailer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Customeradapter2.HolderRetailer holder, int position) {
       RetailerProductModel retailerProductModel = productList.get(position);
        String title = retailerProductModel.getProductTitle();
        String icon = retailerProductModel.getProductIcon();

        holder.productname.setText(title);
        try{
            Picasso.get().load(icon).placeholder(R.drawable.cart_24).into(holder.productIconIv);
        }

        catch (Exception e){
            holder.productIconIv.setImageResource(R.drawable.cart_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Customer_showshops.class);
               intent.putExtra("RetailerList", (Serializable) retailerProductModel.getRetailerList());

            context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private void passData(){

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

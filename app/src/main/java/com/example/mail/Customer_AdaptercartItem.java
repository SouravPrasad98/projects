package com.example.mail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class Customer_AdaptercartItem extends RecyclerView.Adapter<Customer_AdaptercartItem.HolderCartItem> {
    private Context context;
    private ArrayList<ModelCartItem>  cartItems;

    public Customer_AdaptercartItem(Context context, ArrayList<ModelCartItem> cartItems){
        this.context = context;
        this.cartItems =cartItems;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_cart, parent, false);
        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, int position) {
        ModelCartItem modelCartItem = cartItems.get(position);
        String id = modelCartItem.getId();
        String getpId = modelCartItem.getpId();
        String title = modelCartItem.getName();
        String cost = modelCartItem.getCost();
        String price = modelCartItem.getPrice();
        String quantity = modelCartItem.getQuantity();

        holder.itemQuatiTv.setText("["+quantity+"]");
        holder.itemPriceTv.setText(""+cost);
        holder.itemTitleTv.setText(""+title);
        holder.itemPriceEachTv.setText(""+price);

        holder.itemRemoveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyDB easyDB =EasyDB.init(context, "ITEMS_DB")
                        .setTableName("ITEMS_TABLE")
                        .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                        .addColumn(new Column("Item_PId", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                        .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
                        .doneTableColumn();

                easyDB.deleteRow(1, id);
                Toast.makeText(context, "Removed from cart...", Toast.LENGTH_SHORT).show();

                cartItems.remove(position);
                notifyItemChanged(position);
                notifyDataSetChanged();

                double tx = Double.parseDouble((((ShopdetailsActivity)context).allTotalPriceTv.getText().toString().trim().replace("$", "")));
                double totalPrice = tx - Double.parseDouble(cost.replace("$", ""));
                double deliverFee = Double.parseDouble((((ShopdetailsActivity)context).deliveryFee.replace("$", "")));
                double sTotalPrice = Double.parseDouble(String.format("%.2f", totalPrice)) - Double.parseDouble(String.format("%.2f", deliverFee));
                ((ShopdetailsActivity)context).allTotalPrice = 0.00;
                ((ShopdetailsActivity)context).sTotalTv.setText(String.format("%.2f", sTotalPrice));
                ((ShopdetailsActivity)context).allTotalPriceTv.setText("$"+String.format("%.2f", Double.parseDouble(String.format("%.2f", totalPrice))));
                ((ShopdetailsActivity)context).cartCount();

            }
        });



    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class HolderCartItem extends RecyclerView.ViewHolder {
        private TextView itemTitleTv,itemPriceTv, itemPriceEachTv, itemQuatiTv, itemRemoveTv;
        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            itemTitleTv = itemView.findViewById(R.id.itemTitleTv);
            itemPriceEachTv = itemView.findViewById(R.id.itemPriceEachTv);
            itemPriceTv = itemView.findViewById(R.id.itemPriceTv);
            itemQuatiTv = itemView.findViewById(R.id.itemQauntityTv);
            itemRemoveTv = itemView.findViewById(R.id.itemRemoveTv);
        }
    }
}

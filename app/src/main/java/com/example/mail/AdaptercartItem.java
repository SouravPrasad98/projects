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

public class AdaptercartItem extends RecyclerView.Adapter<AdaptercartItem.HolderCartItem> {
    private Context context;
    private ArrayList<ModelCartItem>  cartItems;

    public AdaptercartItem(Context context, ArrayList<ModelCartItem> cartItems){
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



        Double x = Double.parseDouble(quantity);
        Double y = Double.parseDouble(cost);
        Double z =x*y;
        String a = Double.toString(z);




        holder.itemQuatiTv.setText("["+quantity+"]");
        holder.itemPriceTv.setText(""+price);
        holder.itemTitleTv.setText(""+title);
        holder.itemPriceEachTv.setText(""+cost);

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


                double subTotalWithoutDiscount = ((ShopdetailsActivity)context).allTotalPrice;
                double subTotalAfterProductRemove = subTotalWithoutDiscount - Double.parseDouble(a.replace("$", ""));
                ((ShopdetailsActivity)context).allTotalPrice = subTotalAfterProductRemove;
                ((ShopdetailsActivity)context).sTotalTv.setText("$"+ String.format("%.2f", ((ShopdetailsActivity)context).allTotalPrice));

                double promoPrice = Double.parseDouble(((ShopdetailsActivity)context).promoPrice);
                double deliveryFee = Double.parseDouble(((ShopdetailsActivity)context).deliveryFee.replace("$", ""));

                if(((ShopdetailsActivity)context).isPromoCodeApplied){
                    if(subTotalAfterProductRemove< Double.parseDouble(((ShopdetailsActivity)context).promoMinimumOrderPrice)){
                        Toast.makeText(context, "This code is valid for order with minimum amount: $"+ ((ShopdetailsActivity)context).promoMinimumOrderPrice, Toast.LENGTH_SHORT).show();
                        ((ShopdetailsActivity)context).applyBtn.setVisibility(View.GONE);
                        ((ShopdetailsActivity)context).promoDescriptionTv.setVisibility(View.GONE);
                        ((ShopdetailsActivity)context).promoDescriptionTv.setText("");
                        ((ShopdetailsActivity)context).discountTv.setText("$0");
                        ((ShopdetailsActivity)context).isPromoCodeApplied= false;

                        ((ShopdetailsActivity)context).allTotalPriceTv.setText("$" + String.format("%.2f", Double.parseDouble(String.format("%.2f", subTotalAfterProductRemove+ deliveryFee))));

                    }
                    else{
                        ((ShopdetailsActivity)context).applyBtn.setVisibility(View.VISIBLE);
                        ((ShopdetailsActivity)context).promoDescriptionTv.setVisibility(View.VISIBLE);
                        ((ShopdetailsActivity)context).promoDescriptionTv.setText(((ShopdetailsActivity)context).promoDescription);

                        ((ShopdetailsActivity)context).isPromoCodeApplied= true;

                        ((ShopdetailsActivity)context).allTotalPriceTv.setText("$" + String.format("%.2f", Double.parseDouble(String.format("%.2f", subTotalAfterProductRemove+ deliveryFee - promoPrice))));
                    }
                }
                else{
                    ((ShopdetailsActivity)context).allTotalPriceTv.setText("$"+ String.format("%.2f", Double.parseDouble(String.format("%.2f", subTotalAfterProductRemove + deliveryFee))));

                }

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

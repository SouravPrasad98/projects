package com.example.mail;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterProductShopDetails extends RecyclerView.Adapter<AdapterProductShopDetails.HolderProductRetailer> implements Filterable {
    public ArrayList<ModelProduct> productslist, filterlist;
    private Context context;
    private FilterProduct_Shopdetails filter;

    public AdapterProductShopDetails(Context context, ArrayList<ModelProduct>productslist){
        this.context = context;
        this.productslist = productslist;
        this.filterlist = productslist;
    }

    @NonNull
    @Override
    public HolderProductRetailer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_retailer, parent, false);
        return new HolderProductRetailer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductRetailer holder, int position) {
    ModelProduct modelProduct = productslist.get(position);
        String id = modelProduct.getProductId();
        String uid = modelProduct.getUid();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountNote = modelProduct.getDiscountnote();
        String discountPrice = modelProduct.getDiscountprice();
        String productCategory = modelProduct.getProductcategory();
        String prodectDescription = modelProduct.getProductdescription();
        String icon = modelProduct.getProductIcon();
        String percost = modelProduct.getPerunitCost();
        String quantity = modelProduct.getProductquantity();
        String title = modelProduct.getProductTitle();
        String timestamp = modelProduct.getTimestamp();
        String productPrice = modelProduct.getProductprice();

        holder.productquantity.setText("Quantity : " + quantity);
        holder.productname.setText(title);
        holder.discountedpriceEt.setText(discountPrice);
        holder.discountnote.setText(discountNote + "% OFF");
        holder.perunitcost.setText("Rs. " + productPrice);
        holder.productprice.setText(productPrice);

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
      holder.addtocart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
         showQuantityDialog(modelProduct);
    }
});
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private double cost= 0;
    private double finalcost= 0;
    private int quantity= 0;
    private void showQuantityDialog(ModelProduct modelProduct) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_quantity, null);
    TextView nameTv = view.findViewById(R.id.nameTv);
    TextView Quantity = view.findViewById(R.id.Quantity);
    TextView discountedPriceTv = view.findViewById(R.id.discountedPriceTv);
    TextView orignalPriceTv = view.findViewById(R.id.orignalPriceTv);
    TextView finalTv = view.findViewById(R.id.finalTv);
    TextView quantityTv = view.findViewById(R.id.quantityTv);
    ImageButton decreBt = view.findViewById(R.id.decreBt);
    ImageButton increBt = view.findViewById(R.id.increBt);
    Button continueBt = view.findViewById(R.id.continueBt);
    TextView discountNoteTv= view.findViewById(R.id.discountNoteTv);



        String discountPrice1 = modelProduct.getDiscountprice();
        String quantity1 = modelProduct.getProductquantity();
        String title1 = modelProduct.getProductTitle();
        String productPrice1 = modelProduct.getProductprice();
        String discountNote1 = modelProduct.getDiscountnote();
        String productid = modelProduct.getProductId();


        String price;
        if(modelProduct.getDiscountAvailable().equals("true")){
            price = modelProduct.getDiscountprice();
            discountNoteTv.setVisibility(View.VISIBLE);
            orignalPriceTv.setPaintFlags(orignalPriceTv.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            discountNoteTv.setVisibility(View.GONE);
            discountedPriceTv.setVisibility(View.GONE);
            price = modelProduct.getProductprice();
        }

        cost = Double.parseDouble(price.replaceAll("$", ""));
        finalcost = Double.parseDouble(price.replaceAll("$", ""));
        quantity = 1;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        nameTv.setText(""+title1);
        Quantity.setText(""+modelProduct.getProductquantity() );
        orignalPriceTv.setText("$"+modelProduct.getProductprice());
        discountedPriceTv.setText("$"+modelProduct.getDiscountprice());
        discountNoteTv.setText(""+modelProduct.getDiscountnote());
        quantityTv.setText(""+quantity);
        finalTv.setText("$"+finalcost);

        AlertDialog dialog = builder.create();
        dialog.show();

        increBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalcost = finalcost + cost;
                quantity++;
                finalTv.setText("$"+finalcost);
                quantityTv.setText(""+quantity);
            }
        });

        decreBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity>1) {
                    finalcost = finalcost - cost;
                    quantity--;
                    finalTv.setText("$" + finalcost);
                    quantityTv.setText("" + quantity);
                }
            }
        });
        continueBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String proname = nameTv.getText().toString().trim();
                String priceeach= price;
                String totalPrice = finalTv.getText().toString().trim().replace("$", "");
                String qUantity = quantityTv.getText().toString().trim();

                addToCart(productid,priceeach,totalPrice,qUantity,proname);
                dialog.dismiss();
            }
        });
    }
    private int itemId =1;

    private void addToCart(String productid, String priceeach, String price, String qUantity, String proname) {
        itemId++;
        EasyDB easyDB =EasyDB.init(context, "ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id", new String[]{"text","unique"}))
                        .addColumn(new Column("Item_PId", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price", new String[]{"text","not null"}))
        .addColumn(new Column("Item_Quantity", new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each", new String[]{"text","not null"}))
                .doneTableColumn();

        Boolean b = easyDB.addData("Item_Id", itemId)
        .addData("Item_PId", productid)
        .addData("Item_Name", proname)
        .addData("Item_Price", price)
        .addData("Item_Quantity", qUantity)
        .addData("Item_Price_Each", priceeach)
        .doneDataAdding();

        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();

        ((Customer_RetailerShopdetailsActivity)context).cartCount();

    }

    @Override
    public int getItemCount() {
        return productslist.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterProduct_Shopdetails(this, filterlist);
        }
        return filter;
    }

    class HolderProductRetailer extends RecyclerView.ViewHolder{

private ImageView productIconIv;
private TextView  productname,productquantity,perunitcost,addtocart,discountedpriceEt,
        productprice,discountnote;
        public HolderProductRetailer(@NonNull View itemView) {
            super(itemView);
            productIconIv = itemView.findViewById(R.id.productIconIv);
            productname = itemView.findViewById(R.id.productname);
            productquantity = itemView.findViewById(R.id.productquantity);
            addtocart = itemView.findViewById(R.id.addtocart);
            discountedpriceEt = itemView.findViewById(R.id.discountedpriceEt);
            productprice = itemView.findViewById(R.id.productprice);
            discountnote = itemView.findViewById(R.id.discountnote);
            perunitcost = itemView.findViewById(R.id.perunitcost);
        }
    }
}

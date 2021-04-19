package com.example.mail;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductRetailer extends RecyclerView.Adapter<AdapterProductRetailer.HolderProductRetailer> implements Filterable {
    private FilterProduct_retailer filter;

    private Context context;
    public ArrayList<ModelProduct> productList, filterList;
    public AdapterProductRetailer(Context context, ArrayList<ModelProduct> productList){
        this.context = context;
        this.productList = productList;
        this.filterList = filterList;
    }


    @NonNull
    @Override
    public HolderProductRetailer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_seller,parent,false);
        return new HolderProductRetailer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductRetailer holder, int position) {
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
                detailsBottomSheet(modelProduct);

            }
        });


    }

    private void detailsBottomSheet(ModelProduct modelProduct) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.product_details_retailer, null);
        bottomSheetDialog.setContentView(view);

        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageButton editBtn = view.findViewById(R.id.editBtn);
        ImageView productIconIv = view.findViewById(R.id.productIconIv);
        TextView  discountNoteTv= view.findViewById(R.id.discountNoteTv);
        TextView  descriptionTv= view.findViewById(R.id.descriptionTv);
        TextView  quantityTv= view.findViewById(R.id.quantityTv);
        TextView  categoryTv= view.findViewById(R.id.categoryTv);
        TextView  discountedPriceTv= view.findViewById(R.id.discountedPriceTv);
        TextView  orignalPriceTv= view.findViewById(R.id.orignalPriceTv);
        TextView  titleTv= view.findViewById(R.id.titleTv);


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

        titleTv.setText(title);
        descriptionTv.setText(prodectDescription);
        categoryTv.setText(productCategory);
        quantityTv.setText(quantity);
        discountedPriceTv.setText("$"+discountPrice);
        orignalPriceTv.setText("$"+productPrice);
        discountNoteTv.setText(discountNote);
        if(discountAvailable.equals("true")){
            discountedPriceTv.setVisibility(View.VISIBLE);
            discountNoteTv.setVisibility(View.VISIBLE);
            orignalPriceTv.setPaintFlags(orignalPriceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            discountedPriceTv.setVisibility(View.GONE);
            discountNoteTv.setVisibility(View.GONE);

        }
        try{
            Picasso.get().load(icon).placeholder(R.drawable.cart_24).into(productIconIv);
        }

        catch (Exception e){
            productIconIv.setImageResource(R.drawable.cart_24);
        }

        bottomSheetDialog.show();


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(context,EditRetailerProductActivity.class);
                intent.putExtra("productId", id);
                context.startActivity(intent);

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete product"+ title+"?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct(id);

                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

            }
        });



    }

    private void deleteProduct(String id) {
        FirebaseAuth  firebaseAuth= FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Retailer");
        reference.child(firebaseAuth.getUid()).child("RetailerProducts").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

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
            filter = new FilterProduct_retailer(this, filterList);
        }
        return filter;
    }

    class HolderProductRetailer extends RecyclerView.ViewHolder{


        private ImageView productIconIv;
        private TextView productname,discountnote,productquantity,productprice,discountedpriceEt;

        public HolderProductRetailer(@NonNull View itemView) {
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

package com.example.mail;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterOrderByCustomer extends RecyclerView.Adapter<AdapterOrderByCustomer.HolderOrderRetailer> {

    private Context context;
    private ArrayList<ModelOrderWholeseller> orderUsersList;

    public AdapterOrderByCustomer(Context context, ArrayList<ModelOrderWholeseller> orderUsersList) {
        this.context = context;
        this.orderUsersList = orderUsersList;
    }

    @NonNull
    @Override
    public HolderOrderRetailer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order_retailer,parent,false);

        return new HolderOrderRetailer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderRetailer holder, int position) {
        ModelOrderWholeseller modelOrderWholeseller =orderUsersList.get(position);
        String OrderId = modelOrderWholeseller.getOrderId();
        String OrderBy = modelOrderWholeseller.getOrderBy();
        String OrderCost = modelOrderWholeseller.getOrderCost();
        String OrderStatus = modelOrderWholeseller.getOrderStatus();
        String OrderTime = modelOrderWholeseller.getOrderTime();
        String OrderTo = modelOrderWholeseller.getOrderTo();
    
        loadShopInfo(modelOrderWholeseller,holder);

        holder.amountTv.setText("Amount: $ "+ OrderCost);
        holder.statusTv.setText(OrderStatus);
        holder.orderIdTv.setText("OrderId :" +OrderId);
        if(OrderStatus.equals("In Progress"))
        {
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else if (OrderStatus.equals("Completed"))
        {
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.forestgreen));
        }
        else if (OrderStatus.equals("Cancelled"))
        {
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.tomatored));
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(OrderTime));
        String formatedDate = DateFormat.format("dd/MM/yyyy",calendar).toString();


        holder.dateTv.setText(formatedDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderbyCustomerDetailsActivity.class);
                intent.putExtra("orderTo",OrderTo);
                intent.putExtra("orderId", OrderId);
                intent.putExtra("orderBy", OrderBy);
                context.startActivity(intent);
            }
        });

    }

    private void loadShopInfo(ModelOrderWholeseller modelOrderWholeseller, HolderOrderRetailer holder) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.child(modelOrderWholeseller.getOrderTo())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String shopName = "" + dataSnapshot.child("bussinessname").getValue();
                        holder.shopNameTv.setText(shopName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return orderUsersList.size();
    }

    class HolderOrderRetailer extends RecyclerView.ViewHolder{
        private TextView orderIdTv,dateTv,shopNameTv,amountTv,statusTv;

        public HolderOrderRetailer(@NonNull View itemView) {
            super(itemView);

            orderIdTv = itemView.findViewById(R.id.orderTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            shopNameTv = itemView.findViewById(R.id.shopNameTv);
            amountTv = itemView.findViewById(R.id.amountTv);
            statusTv = itemView.findViewById(R.id.statusTv);

        }
    }





}

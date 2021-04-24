package com.example.mail;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class AdapterOrderByCustomer extends RecyclerView.Adapter<AdapterOrderByCustomer.HolderOrderWholeseller> {
    public ArrayList<ModelOrderWholeseller> modelOrderWholesellerArrayList;
    private Context context;



    public AdapterOrderByCustomer(Context context, ArrayList<ModelOrderWholeseller> modelOrderWholesellerArrayList) {
        this.context = context;
        this.modelOrderWholesellerArrayList =modelOrderWholesellerArrayList;

    }

    @NonNull
    @Override
    public HolderOrderWholeseller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order_wholeseller, parent, false);
        return new HolderOrderWholeseller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderWholeseller holder, int position) {
   ModelOrderWholeseller modelOrderWholeseller = modelOrderWholesellerArrayList.get(position);
   String orderBy = modelOrderWholeseller.getOrderBy();
   String orderTo = modelOrderWholeseller.getOrderTo();
   String orderCost = modelOrderWholeseller.getOrderCost();
   String orderStatus = modelOrderWholeseller.getOrderStatus();
   String orderTime = modelOrderWholeseller.getOrderTime();
   String orderId = modelOrderWholeseller.getOrderId();
   loadRetailerInfo(modelOrderWholeseller,holder);
   holder.amountTv.setText("Amount: $" +orderCost);
   holder.statusTv.setText(orderStatus);
   holder.orderIdTv.setText("Order ID:"+orderId);
        if(orderStatus.equals("In Progress"))
        {
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else if (orderStatus.equals("Completed"))
        {
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.forestgreen));
        }
        else if (orderStatus.equals("Cancelled"))
        {
            holder.statusTv.setTextColor(context.getResources().getColor(R.color.tomatored));
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(orderTime));
        String formatedDate = DateFormat.format("dd/MM/yyyy",calendar).toString();


        holder.orderDateTv.setText(formatedDate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,OrderDetailsWholesellerActivity.class);
                intent.putExtra("orderId", orderId);
                intent.putExtra("orderBy", orderBy);
                intent.putExtra("orderTo", orderTo);
                context.startActivity(intent);

            }
        });
    }

    private void loadRetailerInfo(ModelOrderWholeseller modelOrderWholeseller, HolderOrderWholeseller holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Customer");
        ref.child(modelOrderWholeseller.getOrderBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String email = "" + dataSnapshot.child("email").getValue();
                        holder.emailTv.setText(email);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return modelOrderWholesellerArrayList.size();
    }



    class HolderOrderWholeseller extends RecyclerView.ViewHolder{
         private TextView amountTv, statusTv,emailTv,orderDateTv,orderIdTv;
        public HolderOrderWholeseller(@NonNull View itemView) {
            super(itemView);
            orderIdTv = itemView.findViewById(R.id.orderIdTv);
            orderDateTv= itemView.findViewById(R.id.orderDateTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            statusTv= itemView.findViewById(R.id.statusTv);
            amountTv = itemView.findViewById(R.id.amountTv);



        }
    }
}

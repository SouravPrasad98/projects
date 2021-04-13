package com.example.mail.Retailer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mail.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.$Gson$Preconditions;

public class RetailerItemsFirebaseAdapter extends FirebaseRecyclerAdapter<RetailerAddItemHelper,RetailerItemsFirebaseAdapter.myViewHolder>
{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RetailerItemsFirebaseAdapter(@NonNull FirebaseRecyclerOptions<RetailerAddItemHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RetailerAddItemHelper model) {
        holder.type.setText(model.getType());
        if(model.getScale()==1)
            holder.scale.setText("Kg");
        holder.quantity.setText(String.valueOf(model.getQuantity()));
        holder.price.setText(String.valueOf(model.getPrice()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Retailer").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(model.getType()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.retailer_items_layout,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView type,scale,quantity,price;
        Button delete;
        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
            type= (TextView) itemView.findViewById(R.id.retailerRecycleName);
            scale= (TextView) itemView.findViewById(R.id.retailerRecycleScale);
            quantity= (TextView) itemView.findViewById(R.id.retailerRecycleQuantity);
            price=(TextView)itemView.findViewById(R.id.retailerRecyclePrice);
            delete=(Button) itemView.findViewById(R.id.retailerItemDelete);
        }
    }
}

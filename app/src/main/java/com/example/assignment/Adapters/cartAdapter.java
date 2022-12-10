package com.example.assignment.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.Models.cartModel;
import com.example.assignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.CartViewHolders>{

    Context context;
    List<cartModel> cartModelList;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    public cartAdapter(Context context, List<cartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        auth = FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public CartViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolders holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(cartModelList.get(position).getImageURL()).into(holder.cart_image);
        holder.name_cart.setText(cartModelList.get(position).getName());
        holder.price_cart.setText(cartModelList.get(position).getPrice());
        holder.quantity.setText(cartModelList.get(position).getQuantity());
        holder.TPitem_cart.setText("RM "+cartModelList.get(position).getTPcart());


        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference= FirebaseDatabase.getInstance().getReference().child("Cart").child(auth.getCurrentUser().getUid()).child("Current User");

                databaseReference.child(cartModelList.get(position).getCartID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartModelList.remove(cartModelList.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class CartViewHolders extends RecyclerView.ViewHolder {

        ImageView cart_image;
        TextView name_cart;
        TextView price_cart;
        TextView quantity;
        TextView TPitem_cart;
        ImageView delete_item;

        public CartViewHolders(@NonNull View itemView) {
            super(itemView);

            cart_image= itemView.findViewById(R.id.cart_Image);
            name_cart= itemView.findViewById(R.id.name_cart);
            price_cart= itemView.findViewById(R.id.price_cart);
            quantity= itemView.findViewById(R.id.quantity_cart);
            TPitem_cart= itemView.findViewById(R.id.TPitem_cart);
            delete_item=itemView.findViewById(R.id.delete_img);
        }
    }
}

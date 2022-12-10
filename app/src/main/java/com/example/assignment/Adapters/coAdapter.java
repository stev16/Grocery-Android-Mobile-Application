package com.example.assignment.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.Models.coModel;
import com.example.assignment.R;

import java.util.List;

public class coAdapter extends RecyclerView.Adapter<coAdapter.coViewHolder> {

    Context context;
    List<coModel> coModelList;

    public coAdapter(Context context, List<coModel> coModelList) {
        this.context = context;
        this.coModelList = coModelList;
    }

    @NonNull
    @Override
    public coAdapter.coViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new coViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull coAdapter.coViewHolder holder, int position) {
        holder.name.setText(coModelList.get(position).getName());
        holder.quantity.setText("X"+coModelList.get(position).getTotalQuantity());
        holder.price.setText(String.valueOf(coModelList.get(position).getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return coModelList.size();
    }

    public class coViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, price;

        public coViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.coName_item);
            quantity= itemView.findViewById(R.id.coQuantity_item);
            price= itemView.findViewById(R.id.coPrice_Item);
        }
    }
}

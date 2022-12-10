package com.example.assignment.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.Models.itemModel;
import com.example.assignment.R;
import com.example.assignment.detailItem;

import java.util.List;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewHolders> {

    Context context;

    public itemAdapter(Context context, List<itemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }

    List<itemModel> itemModelList;

    @NonNull
    @Override
    public itemViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new itemViewHolders(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolders holder, int position) {
        Glide.with(context).load(itemModelList.get(position).getImageURL()).into(holder.prodImg);
        holder.name.setText(itemModelList.get(position).getName());
        holder.price.setText(itemModelList.get(position).getPrice());
        holder.desc.setText(itemModelList.get(position).getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, detailItem.class);
                intent.putExtra("detail", itemModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class itemViewHolders extends RecyclerView.ViewHolder {

        ImageView prodImg;
        TextView name;
        TextView price;
        TextView desc;

        public itemViewHolders(@NonNull View itemView) {
            super(itemView);

            prodImg= itemView.findViewById(R.id.prodImg);
            name= itemView.findViewById(R.id.text_name);
            price= itemView.findViewById(R.id.text_price);
            desc= itemView.findViewById(R.id.description);
        }
    }
}

package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.assignment.Adapters.itemAdapter;
import com.example.assignment.Models.itemModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class dailyNeeds extends AppCompatActivity {

    List<itemModel> itemModelList;
    itemAdapter itemAdapter;
    RecyclerView itemRecycler;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_needs);

        itemRecycler=findViewById(R.id.itemRecycler);
        itemRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        itemModelList= new ArrayList<>();
        itemAdapter = new itemAdapter(dailyNeeds.this, itemModelList);
        itemRecycler.setAdapter(itemAdapter);

        db= FirebaseDatabase.getInstance().getReference().child("Item").child("Daily");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    itemModel daily= new itemModel();
                    daily.setImageURL(itemSnapshot.child("imageURL").getValue().toString());
                    daily.setName(itemSnapshot.child("name").getValue().toString());
                    daily.setPrice(itemSnapshot.child("price").getValue().toString());
                    daily.setDesc(itemSnapshot.child("desc").getValue().toString());
                    itemModelList.add(daily);

                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        //going to cart
        ImageView cart_page=findViewById(R.id.cart_Img);

        cart_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyNeeds= new Intent(dailyNeeds.this, cartActivity.class);
                startActivity(dailyNeeds);
            }
        });

        //back button
        ImageView back_button=findViewById(R.id.back_Img);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
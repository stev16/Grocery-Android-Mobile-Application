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

public class snackNbeverage extends AppCompatActivity {

    List<itemModel> itemModelList;
    itemAdapter itemAdapter;
    RecyclerView itemRecycler;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_nbeverage);

        itemRecycler=findViewById(R.id.itemRecycler);
        itemRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        itemModelList= new ArrayList<>();
        itemAdapter = new itemAdapter(snackNbeverage.this, itemModelList);
        itemRecycler.setAdapter(itemAdapter);

        db= FirebaseDatabase.getInstance().getReference().child("Item").child("SnaRage");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    itemModel snack= new itemModel();
                    snack.setImageURL(itemSnapshot.child("imageURL").getValue().toString());
                    snack.setName(itemSnapshot.child("name").getValue().toString());
                    snack.setPrice(itemSnapshot.child("price").getValue().toString());
                    snack.setDesc(itemSnapshot.child("desc").getValue().toString());
                    itemModelList.add(snack);

                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView cart_page=findViewById(R.id.cart_Img);

        cart_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyNeeds= new Intent(snackNbeverage.this, cartActivity.class);
                startActivity(dailyNeeds);
            }
        });

        ImageView back_button=findViewById(R.id.back_Img);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment.Adapters.cartAdapter;
import com.example.assignment.Models.cartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class cartActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView cartRecycler;
    cartAdapter cartAdapter;
    List<cartModel> cartModelList;
    Button goCheckout_page;
    ImageView back_img;

    FirebaseAuth auth;
    TextView cartAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        cartAmount=findViewById(R.id.cart_TP);
        goCheckout_page=findViewById(R.id.checkout_btn);
        back_img=findViewById(R.id.back_image_button);

        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Cart").child(auth.getCurrentUser().getUid()).child("Current User");

        cartRecycler= findViewById(R.id.cartRecycler);
        cartRecycler.setLayoutManager(new LinearLayoutManager(this));

        cartModelList= new ArrayList<>();
        cartAdapter = new cartAdapter(this, cartModelList);
        cartRecycler.setAdapter(cartAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot cartSnapshot: snapshot.getChildren()){

                    cartModel cart= new cartModel();
                    cart.setImageURL(cartSnapshot.child("imageURL").getValue().toString());
                    cart.setName(cartSnapshot.child("name").getValue().toString());
                    cart.setPrice(cartSnapshot.child("price").getValue().toString());
                    cart.setQuantity(cartSnapshot.child("totalQuantity").getValue()+" Quantities".toString());
                    cart.setTPcart(Double.valueOf(String.valueOf(cartSnapshot.child("totalPrice").getValue())));

                    String cartID= cartSnapshot.getKey();
                    cart.setCartID(cartID);

                    cartModelList.add(cart);
                    cartAdapter.notifyDataSetChanged();
                }


                calculateTotalPayment(cartModelList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"No Data", Toast.LENGTH_SHORT).show();
            }
        });


        ImageView back_button=findViewById(R.id.back_image_button);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        goCheckout_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCheckout= new Intent(cartActivity.this, CheckoutActivity.class);
                startActivity(goCheckout);
            }
        });
    }

    private void calculateTotalPayment(List<cartModel> cartModelList) {

        Double totalAmount= 0.00;
        for (cartModel cartModel: cartModelList){
            totalAmount+= cartModel.getTPcart();

        }

        cartAmount.setText("Total Cart Amount : RM "+ String.format("%.2f", totalAmount));


    }


}
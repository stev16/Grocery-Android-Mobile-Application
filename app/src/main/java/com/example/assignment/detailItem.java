package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.assignment.Models.itemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class detailItem extends AppCompatActivity {

    ImageView detail_Img;
    TextView name, price, desc;
    Button addToCart;
    ImageView cancel;

    TextView quantity;
    int totalQuantity= 1;
    double totalPrice=0;
    itemModel itemModel= null;
    ImageView add, remove;

    //firebase
    DatabaseReference dbRef;
    FirebaseAuth auth;
    Integer cartID=new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        detail_Img=findViewById(R.id.detail_Img);
        name= findViewById(R.id.detailname_Item);
        price= findViewById(R.id.detailprice_Item);
        desc= findViewById(R.id.detaildesc_Item);
        cancel=findViewById(R.id.cancel_img);

        //quantity components declaration
        quantity=findViewById(R.id.quantity_text);
        add=findViewById(R.id.add_Img);
        remove=findViewById(R.id.remove_Img);

        //firebase
        auth= FirebaseAuth.getInstance();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Cart").child(auth.getCurrentUser().getUid()).child("Current User").child("Cart"+cartID);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Object object= getIntent().getSerializableExtra("detail");
        if (object instanceof itemModel){
            itemModel=(itemModel) object;
        }


        //if item model= null thennnn
        if (itemModel!=null){
            Glide.with(getApplicationContext()).load(itemModel.getImageURL()).into(detail_Img);
            name.setText(itemModel.getName());
            desc.setText(itemModel.getDesc());
            price.setText("Price: RM "+itemModel.getPrice()+"/quantity");

            totalPrice= Double.parseDouble(itemModel.getPrice())* totalQuantity;

        }

        //add to cart declaration
        addToCart= findViewById(R.id.addCart_button);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCart();
            }
        });


        //add and deduct quantity
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity>=1){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice= Double.parseDouble(itemModel.getPrice())* totalQuantity;

                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity>1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    totalPrice= Double.parseDouble(itemModel.getPrice())* totalQuantity;

                }
                else{
                    Toast.makeText(getApplicationContext(), "At least one item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addingToCart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate= Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("dd MM, yyyy");
        saveCurrentDate= currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime= currentTime.format(calForDate.getTime());


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("name").setValue(name.getText().toString());
                snapshot.getRef().child("price").setValue(price.getText().toString());
                snapshot.getRef().child("imageURL").setValue(itemModel.getImageURL());
                snapshot.getRef().child("desc").setValue(desc.getText().toString());
                snapshot.getRef().child("currentDate").setValue(saveCurrentDate);
                snapshot.getRef().child("currentTime").setValue(saveCurrentTime);
                snapshot.getRef().child("totalQuantity").setValue(quantity.getText().toString());
                snapshot.getRef().child("totalPrice").setValue(String.format("%.2f",totalPrice));

                Toast.makeText(getApplicationContext(),"Added", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
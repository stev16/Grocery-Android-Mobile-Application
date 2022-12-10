package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button daily;
    Button snb;
    ImageView cart_page;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daily= findViewById(R.id.button);
        snb= findViewById(R.id.button2);
        cart_page= findViewById(R.id.cart_page);

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyNeeds= new Intent(MainActivity.this, dailyNeeds.class);
                startActivity(dailyNeeds);
            }
        });

        snb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent snackNbeverage= new Intent(MainActivity.this, snackNbeverage.class);
                startActivity(snackNbeverage);
            }
        });


        cart_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dailyNeeds= new Intent(MainActivity.this, cartActivity.class);
                startActivity(dailyNeeds);
            }
        });


    }


}
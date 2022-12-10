package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThankyouPage extends AppCompatActivity {

    Button done;

    FirebaseAuth auth;
    DatabaseReference dbref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_page);

        done= findViewById(R.id.done);

        auth=FirebaseAuth.getInstance();
        dbref= FirebaseDatabase.getInstance().getReference().child("Cart").child(auth.getCurrentUser().getUid()).child("Current User");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent home= new Intent(ThankyouPage.this, MainActivity.class);
                            startActivity(home);
                        }
                    }
                });
            }
        });
    }
}
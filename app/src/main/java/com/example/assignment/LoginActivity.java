package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button signIn;
    TextView register;
    EditText username, userpassword;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        register= findViewById(R.id.signUp_txt);
        signIn= findViewById(R.id.signIn_btn);
        username=findViewById(R.id.userEmail_login);
        userpassword=findViewById(R.id.Password_text);
        auth= FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }

        });


    }

    private void loginUser() {
        String userName= username.getText().toString();
        String userPassword= userpassword.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userPassword.length()<8){
            Toast.makeText(this, "Your Password need at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        //login process
        auth.signInWithEmailAndPassword(userName, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this, "Check Your Login Access", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signUpPage(View view) {
        Intent signUp= new Intent(LoginActivity.this, registerActivity.class);
        startActivity(signUp);
    }
}
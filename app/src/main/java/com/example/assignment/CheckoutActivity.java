package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;


import com.example.assignment.Adapters.coAdapter;
import com.example.assignment.Models.coModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CheckoutActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView coItemRecycler;
    coAdapter coAdapter;
    List<coModel> coModelList;
    TextView orderID, time;

    Integer orderNumber= new Random().nextInt();

    FirebaseAuth auth;
    TextView dFees;
    TextView totalCost;
    ImageView back;

    //pdf purpose
    Display mDisplay;
    String imagesUri;
    String path;
    Bitmap bitmap;

    int totalHeight;
    int totalWidth;

    public static final int READ_PHONE = 110;
    String file_name = "Invoice";
    File myPath;
    Button confirm, cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        dFees=findViewById(R.id.DF_text);
        totalCost=findViewById(R.id.totalCost_text);
        orderID=findViewById(R.id.orderID);
        time=findViewById(R.id.time_checkout);
        cancel= findViewById(R.id.canc);
        back=findViewById(R.id.back_image_button);

        coItemRecycler= findViewById(R.id.coItemRecycler);
        coItemRecycler.setLayoutManager(new LinearLayoutManager(this));

        coModelList= new ArrayList<>();
        coAdapter = new coAdapter(this, coModelList);
        coItemRecycler.setAdapter(coAdapter);

        databaseReference.child("Cart").child(auth.getCurrentUser().getUid()).child("Current User")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot coSnapshot: snapshot.getChildren()){

                    coModel checkout= new coModel();
                    checkout.setName(coSnapshot.child("name").getValue().toString());
                    checkout.setTotalQuantity(coSnapshot.child("totalQuantity").getValue().toString());
                    checkout.setTotalPrice(Double.valueOf(String.valueOf(coSnapshot.child("totalPrice").getValue())));

                    coModelList.add(checkout);
                    coAdapter.notifyDataSetChanged();
                }


                calculateTotalPayment(coModelList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"No Data", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a= new Intent(CheckoutActivity.this, cartActivity.class);
                startActivity(a);
            }
        });

        //for inside the boxes
        orderID.setText(String.valueOf(orderNumber));
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate= Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("dd MM, yyyy");
        saveCurrentDate= currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime= currentTime.format(calForDate.getTime());

        time.setText(saveCurrentDate +"||"+ saveCurrentTime);

        //start progressing to make a PDF
        confirm= findViewById(R.id.confirm);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();

        if(Build.VERSION.SDK_INT >= 26){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
            }else{
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PHONE);
            }
        }

        //button for pdf and to thank you page
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disappear the buttons in pdf
                confirm.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                back.setVisibility(View.GONE);

                takeScreenShot();

                cancel.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);

                Intent tyPage= new Intent(CheckoutActivity.this, ThankyouPage.class);
                startActivity(tyPage);
            }
        });



    }

    private void calculateTotalPayment(List<coModel> coModelList) {

        Double totalAmount= 0.00;
        Double delivery = 5.00;
        for (coModel checkout: coModelList){
            totalAmount+= checkout.getTotalPrice();

        }

        delivery += totalAmount * 0.037;


        Double totalAllPayment= delivery+totalAmount;

        dFees.setText("RM "+ String.format("%.2f", delivery));
        totalCost.setText("RM "+ String.format("%.2f", totalAllPayment));
    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth){

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();

        if(bgDrawable != null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);
        return returnedBitmap;
    }

    private void takeScreenShot(){

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Invoice/");

        if(!folder.exists()){
            boolean success = folder.mkdir();
        }

        path = folder.getAbsolutePath();
        path = path + "/" + file_name + System.currentTimeMillis() + ".pdf";

        View layout = findViewById(R.id.checkout);

        ConstraintLayout cons = findViewById(R.id.checkout);
        totalHeight = cons.getHeight();
        totalWidth = cons.getWidth();

        String external = Environment.getExternalStorageDirectory() + "/Invoice/";
        File file = new File(external);
        if(!file.exists())
            file.mkdir();
        String fileName = file_name + ".jpg";
        myPath = new File(external, fileName);
        imagesUri = myPath.getPath();
        bitmap = getBitmapFromView(layout, totalHeight, totalWidth);

        try{
            FileOutputStream fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 150, fos);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        createPdf();


    }

    private void createPdf() {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth(), this.bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);
        File filePath = new File(path);
        try{
            document.writeTo(new FileOutputStream(filePath));
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Something Wrong: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

        document.close();

        if (myPath.exists())
            myPath.delete();


    }


}
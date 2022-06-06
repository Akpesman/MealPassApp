package com.example.mealpassapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealpassapp.R;
import com.example.mealpassapp.helpers.Connectivity;
import com.example.mealpassapp.model.PayModelClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FoodSellerActivity extends AppCompatActivity {

    Button additem , checkOrder, profile, inbox, delivery, orderHistory, myItems;
    SharedPreferences sharedPref , sharedPrefLogin;
    SharedPreferences.Editor editor , editor2;
    public static String Name , CNIC , Phone , URI, userId="", token, flag;
    public static double lati, longi;
    AlertDialog alertDialog;
    String payType="", payNumber="";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_seller);

        additem = findViewById(R.id.btnAddItem);
        checkOrder = findViewById(R.id.btnCheck);
        delivery = findViewById(R.id.btnDelivery);
        profile = findViewById(R.id.btnProfile);
        inbox = findViewById(R.id.btnInbox);
        orderHistory = findViewById(R.id.btnHistory);
        myItems = findViewById(R.id.btnMyItems);

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putBoolean("StatusFood" , true);
        editor.apply();

        sharedPrefLogin = getSharedPreferences("userLoginData", Context.MODE_PRIVATE);

        Name = sharedPrefLogin.getString("fullName","null");
        CNIC =  sharedPrefLogin.getString("account","null");
        URI = sharedPrefLogin.getString("URI","null");
        Phone = sharedPrefLogin.getString("phone","null");
        userId = sharedPrefLogin.getString("ID","null");
        lati = Double.parseDouble(sharedPrefLogin.getString("latitude","0"));
        longi = Double.parseDouble(sharedPrefLogin.getString("longitude","0"));
        token = sharedPrefLogin.getString("token","null");
        flag = sharedPrefLogin.getString("flag","00");
        editor2 = sharedPrefLogin.edit();

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Connectivity.isConnectedFast(getApplicationContext()) && Connectivity.isConnectedFast(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext() , AddFoodItemActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(FoodSellerActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity.isConnectedFast(getApplicationContext()) && Connectivity.isConnectedFast(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), CheckOrderActivity.class);
                    intent.putExtra("key", "pending");
                    startActivity(intent);
                }else {
                    Toast.makeText(FoodSellerActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity.isConnectedFast(getApplicationContext()) && Connectivity.isConnectedFast(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext() , CheckDeliveryPointsActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(FoodSellerActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity.isConnectedFast(getApplicationContext()) && Connectivity.isConnectedFast(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext() , UpdateProfileActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(FoodSellerActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }
            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity.isConnectedFast(getApplicationContext()) && Connectivity.isConnectedFast(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext() , ChattingListActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(FoodSellerActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }
            }
        });

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckOrderActivity.class);
                intent.putExtra("key", "history");
                startActivity(intent);
            }
        });

        myItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Connectivity.isConnectedFast(getApplicationContext()) && Connectivity.isConnectedFast(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext() , ViewMyItemsActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(FoodSellerActivity.this, "Check Your Network", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_menu_seller, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Logout?");
            alert.setCancelable(false);
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editor.clear();
                    editor.apply();
                    editor2.clear();
                    editor2.apply();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(userId).child("token").setValue("null");
                    Intent intent = new Intent(getApplicationContext() , LoginScreenActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Sign Out", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.mealpassapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealpassapp.R;
import com.example.mealpassapp.adapters.MyItemsListAdapter;
import com.example.mealpassapp.helpers.BaseActivity;
import com.example.mealpassapp.model.FoodModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMyItemsActivity extends BaseActivity{

    RecyclerView recyclerView ;
    DatabaseReference databaseReference;
    MyItemsListAdapter imageAdapter;
    TextView tvNoItems, tvItems;
    List<FoodModel> list;
    Toolbar toolbar;
    SharedPreferences sharedPrefLogin;
    String userId="";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.searchmenu, menu);
            MenuItem searchmenuItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) searchmenuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        imageAdapter.getFilter().filter(newText);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return false;
                }
            });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_items);

        databaseReference = FirebaseDatabase.getInstance().getReference("FoodItem/");
        list = new ArrayList<>();

        showProgressDialog();
        tvNoItems = findViewById(R.id.tvNoitems2);
        tvItems = findViewById(R.id.textIt);
        recyclerView = findViewById(R.id.recyclerView2) ;
        recyclerView.setHasFixedSize(true); ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.itemstoolbar2);
        toolbar.setTitle("Search Your Items");
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.searchmenu);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPrefLogin = getSharedPreferences("userLoginData", Context.MODE_PRIVATE);

        userId = sharedPrefLogin.getString("ID","null");
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                tvNoItems.setText("");
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    FoodModel item = dataSnapshot1.getValue(FoodModel.class);
                    if(userId.equals(item.getSellerId())){
                        list.add(item);
                    }
                }
                if(list.size()>0){
                    imageAdapter = new MyItemsListAdapter(ViewMyItemsActivity.this, list);
                    recyclerView.setAdapter(imageAdapter);
                }else {
                    tvNoItems.setText("No items found");
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
            }});
    }
}
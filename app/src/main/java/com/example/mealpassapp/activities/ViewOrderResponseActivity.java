package com.example.mealpassapp.activities;

import android.os.Bundle;

import com.example.mealpassapp.R;
import com.example.mealpassapp.fragments.AcceptedOrderFragment;
import com.example.mealpassapp.fragments.PendingOrderFragment;
import com.example.mealpassapp.fragments.RejectedOrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

public class ViewOrderResponseActivity extends AppCompatActivity {

    public static String itemId, orderId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_pending:
                    selectedFragment = new PendingOrderFragment();
                    break;
                case R.id.navigation_accepted:
                    selectedFragment = new AcceptedOrderFragment();
                    break;
                case R.id.navigation_rejected:
                    selectedFragment = new RejectedOrderFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_response);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment selectedFragment = null;
        selectedFragment = new PendingOrderFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

    }
}
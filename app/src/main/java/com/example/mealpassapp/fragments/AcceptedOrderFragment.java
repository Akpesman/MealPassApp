package com.example.mealpassapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mealpassapp.R;
import com.example.mealpassapp.activities.UserActivity;
import com.example.mealpassapp.adapters.ViewOrderResponseListAdapter;
import com.example.mealpassapp.model.OrderStatusModelClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AcceptedOrderFragment extends Fragment {
    Context context;
    ListView listView;
    TextView textView;
    DatabaseReference databaseReference;
    List<OrderStatusModelClass> list;
    String userId;

    public AcceptedOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_pending_order, container, false);
        context = container.getContext();

        showProgressDialog();

        listView = view.findViewById(R.id.listItem);
        textView = view.findViewById(R.id.noStatus);

        databaseReference = FirebaseDatabase.getInstance().getReference("FoodOrderStatus/");
        list = new ArrayList<>();
        userId = UserActivity.userId;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final OrderStatusModelClass status = list.get(position);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final String status = "Accepted";
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                textView.setText("");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OrderStatusModelClass modelClass = snapshot.getValue(OrderStatusModelClass.class);
                    if(userId.equals(modelClass.getCustId()) && status.equals(modelClass.getStatus())){
                        list.add(modelClass);
                    }
                }
                if(list.size()>0){
                    ViewOrderResponseListAdapter adapter = new ViewOrderResponseListAdapter(context, list);
                    listView.setAdapter(adapter);
                }else {
                    textView.setText("No Response!!");
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}

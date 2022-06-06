package com.example.mealpassapp.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mealpassapp.activities.CheckDeliveryPointsActivity;
import com.example.mealpassapp.activities.DeliveryPointsMapsActivity;
import com.example.mealpassapp.activities.FullScreenImageActivity;
import com.example.mealpassapp.R;
import com.example.mealpassapp.activities.ViewWeatherActivity;
import com.example.mealpassapp.model.FoodOrderModel;
import com.example.mealpassapp.model.UsersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CheckAccpetedOrdersListAdapter extends ArrayAdapter<FoodOrderModel> implements Filterable {
    private Activity context;
    private List<FoodOrderModel> ordersList, searchList;

    public CheckAccpetedOrdersListAdapter(Activity context , List<FoodOrderModel> ordersList){
        super(context , R.layout.accepted_order_list , ordersList);
        this.context = context;
        this.ordersList = ordersList;

        searchList = new ArrayList<>();
        searchList.addAll(ordersList);
    }
    @Override
    public View getView(int position, View convertView , ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        final View listViewItem = inflater.inflate(R.layout.accepted_order_list , null , true);

        TextView customerName = (TextView) listViewItem.findViewById(R.id.tvCustomerName);
        TextView itemname = (TextView) listViewItem.findViewById(R.id.tvitem);
        TextView textViewquantity = (TextView) listViewItem.findViewById(R.id.tv3);
        TextView price = (TextView) listViewItem.findViewById(R.id.tvprice);
        TextView dateTime = (TextView) listViewItem.findViewById(R.id.datetime);
        TextView status = (TextView) listViewItem.findViewById(R.id.status);
        TextView tvWeather = (TextView) listViewItem.findViewById(R.id.tvWeather);
        TextView tvDelivery = (TextView) listViewItem.findViewById(R.id.tvDelivery);
        ImageView profilePic = (ImageView) listViewItem.findViewById(R.id.customerPic);
        ImageView imgWeather = (ImageView) listViewItem.findViewById(R.id.imgWeather);
        ImageView imgDelivery = (ImageView) listViewItem.findViewById(R.id.imgDelivery);

        tvWeather.setVisibility(View.VISIBLE);
        imgWeather.setVisibility(View.VISIBLE);

        tvDelivery.setVisibility(View.VISIBLE);
        imgDelivery.setVisibility(View.VISIBLE);

        final FoodOrderModel order = ordersList.get(position);

        if(order.getFlag().equals("Accepted2")){
            customerName.setText(order.getUserName());
            itemname.setText(order.getFoodName());
            textViewquantity.setText(order.getQuantity()+"");
            price.setText("Points Purchased");
            dateTime.setText(order.getDate());
            Picasso.with(context).load(order.getUserPic()).fit().centerCrop().placeholder(R.drawable.loading).into(profilePic);
            status.setText("Accepted");
        }else {
            customerName.setText(order.getUserName());
            itemname.setText(order.getFoodName());
            textViewquantity.setText(order.getQuantity()+"");
            price.setText("Rs "+order.getFoodPrice());
            dateTime.setText(order.getDate());
            status.setText("Accepted");
            Picasso.with(context).load(order.getUserPic()).fit().centerCrop().placeholder(R.drawable.loading).into(profilePic);
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , FullScreenImageActivity.class);
                intent.putExtra("URI" , order.getUserPic());
                context.startActivity(intent);
            }
        });
        tvWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocWeather(order.getUserId());
            }
        });
        imgWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocWeather(order.getUserId());
            }
        });

        tvDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custId = order.getUserId();
                getLocDelivery(custId);
            }
        });

        imgDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custId = order.getUserId();
                getLocDelivery(custId);
            }
        });

        return listViewItem;
    }

    private void getLocWeather(final String custId) {
        showProgressDialog();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users/");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UsersModel user = snapshot.getValue(UsersModel.class);
                    if(custId.equals(user.getId())){
                        CheckDeliveryPointsActivity.custLati = user.getLatitude();
                        CheckDeliveryPointsActivity.custLongi = user.getLongtitude();
                        hideProgressDialog();
                        Intent intent = new Intent(context, ViewWeatherActivity.class);
                        context.startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void getLocDelivery(final String custId) {
        showProgressDialog();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users/");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UsersModel user = snapshot.getValue(UsersModel.class);
                    if(custId.equals(user.getId())){
                        CheckDeliveryPointsActivity.custLati = user.getLatitude();
                        CheckDeliveryPointsActivity.custLongi = user.getLongtitude();
                        hideProgressDialog();
                        Intent intent = new Intent(context, DeliveryPointsMapsActivity.class);
                        context.startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public Filter getFilter() {
        return Dataresult;
    }
    private Filter Dataresult = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FoodOrderModel> FilterList = new ArrayList<>();
            if(constraint == null && constraint.length()==0){
                FilterList.addAll(searchList);
            }else {
                String characters = constraint.toString().toLowerCase().trim();
                for(FoodOrderModel order : searchList){
                    if(order.getFoodName().toLowerCase().contains(characters)){
                        FilterList.add(order);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = FilterList;
            return  filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ordersList.clear();
            ordersList.addAll((Collection<? extends FoodOrderModel>) results.values);
            notifyDataSetChanged();
        }
    };

    private ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading location coordinates..");
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
}

package com.example.mealpassapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealpassapp.R;
import com.example.mealpassapp.model.FoodOrderModel;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ViewPendingOrderListAdapter extends ArrayAdapter<FoodOrderModel> {
    private Context context;
    private List<FoodOrderModel> statusList;

    public ViewPendingOrderListAdapter(Context context, List<FoodOrderModel> list) {

//        super(context, R.layout.order_response_list, list);
        super(context , android.R.layout.simple_list_item_1 , list);
        this.context = context;
        this.statusList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View status_list = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        status_list = inflater.inflate(R.layout.order_response_list,null);

        ImageView picture = (ImageView) status_list.findViewById(R.id.orderDealerPic);
        ImageView rating = (ImageView) status_list.findViewById(R.id.rate_items);
        TextView status = (TextView) status_list.findViewById(R.id.tvstatus);
        TextView tvItem = (TextView) status_list.findViewById(R.id.tvItem);
        TextView dealerbusiness = (TextView) status_list.findViewById(R.id.tvdealer);
        TextView total = (TextView) status_list.findViewById(R.id.tvtotal);
        TextView tvprice = (TextView) status_list.findViewById(R.id.textviewPrice);
        TextView tvquan = (TextView) status_list.findViewById(R.id.textVquan);
        TextView date = (TextView) status_list.findViewById(R.id.statDate);

        int price = 0 , quantity= 0;

        final FoodOrderModel selectedItemStatus = statusList.get(position);

        String uri = selectedItemStatus.getFoodPic();

        price = selectedItemStatus.getFoodPrice();
        quantity = selectedItemStatus.getQuantity();
        int totalbill = price * quantity;
        float dis = selectedItemStatus.getDiscount()/100*totalbill;
        float tot = totalbill - dis;
        String t = String.valueOf(tot);
        total.setText(t+" pound");

        tvprice.setText(selectedItemStatus.getFoodPrice()+" pound");
        tvquan.setText(selectedItemStatus.getQuantity()+"");
        status.setText(selectedItemStatus.getFlag());
        tvItem.setText(selectedItemStatus.getFoodName());
        dealerbusiness.setText(selectedItemStatus.getSellerName());
        date.setText(selectedItemStatus.getDate());
        Picasso.with(context).load(uri).placeholder(R.drawable.loading).into(picture);

        rating.setVisibility(View.GONE);
        return status_list;
    }
}

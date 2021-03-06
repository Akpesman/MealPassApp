package com.example.mealpassapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mealpassapp.R;
import com.example.mealpassapp.activities.UserActivity;
import com.example.mealpassapp.model.FoodModel;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private List<FoodModel> urlList;

    public SliderAdapter(Context context, List<FoodModel> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        final FoodModel item = urlList.get(position);
        Picasso.with(context).load(item.getFoodPic()).into(imageView);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}

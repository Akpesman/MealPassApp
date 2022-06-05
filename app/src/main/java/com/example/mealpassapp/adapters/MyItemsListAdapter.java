package com.example.mealpassapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealpassapp.R;
import com.example.mealpassapp.model.FoodModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyItemsListAdapter extends RecyclerView.Adapter<MyItemsListAdapter.ImageViewHolder> implements Filterable {
    private Context mcontext ;
    private List<FoodModel> muploadList;
    private List<FoodModel> muploadListsearch;
    public static String itemId = "";
    int position;

    public MyItemsListAdapter(Context context , List<FoodModel> uploadList ) {
        mcontext = context ;
        muploadList = uploadList ;

        muploadListsearch = new ArrayList<>();
        muploadListsearch.addAll(muploadList);

        position = 0;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.my_items_list, parent , false);
        return (new ImageViewHolder(v));
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

     this.position = position;

     final FoodModel item = muploadList.get(position);

     holder.textViewItemName.setText(item.getFoodTitle());
     holder.textViewItemPrice.setText("Rs."+item.getPrice());
     Picasso.with(mcontext).load(item.getFoodPic()).placeholder(R.drawable.loading).into(holder.itemPic);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                builder.setTitle("Confimation");
                builder.setMessage("Are you sure to delete this item?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = item.getId();
                        DatabaseReference dbRef =  FirebaseDatabase.getInstance().getReference("FoodItem/").child(id);
                        dbRef.removeValue();
                        Toast.makeText(mcontext, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return muploadList.size();
    }

    public static  class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewItemName;
        public TextView textViewItemPrice;
        public ImageView itemPic;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tvItemName2);
            textViewItemPrice = itemView.findViewById(R.id.tvItemPrice2);
            itemPic = itemView.findViewById(R.id.ItemPic);
        }
    }

    @Override
    public Filter getFilter() {
        return Dataresult;
    }
    private Filter Dataresult = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<FoodModel> FilterList = new ArrayList<>();
           if(constraint == null && constraint.length()==0){
               FilterList.addAll(muploadListsearch);
           }else {
               String characters = constraint.toString().toLowerCase().trim();
               for(FoodModel upload : muploadListsearch){
                   if(upload.getFoodTitle().toLowerCase().contains(characters)){
                       FilterList.add(upload);
                   }
               }
           }
           FilterResults filterResults = new FilterResults();
           filterResults.values = FilterList;
           return  filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            muploadList.clear();
            muploadList.addAll((Collection<? extends FoodModel>) results.values);
            notifyDataSetChanged();
        }
    };

}

package com.example.boone.app3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by User on 1/1/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Retail_Items> mRetail_Items = new ArrayList<>();

    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Retail_Items> retail_Items ) {
        mRetail_Items = retail_Items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView imageName,source,price;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            /*rice = itemView.findViewById(R.id.item_price);

            image = itemView.findViewById(R.id.item_image);
            imageName = itemView.findViewById(R.id.item_name);*/
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Retail_Items retail_items = mRetail_Items.get(position);

        if(! retail_items.imageUrl.equals("")){
            Glide.with(mContext)
                    .asBitmap()
                    .load(retail_items.imageUrl)
                    .into(holder.image);
        }

        String name = retail_items.title + retail_items.condition;
        holder.imageName.setText(name);

        String price = "$"+retail_items.value + retail_items.currency;
        holder.price.setText(price);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(retail_items.itemWebUrl));
                view.getContext().startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mRetail_Items.size();
    }



}
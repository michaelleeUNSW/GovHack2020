package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

/**
 * Created by Mike Lee on 9/07/2017.
 */

public class BookAdapter extends ArrayAdapter<Hit> {
    public BookAdapter(Context context, ArrayList<Hit> list) {
        super(context, 0, list);
    }

    public void setBooks(ArrayList<Hit> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        Hit hit = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.Title);
        title.setText(hit.getTitle());

        return listItemView;
    }
}

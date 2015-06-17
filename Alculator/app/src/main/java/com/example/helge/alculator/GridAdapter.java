package com.example.helge.alculator;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.example.helge.alculator.R;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<Integer> list = new ArrayList<>();

    public GridAdapter(Context context) {
        mContext = context;
        list.add(R.drawable.drink_beer_icon);
        list.add(R.drawable.drink_shot_icon);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

            int size = Math.round(mContext.getResources().getDimension(R.dimen.gridview_columnwidth));

            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(list.get(position));
        return imageView;
    }
}

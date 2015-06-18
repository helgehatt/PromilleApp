package com.example.helge.alculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        return list.size()+1;
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
        RelativeLayout view;
        ImageView image;
        TextView name;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (RelativeLayout) inflater.inflate(R.layout.grid_item, null, false);
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);

            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setPadding(8, 8, 8, 8);

            int size = Math.round(mContext.getResources().getDimension(R.dimen.gridview_imagesize));

            view.setLayoutParams(new GridView.LayoutParams(size, size));
        } else {
            view = (RelativeLayout) convertView;
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
        }

        if (position == list.size()){
            name.setVisibility(View.GONE);
            view.findViewById(R.id.gridtext_background).setVisibility(View.GONE);
            image.setImageResource(R.drawable.alculator_newicon);
        } else {
            name.setVisibility(View.VISIBLE);
            view.findViewById(R.id.gridtext_background).setVisibility(View.VISIBLE);
            name.setText("MooMoo");
            image.setImageResource(list.get(position));
        }

        return view;
    }
}

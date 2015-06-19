package com.example.helge.alculator;

import android.content.Context;
import android.graphics.Color;
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
    private ArrayList<Drink> list = new ArrayList<>();
    private int selectedItem;

    public GridAdapter(Context context) {
        mContext = context;
        //Below items entered for testing
        list.add(new Drink("A beer", 5.8, 50, R.drawable.drink_beer_icon));
        list.add(new Drink("A shot", 30, 10, R.drawable.drink_shot_icon));
    }

    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Drink getItem(int position) {
        if (position == list.size()) return null;
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout view = (RelativeLayout) convertView;
        ImageView image;
        TextView name;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (RelativeLayout) inflater.inflate(R.layout.grid_item, null, false);
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);

            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            int imageSize = Math.round(mContext.getResources().getDimension(R.dimen.gridview_imagesize));

            view.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
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
            Drink drink = list.get(position);
            name.setVisibility(View.VISIBLE);
            view.findViewById(R.id.gridtext_background).setVisibility(View.VISIBLE);
            name.setText(drink.getName());
            image.setImageResource(drink.getImageID());
        }

        return view;
    }

    public boolean setSelected(int position) {
        if (position == list.size()) return true;
        if (0 <= position && position < list.size()){
            list.get(selectedItem).setSelected(false);
            list.get(position).setSelected(true);
        }
        return false;
    }

    public Drink getSelectedItem() {
        if (0 <= selectedItem && selectedItem < list.size()){
            return list.get(selectedItem);
        }
        return null;
    }
}

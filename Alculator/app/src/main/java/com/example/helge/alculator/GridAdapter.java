package com.example.helge.alculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private final String TAG = "Alculator";

    private final Context mContext;
    private ArrayList<Drink> list = new ArrayList<>();
    private int selectedItem;

    public GridAdapter(Context context) {
        mContext = context;
        //Below items entered for testing
        list.add(new Drink("A beer", 5.8, 50, BitmapFactory.decodeResource(mContext.getApplicationContext().getResources(), R.drawable.drink_beer_icon)));
        list.add(new Drink("A shot", 30, 10, BitmapFactory.decodeResource(mContext.getApplicationContext().getResources(), R.drawable.drink_shot_icon)));
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
        TextView counter;
        ImageView textbackground;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (RelativeLayout) inflater.inflate(R.layout.grid_item, null, false);
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            counter = (TextView) view.findViewById(R.id.counter);
            textbackground = (ImageView) view.findViewById(R.id.gridtext_background);

            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            int imageSize = Math.round(mContext.getResources().getDimension(R.dimen.gridview_imagesize));

            view.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
        } else {
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            counter = (TextView) view.findViewById(R.id.counter);
            textbackground = (ImageView) view.findViewById(R.id.gridtext_background);
        }

        if (position == list.size()){
            name.setVisibility(View.GONE);
            textbackground.setVisibility(View.GONE);
            counter.setVisibility(View.GONE);
            image.setImageResource(R.drawable.alculator_newicon);
        } else {
            Drink drink = list.get(position);
            name.setVisibility(View.VISIBLE);
            textbackground.setVisibility(View.VISIBLE);
            counter.setVisibility(View.VISIBLE);
            name.setText(drink.getName());

            int quantity = drink.getQuantity();
            if (0 < quantity)
                counter.setText(""+quantity);
            else
                counter.setText("");

            image.setImageBitmap(drink.getImage());
        }

        Log.i(TAG, "Draw view in grid.");

        return view;
    }

    public boolean setSelected(int position) {
        if (position == list.size()) return false;
        if (0 <= position && position < list.size()){
            list.get(selectedItem).setSelected(false);
            list.get(position).setSelected(true);
            selectedItem = position;
        }
        return true;
    }

    public Drink getSelectedItem() {
        if (0 <= selectedItem && selectedItem < list.size()){
            return list.get(selectedItem);
        }
        return null;
    }

    public void add(Drink newDrink){
        list.add(newDrink);
    }
}

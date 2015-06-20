package com.example.helge.alculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
    public static final String TAG = "Alculator";

    private final Context mContext;
    private ArrayList<Drink> list = new ArrayList<Drink>();
    private int selectedItem;
    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public GridAdapter(Context context) {
        mContext = context;

        mDbHelper = new DrinksDbHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();

        clearDatabase(); // TODO skal fjernes, når vi er lidt længere
        addToDatabase(new Drink("Beer", 4.5, 33, BitmapFactory.decodeResource(mContext.getApplicationContext().getResources(), R.drawable.drink_beer_icon)));
        addToDatabase(new Drink("Light beer", 2.1, 33, BitmapFactory.decodeResource(mContext.getApplicationContext().getResources(), R.drawable.drink_beer_icon)));
        addToDatabase(new Drink("Shot", 32, 4, BitmapFactory.decodeResource(mContext.getApplicationContext().getResources(), R.drawable.drink_shot_icon)));

        Cursor cursor = readDrinks();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Drink(cursor.getString(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_PERCENTAGE)),
                    cursor.getInt(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_VOLUME)),
                    cursor.getInt(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_CALORIES)),
                    BitmapFactory.decodeResource(mContext.getApplicationContext().getResources(), R.drawable.drink_beer_icon))); // TODO get image resource from database
            cursor.moveToNext();
        }
    }

    public void addToDatabase(Drink drink) {
        ContentValues values = new ContentValues();

        values.put(DrinksContract.DrinkEntry.COLUMN_NAME, drink.getName());
        values.put(DrinksContract.DrinkEntry.COLUMN_PERCENTAGE, drink.getAlcoholPercent());
        values.put(DrinksContract.DrinkEntry.COLUMN_VOLUME, drink.getVolume());
        values.put(DrinksContract.DrinkEntry.COLUMN_CALORIES, drink.getCalories());
        values.put(DrinksContract.DrinkEntry.COLUMN_IMAGE, ""); //TODO få gemt billeder til databasen
        mDatabase.insert(DrinksContract.DrinkEntry.TABLE_NAME, null, values);
        values.clear();
    }

    public void add(Drink newDrink) {
        list.add(newDrink);
    }

    private Cursor readDrinks() {
        String[] columns = {
                DrinksContract.DrinkEntry.COLUMN_NAME,
                DrinksContract.DrinkEntry.COLUMN_PERCENTAGE,
                DrinksContract.DrinkEntry.COLUMN_VOLUME,
                DrinksContract.DrinkEntry.COLUMN_CALORIES,
                DrinksContract.DrinkEntry.COLUMN_IMAGE
        };

        return mDatabase.query(DrinksContract.DrinkEntry.TABLE_NAME,
                columns, null, new String[] {}, null, null, null);
    }

    private void clearDatabase() {
        mDbHelper.getWritableDatabase().delete(DrinksContract.DrinkEntry.TABLE_NAME, null, null);
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
                counter.setText("" + quantity);
            else
                counter.setText("");

            image.setImageBitmap(drink.getImage());
        }
        
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
}

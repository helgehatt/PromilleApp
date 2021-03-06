package com.example.helge.alculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class GridAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<Drink> list = new ArrayList<>();
    private Drink selectedItem;
    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public GridAdapter(Context context) {
        mContext = context;

        mDbHelper = new DrinksDbHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();

        Cursor cursor = readDrinks();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String filePath = cursor.getString(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_IMAGE));
            Bitmap image;
            if (filePath != null)
                image = Bitmap.createScaledBitmap(getBitmapFromFilePath(filePath), 200, 200, false);
            else
                image = ((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.drink_empty)).getBitmap();

            list.add(new Drink(cursor.getString(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_PERCENTAGE)),
                    cursor.getDouble(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_VOLUME)),
                    cursor.getDouble(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_CALORIES)),
                    image,
                    cursor.getLong(cursor.getColumnIndex(DrinksContract.DrinkEntry.COLUMN_LAST_USE)) ));
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void addToDatabase(String name, double percentage, double volume, double calories, String imagePath, long lastUse) {
        ContentValues values = new ContentValues();

        values.put(DrinksContract.DrinkEntry.COLUMN_NAME, name);
        values.put(DrinksContract.DrinkEntry.COLUMN_PERCENTAGE, percentage);
        values.put(DrinksContract.DrinkEntry.COLUMN_VOLUME, volume);
        values.put(DrinksContract.DrinkEntry.COLUMN_CALORIES, calories);
        values.put(DrinksContract.DrinkEntry.COLUMN_IMAGE, imagePath);
        values.put(DrinksContract.DrinkEntry.COLUMN_LAST_USE, lastUse);
        mDatabase.insert(DrinksContract.DrinkEntry.TABLE_NAME, null, values);
        values.clear();
    }

    public void setLastUseAndSort(Drink drink, long lastUse) {
        ContentValues values = new ContentValues();
        values.put(DrinksContract.DrinkEntry.COLUMN_LAST_USE, lastUse);
        mDatabase.update(DrinksContract.DrinkEntry.TABLE_NAME, values, DrinksContract.DrinkEntry.COLUMN_NAME + "='" + drink.getName() + "'", null);
        values.clear();

        drink.setLastUse(lastUse);

        sort();
    }

    public static Bitmap getBitmapFromFilePath(String filePath) {
        File image = new File(filePath);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

        return bitmap;
    }

    public void add(Drink newDrink) {
        list.add(newDrink);
        sort();
    }

    protected void sort(){
        Collections.sort(list);
    }

    private Cursor readDrinks() {
        String[] columns = {
                DrinksContract.DrinkEntry.COLUMN_NAME,
                DrinksContract.DrinkEntry.COLUMN_PERCENTAGE,
                DrinksContract.DrinkEntry.COLUMN_VOLUME,
                DrinksContract.DrinkEntry.COLUMN_CALORIES,
                DrinksContract.DrinkEntry.COLUMN_IMAGE,
                DrinksContract.DrinkEntry.COLUMN_LAST_USE
        };

        return mDatabase.query(DrinksContract.DrinkEntry.TABLE_NAME,
                columns, null, new String[] {}, null, null, DrinksContract.DrinkEntry.COLUMN_LAST_USE + " DESC");
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
        ImageView textBackground;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (RelativeLayout) inflater.inflate(R.layout.grid_item, null, false);
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            counter = (TextView) view.findViewById(R.id.counter);
            textBackground = (ImageView) view.findViewById(R.id.gridtext_background);

            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            int imageSize = Math.round(mContext.getResources().getDimension(R.dimen.gridview_imagesize));

            view.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
        } else {
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            counter = (TextView) view.findViewById(R.id.counter);
            textBackground = (ImageView) view.findViewById(R.id.gridtext_background);
        }
        
        if (position == list.size()){
            name.setVisibility(View.GONE);
            textBackground.setVisibility(View.GONE);
            counter.setVisibility(View.GONE);
            image.setImageResource(R.drawable.alculator_newicon);
        } else {
            Drink drink = list.get(position);
            name.setVisibility(View.VISIBLE);
            textBackground.setVisibility(View.VISIBLE);
            counter.setVisibility(View.VISIBLE);
            name.setText(drink.getName());

            int quantity = drink.getQuantity();
            if (0 < quantity)
                counter.setText("" + quantity);
            else
                counter.setText("");

            if (drink.isSelected())
                view.setBackgroundResource(R.drawable.btn_blue_matte);
            else
                view.setBackgroundResource(R.drawable.btn_black);

            image.setImageBitmap(drink.getImage());
        }
        
        return view;
    }

    public boolean setSelected(int position) {
        if (position == list.size()) return false;
        if (0 <= position && position < list.size()){
            if (selectedItem != null) selectedItem.setSelected(false);
            selectedItem = list.get(position);
            selectedItem.setSelected(true);
        }
        return true;
    }

    public Drink getSelectedItem() {
        return selectedItem;
    }

    public void removeSelectedItem() {
        list.remove(selectedItem);
        mDatabase.delete(DrinksContract.DrinkEntry.TABLE_NAME,
                DrinksContract.DrinkEntry.COLUMN_NAME + "='" + selectedItem.getName() + "'",
                null);
    }
}

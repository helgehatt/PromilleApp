package com.example.helge.alculator;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Drink {
    //Fields
    private Bitmap image;
    private String name;
    private double alcoholPercent;
    private double volume;
    private double calories;
    private int quantity = 0;
    private boolean selected = false;

    //Constructor
    public Drink(String name, double percent, double volume, Bitmap image){
        this.name = name;
        this.alcoholPercent = percent;
        this.volume = volume;
        this.image = image;
    }

    public Drink(String name, double percent, double volume, double calories, Bitmap image){
        this(name, percent, volume, image);
        this.calories = calories;
    }

    //Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAlcoholPercent() {
        return alcoholPercent;
    }

    public void setAlcoholPercent(double alcoholPercent) {
        this.alcoholPercent = alcoholPercent;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incQuantity(){
        this.quantity++;
    }

    public void decQuantity(){
        this.quantity--;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}

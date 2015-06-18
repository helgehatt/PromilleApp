package com.example.helge.alculator;

public class Drink {
    //Fields
    private int imageID;
    private String name;
    private double alcoholPercent;
    private double volume;
    private double calories;
    private int quantity = 0;
    private boolean selected = false;

    //Constructor
    public Drink(String name, double percent, double volume, int imageID){
        this.name = name;
        this.alcoholPercent = percent;
        this.volume = volume;
        this.imageID = imageID;
    }

    public Drink(String name, double percent, double volume, double calories, int imageID){
        this(name, percent, volume, imageID);
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
        if (0 < quantity) this.quantity--;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
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

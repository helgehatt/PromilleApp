package com.example.helge.alculator;

public class Drink {
    //Fields
    private int imageID;
    private String name;
    private double alcoholPercent;
    private double volume;
    private double calories;

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
}

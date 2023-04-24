package gis.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/* This class should represent every location needed in the dataset. This could be
   expanded upon by creating another class that has a HashMap in the form of
   HashMap<[X,Y], Location>, meaning that given an X and Y value to the HashMap,
   it would return the appropriate object of this class

   The general process would be:

   1. Create Location object based off unique x and y from text file using Location() constructor
   2. Add any given dates and pollutions through setDay() method
   3. Add this Location object to the more general HashMap mentioned above by using an array of [X,Y] as the key
      and the Location object as a value.

   Any results of calculations could be added as a dates/pollution pair in dayValueDictionary to this object,
   and the output txt file could simply go through each location that matches the input locations and print all days
   found.
*/
public class Location {
    private double x;

    private double y;
    private int ID;
    private HashMap<Integer, Double> dayValueDictionary;

    private HashSet<Integer> originalDays;

    public Location(double x, double y, int ID) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        dayValueDictionary = new HashMap<>();
        originalDays = new HashSet<>();
    }

    public void setDay(int day, double value) {
        dayValueDictionary.put(day, value);
    }

    public double getDay(int day) {
        return dayValueDictionary.get(day);
    }

    // return the entire hashmap
    public HashMap<Integer, Double> getDayValueDictionary() {
        return dayValueDictionary;
    }

    // Only used for weird cross validation shenanigans
    public void setDayValueDictionary(HashMap<Integer, Double> dayValueDictionary) {
        this.dayValueDictionary = dayValueDictionary;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String showData() {

        //visually show object
        String str = "ID = " + ID + " x = " + x + "  " + " y = " + y + " " + " \n" + "Day/Measurement = " + Arrays.asList(dayValueDictionary) + " \n"; // method 1
        return str;
        //System.out.println();
    }

    public String toString() {
        return "-----------\nID: " + getID() + "\nX: " + getX() + "\nY: " + getY() + "\nDay Value Dict: " + getDayValueDictionary() + "\n";
    }

    public void addSetDay(int day) {
        originalDays.add(day);
    }

    public boolean containsSetDay(int day) {
        return originalDays.contains(day);
    }

}
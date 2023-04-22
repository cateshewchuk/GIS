package gis.Algorithm;

import gis.app.MainController;
import gis.model.*;
import java.util.*;
import gis.triangulation.*;

public class ConvertData {
    // Converts the given data pm25 values from null to -1 for easier use in algorithms
    public static void nullToValue(HashMap<String, Location> data) {
        List<Vector2D> coordinates = coordinatesToVector(data);
        for(Vector2D vector: coordinates) {
            String skey = vector.x + ", " + vector.y;

            switch(MainController.selected.toString()) {
                case "Day":
                    for (int i = 0; i <= 365; i++)
                        if (data.get(skey).getDayValueDictionary().get(i) == null)
                            data.get(skey).setDay(i, -1);
                    break;
                case "Month":
                    for (int i = 0; i <= 11; i++)
                        if (data.get(skey).getDayValueDictionary().get(i) == null)
                            data.get(skey).setDay(i, -1);
                    break;
                case "Year":
                    if (data.get(skey).getDayValueDictionary().get(0) == null)
                        data.get(skey).setDay(0, -1);
            }
        }
    }

    public static void NaNToValue(HashMap<String, Location> data) {
        List<Vector2D> coordinates = coordinatesToVector(data);
        for(Vector2D vector: coordinates) {
            String skey = vector.x + ", " + vector.y;

            switch(MainController.selected.toString()) {
                case "Day":
                    for (int i = 0; i <= 365; i++)
                        if (Double.isNaN((double)data.get(skey).getDayValueDictionary().get(i)))
                            data.get(skey).setDay(i, -1);
                    break;
                case "Month":
                    for (int i = 0; i <= 11; i++)
                        if (Double.isNaN((double)data.get(skey).getDayValueDictionary().get(i)))
                            data.get(skey).setDay(i, -1);
                    break;
                case "Year":
                    if (Double.isNaN((double)data.get(skey).getDayValueDictionary().get(0)))
                        data.get(skey).setDay(0, -1);
            }
        }
    }

    public static void valueToNaN(HashMap<String, Location> data) {
        List<Vector2D> coordinates = coordinatesToVector(data);
        for(Vector2D vector: coordinates) {
            String skey = vector.x + ", " + vector.y;

            switch(MainController.selected.toString()) {
                case "Day":
                    for (int i = 0; i <= 365; i++)
                        if (data.get(skey).getDayValueDictionary().get(i) == -1)
                            data.get(skey).setDay(i, Double.NaN);
                    break;
                case "Month":
                    for (int i = 0; i <= 11; i++)
                        if (data.get(skey).getDayValueDictionary().get(i) == -1)
                            data.get(skey).setDay(i, Double.NaN);
                    break;
                case "Year":
                    if (data.get(skey).getDayValueDictionary().get(0) == -1)
                        data.get(skey).setDay(0, Double.NaN);
            }
        }
    }

    // Converts given (x, y) coordinates into the Vector2D format
    public static List<Vector2D> coordinatesToVector(HashMap<String, Location> data) {
        List<Vector2D> knownCoordinates = new ArrayList<>();

        for(Map.Entry<String, Location> entry: data.entrySet()) {
            Vector2D vector = new Vector2D(entry.getValue().getX(), entry.getValue().getY());
            knownCoordinates.add(vector);
        }

        return knownCoordinates;
    }

    // Method to handle single location and vector
    public static Vector2D coordinatesToVector(Location loc) {
        HashMap<String, Location> locHashMap = new HashMap<>();
        locHashMap.put(loc.getX() + ", " + loc.getY(), loc);
        // Really hacky
        return coordinatesToVector(locHashMap).toArray(new Vector2D[1])[0];
    }

}

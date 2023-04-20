package gis.Algorithm;

import gis.model.*;
import java.util.*;
import gis.triangulation.*;

public class ConvertData {
    // Converts the given data pm25 values from null to -1 for easier use in algorithms
    public static void nullToValue(HashMap<String, Location> data) {
        List<Vector2D> coordinates = coordinatesToVector(data);
        for(Vector2D vector: coordinates) {
            String skey = vector.x + ", " + vector.y;

            for (int i = 0; i <= 365; i++)
                if (data.get(skey).getDayValueDictionary().get(i) == null)
                    data.get(skey).setDay(i, -1);
        }
    }

    // Converts given (x, y) coordinates into the Vector2D format
    public static List<Vector2D> coordinatesToVector(HashMap<String, Location> data) {
        List<Vector2D> knownCoordinates = new ArrayList<Vector2D>();

        for(Map.Entry<String, Location> entry: data.entrySet()) {
            Vector2D vector = new Vector2D(entry.getValue().getX(), entry.getValue().getY());
            knownCoordinates.add(vector);
        }

        return knownCoordinates;
    }

}

package gis.Algorithm;

import gis.app.MainController;
import gis.model.Location;
import gis.triangulation.Vector2D;

import java.util.HashMap;
import java.util.List;

public class TimeInterpolation {
    public static void timeInterpolate(HashMap<String, Location> knownData) {
        // Variables used for time shape function shown in comments above
        double wi;
        double wi1 = -1;
        double wi2 = -1;
        double t1 = -1;
        double t2 = -1;

        List<Vector2D> coordinates = ConvertData.coordinatesToVector(knownData);
        ConvertData.nullToValue(knownData);

        for (Vector2D point : coordinates) {
            // To be in the format for the HashMaps being used
            String pointKey = point.x + ", " + point.y;

            /*
            This will loop through January 2nd - December 30th for each (x, y) coordinate
            in the unknown data. If the pm value is unknown, it will search for the closest
            location before and after that does have a value in order to find t1, t2, wi1, and wi2
             */
            if(MainController.selected == "Day") {
                for (int j = 1; j <= 364; j++) {
                    if (knownData.get(pointKey).getDayValueDictionary().get(j) == -1) {
                        for (int k = j - 1; k >= 0; k--) {
                            if (knownData.get(pointKey).getDayValueDictionary().get(k) != -1) {
                                t1 = k;
                                wi1 = knownData.get(pointKey).getDayValueDictionary().get(k);
                                break;
                            }
                            else{
                                t1 = -1;
                                wi1 = -1;
                            }
                        }
                        for (int r = j + 1; r <= 365; r++) {
                            if (knownData.get(pointKey).getDayValueDictionary().get(r) != -1) {
                                wi2 = knownData.get(pointKey).getDayValueDictionary().get(r);
                                t2 = r;
                                break;
                            }
                            else{
                                t2 = -1;
                                wi2 = -1;
                            }
                        }

                        if (wi1 != -1 && wi2 != -1) {
                            wi = ((t2 - j) / (t2 - t1)) * wi1 + ((j - t1) / (t2 - t1)) * wi2;
                            knownData.get(pointKey).setDay(j, wi);
                        }
                    }
                }
            }

            /*
            This will loop through February - November for each (x, y) coordinate
            in the unknown data. If the pm value is unknown, it will search for the closest
            location before and after that does have a value in order to find t1, t2, wi1, and wi2
             */
            else if(MainController.selected == "Month") {
                for (int j =1; j <= 10; j++) {

                    if (knownData.get(pointKey).getDayValueDictionary().get(j) == -1) {
                        for (int k = j - 1; k >= 0; k--) {
                            if (knownData.get(pointKey).getDayValueDictionary().get(k) != -1) {
                                t1 = k;
                                wi1 = knownData.get(pointKey).getDayValueDictionary().get(k);
                                break;
                            }
                            else{
                                t1 = -1;
                                wi1 = -1;
                            }
                        }
                        for (int r = j + 1; r <= 11; r++) {
                            if (knownData.get(pointKey).getDayValueDictionary().get(r) != -1) {
                                wi2 = knownData.get(pointKey).getDayValueDictionary().get(r);
                                t2 = r;
                                break;
                            }
                            else{
                                t2 = -1;
                                wi2 = -1;
                            }
                        }

                        if(wi1 != -1 && wi2 != -1) {
                            wi = ((t2 - j) / (t2 - t1)) * wi1 + ((j - t1) / (t2 - t1)) * wi2;
                            knownData.get(pointKey).setDay(j, wi);
                        }
                    }
                }
            }

            // If input is "Year", this will just change any unknown values to NaN to match other data
            else {
                if(knownData.get(pointKey).getDayValueDictionary().get(0) == -1) {
                    knownData.get(pointKey).setDay(0, 2.0 % 0);
                }
            }
        }
    }
}

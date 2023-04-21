package gis.Algorithm;
import gis.app.Main;
import gis.app.MainController;
import gis.triangulation.*;
import gis.model.*;



import java.util.*;

/* If you know the pm25 values at a certain location at two separate times, you
    can use the following formula to approximate the pm25 value at an unknown time
    using the following 1D time shape function:
        wi(t) = ((t2 - t)/(t2 - t1))wi1 + ((t-t1)/(t2-t1))wi2

 */
public class TimeInterpolation {
    public static void timeInterpolate(HashMap<String, Location> unknownData) {
        // Variables used for time shape function shown in comments above
        double wi;
        double wi1 = -1;
        double wi2 = -1;
        double t1 = 1;
        double t2 = 1;

        List<Vector2D> coordinates = ConvertData.coordinatesToVector(unknownData);

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

                    if (unknownData.get(pointKey).getDayValueDictionary().get(j) == -1) {
                        for (int k = j - 1; k >= 0; k--) {
                            if (unknownData.get(pointKey).getDayValueDictionary().get(k) != -1) {
                                t1 = k;
                                wi1 = unknownData.get(pointKey).getDayValueDictionary().get(k);
                                break;
                            }
                        }
                        for (int r = j + 1; r <= 365; r++) {
                            if (unknownData.get(pointKey).getDayValueDictionary().get(r) != -1) {
                                wi2 = unknownData.get(pointKey).getDayValueDictionary().get(r);
                                t2 = r;
                                break;
                            }
                        }

                        if (wi1 != -1 && wi2 != -1) {
                            wi = ((t2 - j) / (t2 - t1)) * wi1 + ((j - t1) / (t2 - t1)) * wi2;
                            unknownData.get(pointKey).setDay(j, wi);
                        }
                    }
                }

                // This changes it to NaN to match the other data
                for(int i = 0; i <= 365; i++) {
                    if (unknownData.get(pointKey).getDayValueDictionary().get(0) == -1) {
                        unknownData.get(pointKey).setDay(i, 2.0 % 0);
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

                    if (unknownData.get(pointKey).getDayValueDictionary().get(j) == -1) {
                        for (int k = j - 1; k >= 0; k--) {
                            if (unknownData.get(pointKey).getDayValueDictionary().get(k) != -1) {
                                t1 = k;
                                wi1 = unknownData.get(pointKey).getDayValueDictionary().get(k);
                                break;
                            }
                        }
                        for (int r = j + 1; r <= 11; r++) {
                            if (unknownData.get(pointKey).getDayValueDictionary().get(r) != -1) {
                                wi2 = unknownData.get(pointKey).getDayValueDictionary().get(r);
                                t2 = r;
                                break;
                            }
                        }

                        if(wi1 != -1 && wi2 != -1) {
                            wi = ((t2 - j) / (t2 - t1)) * wi1 + ((j - t1) / (t2 - t1)) * wi2;
                            unknownData.get(pointKey).setDay(j, wi);
                        }
                    }
                }

                // This changes it to NaN to match the other data
                for(int i = 0; i <= 11; i++) {
                    if (unknownData.get(pointKey).getDayValueDictionary().get(i) == -1) {
                        unknownData.get(pointKey).setDay(i, 2.0 % 0);
                    }
                }
            }

            // If input is "Year", this will just change any unknown values to NaN to match other data
            else {
                if(unknownData.get(pointKey).getDayValueDictionary().get(0) == -1) {
                    unknownData.get(pointKey).setDay(0, 2.0 % 0);
                }
            }
        }
    }
}
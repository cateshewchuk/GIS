package gis.Algorithm;
import gis.triangulation.*;
import gis.model.*;



import java.util.*;

/* If you know the pm25 values at a certain location at two separate times, you
    can use the following formula to approximate the pm25 value at an unknown time
    using the following 1D time shape function:
        wi(t) = ((t2 - t)/(t2 - t1))wi1 + ((t-t1)/(t2-t1))wi2

 */
public class TimeInterpolation {
    public void timeInterpolate(HashMap<String, Location> unknownData) {
        double wi;
        double wi1 = 0;
        double wi2 = 0;
        double t;
        double t1 = 0;
        double t2 = 0;

        List<Vector2D> coordinates = ConvertData.coordinatesToVector(unknownData);

        for(int i = 0; i < coordinates.size(); i++) {
            Vector2D point = coordinates.get(i);
            String pointKey = point.x + ", " + point.y;

            for(int j = 2; j <= 364; j++) {
                t = j;
                if(unknownData.get(pointKey).getDayValueDictionary().get(j) != 0)
                    continue;
                else {
                    for(int k = j - 1; k > 0; k--) {
                        if(unknownData.get(pointKey).getDayValueDictionary().get(k) != 0) {
                            t1 = unknownData.get(pointKey).getDay(k);
                            wi1 = unknownData.get(pointKey).getDayValueDictionary().get(k);
                            break;
                        }
                        else
                            continue;
                    }
                    for(int r = j + 1; j <= 365; j++) {
                        if(unknownData.get(pointKey).getDayValueDictionary().get(r) != 0) {
                            wi2 = unknownData.get(pointKey).getDayValueDictionary().get(r);
                            t2 = unknownData.get(pointKey).getDay(r);
                            break;
                        }
                        else
                            continue;
                    }
                    wi =  ((t2 - t) / (t2 - t1)) * wi1 + ((t - t1) / (t2 - t1)) * wi2;
                    unknownData.get(pointKey).setDay(j, wi);
                }
            }


            /* Come back to this because I don't feel like this is the right move
            if(unknownData.get(pointKey).getDayValueDictionary().get(1) == 0)
                unknownData.get(pointKey).setDay(1, unknownData.get(pointKey).getDayValueDictionary().get(2));

            if(unknownData.get(pointKey).getDayValueDictionary().get(365) == 0)
                unknownData.get(pointKey).setDay(365, unknownData.get(pointKey).getDayValueDictionary().get(364));


             */
        }
    }
}

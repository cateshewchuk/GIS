package gis.Algorithm;
import gis.model.*;
import java.util.*;
import gis.triangulation.*;

import static gis.triangulation.Triangulation.closestTriangle;

/*  Computing the values of unknown data using triangulation and the
    SF-reduction formula

 */
public class SpatioInterpolation {
    public static void queryResults(HashMap<String, Location> knownData, HashMap<String, Location> unknownData) throws NotEnoughPointsException {
        List<Vector2D> knownCoordinates = ConvertData.coordinatesToVector(knownData);
        List<Vector2D> unknownCoordinates = ConvertData.coordinatesToVector(unknownData);

        ConvertData.nullToValue(knownData);
        ConvertData.nullToValue(unknownData);

        double N1, N2, N3, w1, w2, w3, w;

        for (Vector2D point : unknownCoordinates) {
            String pointKey = point.x + ", " + point.y;

            List<Triangle2D> tris = Triangulation.del(knownCoordinates, point);
            Triangle2D tri = closestTriangle(tris, point);


            if(tri != null) {
                String outerKey1 = tri.a.x + ", " + tri.a.y;
                String outerKey2 = tri.b.x + ", " + tri.b.y;
                String outerKey3 = tri.c.x + ", " + tri.c.y;

                N1 = Triangulation.calcN1(tri, point);
                N2 = Triangulation.calcN2(tri, point);
                N3 = Triangulation.calcN3(tri, point);

                for (int j = 0; j <= 365; j++) {
                    if(unknownData.get(pointKey).getDayValueDictionary().get(j) == -1) {
                        w1 = knownData.get(outerKey1).getDayValueDictionary().get(j);
                        w2 = knownData.get(outerKey2).getDayValueDictionary().get(j);
                        w3 = knownData.get(outerKey3).getDayValueDictionary().get(j);

                        if (w1 != -1 && w2 != -1 && w3 != -1) {
                            w = N1 * w1 + N2 * w2 + N3 * w3;
                            unknownData.get(pointKey).setDay(j, w);
                        }
                    }
                }
            }
        }
    }
}
package gis.Algorithm;
import gis.model.*;
import java.util.*;
import gis.triangulation.*;

/*  Computing the values of unknown data using triangulation and the
    SF-reduction formula

 */
public class SpatioInterpolation {
    public void queryValues(HashMap<String, Location> knownData, HashMap<String, Location> unknownData) throws NotEnoughPointsException {
        List<Vector2D> knownCoordinates = ConvertData.coordinatesToVector(knownData);
        List<Vector2D> unknownCoordinates = ConvertData.coordinatesToVector(unknownData);

        double N1, N2, N3, w1, w2, w3, w;

        for (Vector2D point : unknownCoordinates) {
            String pointKey = point.x + ", " + point.y;

            Triangle2D outer = Triangulation.del(knownCoordinates, point);

            String outerKey1 = outer.a.x + ", " + outer.a.y;
            String outerKey2 = outer.b.x + ", " + outer.b.y;
            String outerKey3 = outer.c.x + ", " + outer.c.y;

            N1 = Triangulation.calcN1(outer, point);
            N2 = Triangulation.calcN2(outer, point);
            N3 = Triangulation.calcN3(outer, point);

            for (int j = 1; j <= 365; j++) {
                w1 = knownData.get(outerKey1).getDayValueDictionary().get(j);
                w2 = knownData.get(outerKey2).getDayValueDictionary().get(j);
                w3 = knownData.get(outerKey3).getDayValueDictionary().get(j);

                if (w1 != 0 && w2 != 0 && w3 != 0) {
                    w = N1 * w1 + N2 * w2 + N3 * w3;
                    unknownData.get(pointKey).setDay(j, w);
                }
            }
        }
    }
}

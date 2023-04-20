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

        // variables needed for SF-reduction
        double N1, N2, N3, w1, w2, w3, w;

        for (Vector2D point : unknownCoordinates) {
            String pointKey = point.x + ", " + point.y;

            List<Triangle2D> tris = Triangulation.del(knownCoordinates, point);
            Triangle2D tri = closestTriangle(tris, point);
            //System.out.println(point.x + ", " + point.y + ": " + tri);

            if(tri != null) {
                // To be in the format for the HashMaps being used
                String outerKey1 = tri.a.x + ", " + tri.a.y;
                String outerKey2 = tri.b.x + ", " + tri.b.y;
                String outerKey3 = tri.c.x + ", " + tri.c.y;

                //Calculate the N1, N2, and N3 for each point using it's containing triangle
                N1 = Triangulation.calcN1(tri, point);
                N2 = Triangulation.calcN2(tri, point);
                N3 = Triangulation.calcN3(tri, point);

                /*
                Loops through each day of the year for every (x, y) coordinate in the output
                data and checks for if the vertices of the containing triangle has a given pm
                value for that date. If each vertex has a value for that date, you can use the
                formula to approximate the pm value for the (x, y) coordinate that we
                are searching for and set it for that date.

                ** ONLY WORKS IF WE ARE LOOKING AT JUST ONE YEAR'S WORTH OF DATA
                 */
                for (int i = 0; i <= 365; i++) {
                    if(unknownData.get(pointKey).getDayValueDictionary().get(i) == -1) {
                        w1 = knownData.get(outerKey1).getDayValueDictionary().get(i);
                        w2 = knownData.get(outerKey2).getDayValueDictionary().get(i);
                        w3 = knownData.get(outerKey3).getDayValueDictionary().get(i);

                        if (w1 != -1 && w2 != -1 && w3 != -1) {
                            w = N1 * w1 + N2 * w2 + N3 * w3;
                            unknownData.get(pointKey).setDay(i, w);
                        }
                    }
                }
                /* This could be a set up for if the user inputs by month instead of day
                ** STILL ONLY WORKS IF WE ARE LOOKING AT ONE YEAR'S WORTH OF DATA

                for(int i = 0; i <= 11; i++) {
                    if(unknownData.get(pointKey).getDayValueDictionary().get(i) == -1) {
                        w1 = knownData.get(outerKey1).getDayValueDictionary().get(i);
                        w2 = knownData.get(outerKey2).getDayValueDictionary().get(i);
                        w3 = knownData.get(outerKey3).getDayValueDictionary().get(i);

                        if(w1 != -1 && w2 != -1 && w3 != -1) {
                            w = N1 * w1 + N2 * w2 + N3 * w3;
                            unknownData.get(pointKey).setDay(i, w);
                        }
                    }
                }
                 */

                /* This is a possible solution if we only have a value for the year, rather than months or days

                for(int i = 0; i <= (need a variable to represent how many years we are searching through); i++) {
                    if(unknownData.get(pointKey).getDayValueDictionary().get(i) == -1) {
                        w1 = knownData.get(outerKey1).getDayValueDictionary().get(i);
                        w2 = knownData.get(outerKey2).getDayValueDictionary().get(i);
                        w3 = knownData.get(outerKey3).getDayValueDictionary().get(i);

                        if(w1 != -1 && w2 != -1 && w3 != -1) {
                            w = N1 * w1 + N2 * w2 + N3 * w3;
                            unknownData.get(pointKey).setDay(i, w);
                        }
                    }
                }


                **I NEED SOME SORT OF IF FUNCTION TO DETERMINE WHAT THE USER INPUT WAS (DAY, MONTH, OR YEAR)
                 */
            }
        }
    }
}
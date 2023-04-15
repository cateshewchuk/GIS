package gis.triangulation;

import java.util.*;

/* Contains methods to complete the Delaunay Triangulation on the points
    in the dataset of known values onto the dataset of unknown values.
    To complete the query of finding the unknown value, the formula
        w(x,y) = N1(x,y)w1 + N2(x,y)w2 + N3(x,y)w3
    where N1, N2, and N3 are the following linear shape functions:
        N1(x,y) = A1/A, N2(x,y) = A2/A, N3(x,y) = A3/A
    where A is the area of the outer triangle and A1, A2, and A3
    are the areas of the three sub-triangles ww2w3, w1ww3, w1w2w respectively
 */

public class Triangulation {
    public static List<Triangle2D> del(List<Vector2D> pointsKnown, Vector2D pointUnknown) throws NotEnoughPointsException {
        //Triangulates the points that we know the data for
        DelaunayTriangulator dt = new DelaunayTriangulator(pointsKnown);
        dt.triangulate();

        //Creates list of triangles created by the Delaunay Triangulation
        List<Triangle2D> triangles = dt.getTriangles();

        return triangles;

        /*
        //Contains all triangles formed
        TriangleSoup tris = new TriangleSoup();
        for(Triangle2D t : triangles) {
            tris.add(t);
        }


        //Returns the triangle with the three points that are
        //closest to the point we are trying to find the query data for
        Triangle2D container = tris.findContainingTriangle(pointUnknown);

        return container;


         */
        }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow(y2 - y1, 2));
    }

    // Area of a triangle using Heron's formula (using three sides of triangle)
    public static double outerArea(Triangle2D tri) {
        return 0.5 * Math.abs(tri.a.x * (tri.b.y - tri.c.y) + tri.b.x * (tri.c.y - tri.a.y) + tri.c.x * (tri.a.y - tri.b.y));
    }
    //
    public static double calcN1(Triangle2D tri, Vector2D point) {
        double A1 = 0.5 * Math.abs(point.x * (tri.b.y - tri.c.y) + tri.b.x * (tri.c.y - point.y) + tri.c.x * (point.y - tri.b.y));
        return A1 / outerArea(tri);
    }

    public static double calcN2(Triangle2D tri, Vector2D point) {
        double A2 = 0.5 * Math.abs(tri.a.x * (point.y - tri.c.y) + point.x * (tri.c.y - tri.a.y) + tri.c.x * (tri.a.y - point.y));
        return A2 / outerArea(tri);
    }

    public static double calcN3(Triangle2D tri, Vector2D point) {
        double A3 = Math.abs(0.5 * (tri.a.x * (tri.b.y - point.y) + tri.b.x * (point.y - tri.a.y) + point.x * (tri.a.y - tri.b.y)));
        return A3 / outerArea(tri);
    }

    public static boolean isInTriangle(Triangle2D tri, Vector2D point) {
        double A = outerArea(tri);
        double A1 = 0.5 * Math.abs(point.x * (tri.b.y - tri.c.y) + tri.b.x * (tri.c.y - point.y) + tri.c.x * (point.y - tri.b.y));
        double A2 = 0.5 * Math.abs(tri.a.x * (point.y - tri.c.y) + point.x * (tri.c.y - tri.a.y) + tri.c.x * (tri.a.y - point.y));
        double A3 = Math.abs(0.5 * (tri.a.x * (tri.b.y - point.y) + tri.b.x * (point.y - tri.a.y) + point.x * (tri.a.y - tri.b.y)));

        return (A == A1 + A2 + A3);
    }
    public static double spatio(double w1, double w2, double w3, double N1, double N2, double N3) {
        return N1 * w1 + N2 * w2 + N3 * w3;
    }
}

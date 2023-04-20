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

    }

    // Area of a triangle using the three vertices
    public static double outerArea(Triangle2D tri) {
        return Math.abs(tri.a.x * (tri.b.y - tri.c.y) + tri.b.x * (tri.c.y - tri.a.y) + tri.c.x * (tri.a.y - tri.b.y));
    }

    public static double calcN1(Triangle2D tri, Vector2D point) {
        // Area of the sub-triangle ww2w3
        double A1 = Math.abs(point.x * (tri.b.y - tri.c.y) + tri.b.x * (tri.c.y - point.y) + tri.c.x * (point.y - tri.b.y));
        return A1 / outerArea(tri);
    }

    public static double calcN2(Triangle2D tri, Vector2D point) {
        // Area of the sub-triangle w1ww3
        double A2 = Math.abs(tri.a.x * (point.y - tri.c.y) + point.x * (tri.c.y - tri.a.y) + tri.c.x * (tri.a.y - point.y));
        return A2 / outerArea(tri);
    }

    public static double calcN3(Triangle2D tri, Vector2D point) {
        // Area of the sub-triangle w1w2w
        double A3 = Math.abs(tri.a.x * (tri.b.y - point.y) + tri.b.x * (point.y - tri.a.y) + point.x * (tri.a.y - tri.b.y));
        return A3 / outerArea(tri);
    }

    // Method to search the triangles formed by the Delaunay Triangulation
    // and find the closest triangle that contains the given point.
    public static Triangle2D closestTriangle(List<Triangle2D> soup, Vector2D point) {
        TriangleSoup triSoup = new TriangleSoup();
        for(Triangle2D triangle : soup) {
            triSoup.add(triangle);
        }

        return triSoup.findContainingTriangle(point);
    }
}
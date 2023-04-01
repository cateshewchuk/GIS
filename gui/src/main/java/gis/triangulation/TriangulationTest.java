package gis.triangulation;

import java.util.*;

public class TriangulationTest{
    public static Triangle2D del() throws NotEnoughPointsException {
        List<Vector2D> pointSetKnownData = new ArrayList<Vector2D>();
        Vector2D unknownPoint = new Vector2D(4.75, 0);

        pointSetKnownData.add(new Vector2D(0,0));
        pointSetKnownData.add(new Vector2D(5, 5));
        pointSetKnownData.add(new Vector2D(5, -5));
        pointSetKnownData.add(new Vector2D(2.5, 2.5));
        pointSetKnownData.add(new Vector2D(5, 6));

        DelaunayTriangulator dt = new DelaunayTriangulator(pointSetKnownData);
        dt.triangulate();

        List<Triangle2D> triangles = dt.getTriangles();

        TriangleSoup tris = new TriangleSoup();
        for(Triangle2D t : triangles) {
            tris.add(t);
        }

        Triangle2D container = tris.findContainingTriangle(unknownPoint);

        System.out.println(container);

        double w1 = 10;
        double w2 = 15;
        double w3 = 13;

        double query = spatio(w1, w2, w3, calcN1(container, unknownPoint), calcN2(container, unknownPoint), calcN3(container, unknownPoint));

        System.out.print(query);

        return container;
        }



    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow(y2 - y1, 2));
    }

    // Area of a triangle using Heron's formula (using three sides of triangle)
    public static double outerArea(Triangle2D tri) {
        return Math.abs(0.5 * (tri.a.x * (tri.b.y - tri.c.y) + tri.b.x * (tri.c.y - tri.a.y) + tri.c.x * (tri.a.y - tri.b.y)));
    }

    public static double calcN1(Triangle2D tri, Vector2D point) {
        double A1 = Math.abs(0.5 * (point.x * (tri.b.y - tri.c.y) + tri.b.x * (tri.c.y - point.y) + tri.c.x * (point.y - tri.b.y)));
        return A1 / outerArea(tri);
    }

    public static double calcN2(Triangle2D tri, Vector2D point) {
        double A2 = Math.abs(0.5 * (tri.a.x * (point.y - tri.c.y) + point.x * (tri.c.y - tri.a.y) + tri.c.x * (tri.a.y - point.y)));
        return A2 / outerArea(tri);
    }

    public static double calcN3(Triangle2D tri, Vector2D point) {
        double A3 = Math.abs(0.5 * (tri.a.x * (tri.b.y - point.y) + tri.b.x * (point.y - tri.a.y) + point.x * (tri.a.y - tri.b.y)));
        return A3 / outerArea(tri);
    }

    public static double spatio(double w1, double w2, double w3, double N1, double N2, double N3) {
        return N1 * w1 + N2 * w2 + N3 * w3;
    }
}

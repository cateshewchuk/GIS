package gis.app;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;
public class Algorithm {
    //Scratch work for Interpolation Algorithm
    /* Input:
     * A - matrix representing known data in the form [t x y w],
     * where t = time, x, y = location coordinates, w = known
     * measured value
     * B - matrix of query points in the form [t x y], where
     * t = time, x, y = location coordinates
     * dtMesh (optional) - the gis.triangulation used to perform
     * the interpolation
     * Da (optional) = a matrix containing the time interpolated
     * values for all locations in A;
     * T (optional) = a vector containing all times in A
     *
     *
     * Output:
     * res - a matrix containing the query point coordinates and
     * the interpolated value at each query point in the form [t x y w]
     *
     *
     * dtMesh - a matrix containing the time interpolated values for
     * all locations in A
     * T - a vector containing all distinct times in A
     * **Check format of input arguments
     * 		if the dtMesh, Da, T are not provided, calculate them as follows:
     * if dtMesh, Da, or T is null:
     * 		dtMesh = calculate Delaunay Triangulation using t, x, y of A;
     * 		Da = calculate time-interpolated value at the Cartesian product
     * 			of all locations in A with all times in T
     * 		T = collect unique times in A;
     * end
     *
     * pl = coordinates of the simplex containing the query point
     * bc = barycentric coordinates
     * [pl, bc] = dtMesh.pointLocation(B)
     * triVals = extract w from A at each corner identified in pl
     * calculate the interpolated values at each query point
     * Vq = bc * triVals;
     * Set Vq to Nan if no containing tetrahedron was found;
     * res = append Vq to B.
     */

    //Euclidean distance between two points
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow(y2 - y1, 2));
    }

    // Area of a triangle using Heron's formula (using three sides of triangle)
    public static double area(double a, double b, double c) {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public static double calculateOuterArea(double[][] A) {
        double side1 = distance(A[0][1], A[0][2], A[1][1], A[1][2]);
        double side2 = distance(A[1][1], A[1][2], A[2][1], A[2][2]);
        double side3 = distance(A[0][1], A[0][2], A[2][1], A[2][2]);
        return area(side1, side2, side3);
    }
    //A1 = ww2w3, A2 = w1ww3, A3 = w1w2w
    public static double calculateN1(double[][] A, double[][] B) {
        double w = distance(A[1][1], A[1][2], A[2][1], A[2][2]);
        double w2 = distance(A[2][1], A[2][2], B[0][1], B[0][2]);
        double w3 = distance(A[1][1], A[1][2], B[0][1], B[0][2]);
        double A1 = area(w, w2, w3);
        double outerArea = calculateOuterArea(A);
        return A1 / outerArea;
    }

    public static double calculateN2(double[][] A, double[][] B) {
        double w = distance(A[0][1], A[0][2], A[2][1], A[2][2]);
        double w2 = distance(A[2][1], A[2][2], B[0][1], B[0][2]);
        double w3 = distance(A[0][1], A[0][2], B[0][1], B[0][2]);
        double A2 = area(w, w2, w3);
        double outerArea = calculateOuterArea(A);
        return A2 / outerArea;
    }

    public static double calculateN3(double[][] A, double[][] B) {
        double w = distance(A[1][1], A[1][2], A[0][1], A[0][2]);
        double w2 = distance(A[0][1], A[0][2], B[0][1], B[0][2]);
        double w3 = distance(A[1][1], A[1][2], B[0][1], B[0][2]);
        double A3 = area(w, w2, w3);
        double outerArea = calculateOuterArea(A);
        return A3 / outerArea;
    }


    public static double interpolateResAtQueryPoint(double[][] A, double[][] B) {
        double N1 = calculateN1(A, B);
        double N2 = calculateN2(A, B);
        double N3 = calculateN3(A, B);
        double w1 = A[0][3];
        double w2 = A[1][3];
        double w3 = A[2][3];

        return N1 * w1 + N2 * w2 + N3 * w3;
    }

    //Example of interpolating time for March 7 and 8
    //for first location in A
    public static double interpolateTime(double[][] A, double t) {
        double t2 = A[3][0];
        double t1 = A[0][0];
        double w2 = A[3][3];
        double w1 = A[0][3];

        return ((t2 - t) / (t2 - t1) * w1) + ((t - t1) / (t2 - t1) * w2);
    }

    public static double[][] res(double[][] A, double[][] B) {
        double[][] res = new double[2][4];
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 3; j++)
                res[i][j] = B[i][j];
            res[i][3] = interpolateResAtQueryPoint(A, B);
        }

        return res;
    }



    // Driver code
    // Dummy data using three coordinates as known input and
    // two query points
    public static void main(String[] args) {
        double mar5 = 1.0;
        double mar6 = 2.0;
        double mar7 = 3.0;
        double mar8 = 4.0;

        //Matrix A in form [t x y w]
        double[][] A = new double[][] {
                {mar5, -87.881412, 30.498001, 14.6},
                {mar5, -85.802181, 33.281261, 16.6},
                {mar5, -87.650556, 34.760556, 14.7},
                {mar8, -87.881412, 30.498001, 9.8},
                {mar8, -85.802181, 33.281261, 14.7},
                {mar8, -87.650556, 34.760556, 9.6}
        };

        //Matrix B in form [t x y]
        double[][] B = new double[][] {
                {mar5, -87.035539, 32.406601},
                {mar8, -87.035539, 32.406601},
        };

        double[][] results = res(A, B);

        System.out.print("This is the interpolated value at the query point on March 5: ");
        System.out.println(A[0][3]);

        System.out.print("Interpolated time of location 1 on March 6: ");
        System.out.println(interpolateTime(A, mar6));

        System.out.print("Interpolated time of location 1 on March 7: ");
        System.out.println(interpolateTime(A, mar7));
    }
}
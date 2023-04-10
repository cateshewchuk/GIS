package gis.Algorithm;
import gis.model.*;
import java.util.*;
import gis.app.*;
import gis.triangulation.*;

/*  Computing the values of unknown data using triangulation and the
    SF-reduction formula

 */
public class SpatioInterpolation {
    public double[][] queryValues(HashMap<String, Location> knownData, HashMap<String, Location> unknownData) throws NotEnoughPointsException {
        double[][] A = ConvertData.convertToA(knownData);
        double[][] B = ConvertData.convertToB(unknownData);
        double[][] res = new double[B.length][4];

        //Copy parts of B into res
        for(int i = 0; i < B.length; i++) {
            res[i][0] = B[i][0];
            res[i][1] = B[i][1];
            res[i][2] = B[i][2];
        }
        List<Vector2D> knownCoordinates = ConvertData.knownXY(A);
        List<Vector2D> unknownCoordinates = ConvertData.knownXY(B);

        double N1;
        double N2;
        double N3;

        for(int i = 0; i < unknownCoordinates.size(); i++) {
            Triangle2D container = Triangulation.del(knownCoordinates, unknownCoordinates.get(i));

        }

        return res;
    }
}

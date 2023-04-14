package gis.Algorithm;

import gis.model.*;
import java.util.*;
import gis.triangulation.*;

public class ConvertData {
    /*
    public static double[][] convertToA(HashMap<String, Location> dataKnown) {
        double[][] matrixA = new double[dataKnown.size()][4];
        int row = 0;

        //Iterator to go through HashMap
        Iterator<Map.Entry<String, Location>> it = dataKnown.entrySet().iterator();

        //Iterating the HashMap
        while(it.hasNext()) {
            Map.Entry<String, Location> new_Map = (Map.Entry<String, Location>)it.next();

            matrixA[row][0] = row % 365;
            matrixA[row][1] = new_Map.getValue().getX();
            matrixA[row][2] = new_Map.getValue().getY();
            matrixA[row][3] = new_Map.getValue().getDayValueDictionary().get(row % 365);

            row++;
        }

        return matrixA;
    }

    public static double[][] convertToB(HashMap<String, Location> dataUnknown) {
        double[][] matrixB = new double[dataUnknown.size()][4];
        int row = 0;

        //Iterator
        Iterator<Map.Entry<String, Location>> it = dataUnknown.entrySet().iterator();

        //Iterating the HashMap
        while(it.hasNext()) {
            Map.Entry<String, Location> new_Map = (Map.Entry<String, Location>)it.next();

            matrixB[row][0] = row % 365;
            matrixB[row][1] = new_Map.getValue().getX();
            matrixB[row][2] = new_Map.getValue().getY();

            row++;
        }

        return matrixB;
    }


    //This does not account for leap years, will edit later when we
    // have it all figured out
    public Vector<Integer> timeVector() {
        Vector<Integer> time = new Vector<>(365);
        for(int i = 0; i < 365; i++)
            time.add(i + 1);

        return time;
    }
    */

    public static List<Vector2D> coordinatesToVector(HashMap<String, Location> data) {
        List<Vector2D> knownCoordinates = new ArrayList<Vector2D>();

        for(Map.Entry<String, Location> entry: data.entrySet()) {
            Vector2D vector = new Vector2D(entry.getValue().getX(), entry.getValue().getY());
            if(knownCoordinates.contains(vector))
                break;
            else
                knownCoordinates.add(vector);
        }

        return knownCoordinates;
    }

    /*
    public double[][] convertToResult(double[][] matrix, double query) {
        double[][] res = new double[matrix.length][4];

        for(int i = 0; i < matrix.length; i++) {
            res[i][0] = matrix[i][0];
            res[i][1] = matrix[i][1];
            res[i][2] = matrix[i][2];
            res[i][3] = query;
        }

        return res;
    }

     */
}

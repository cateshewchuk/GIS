package gis.Algorithm;
import gis.model.*;
import gis.triangulation.NotEnoughPointsException;

import java.util.*;

// A concise class to run both interpolations at once.

public class Interpolation {
    public static void runInterpolation (HashMap<String, Location> knownData, HashMap<String, Location> unknownData) throws NotEnoughPointsException {
        SpatioInterpolation.spaceFunction(knownData, unknownData);
        TimeInterpolation.timeInterpolate(unknownData);
    }

    // Helper method for validation interpolation. See CrossValidation.java for details.
    public static void runInterpolation(HashMap<String, Location> knownData, Location locationToSolve) throws NotEnoughPointsException {
        HashMap<String, Location> locHashMap= new HashMap<>();
        locHashMap.put(locationToSolve.getX() + ", " + locationToSolve.getY(), locationToSolve);
        runInterpolation(knownData, locHashMap);
    }
}

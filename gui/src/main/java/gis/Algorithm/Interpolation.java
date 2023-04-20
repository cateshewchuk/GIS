package gis.Algorithm;
import gis.model.*;
import gis.triangulation.NotEnoughPointsException;

import java.util.*;

// A concise class to run both interpolations at once.

public class Interpolation {
    public static void runInterpolation (HashMap<String, Location> knownData, HashMap<String, Location> unknownData) throws NotEnoughPointsException {
        SpatioInterpolation.queryResults(knownData, unknownData);
        TimeInterpolation.timeInterpolate(unknownData);
    }
}

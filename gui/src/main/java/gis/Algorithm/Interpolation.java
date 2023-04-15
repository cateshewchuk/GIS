package gis.Algorithm;
import gis.model.*;
import gis.triangulation.NotEnoughPointsException;

import java.util.*;

public class Interpolation {
    public static void runInterpolation (HashMap<String, Location> knownData, HashMap<String, Location> unknownData) throws NotEnoughPointsException {
        SpatioInterpolation.queryValues(knownData, unknownData);
        TimeInterpolation.timeInterpolate(unknownData);
    }
}

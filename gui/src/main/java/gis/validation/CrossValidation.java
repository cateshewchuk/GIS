package gis.validation;

import gis.model.Location;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CrossValidation {
    private HashMap<String, Location> locationsToValidate;

    public CrossValidation(HashMap<String, Location> locationsToValidate) {
        // Fairly messy, refactor if given time
        this.locationsToValidate = locationsToValidate;
    }

    // Driver method for Leave Out One Cross Validation (LOOCV)
    // See papers 3 and 5
    public void loocv() {
        // Create file to write to
        File outputFile = new File("loocv_sf.txt");

        try {
            // Delete file if it exists, then creates a new one.
            outputFile.delete();
            outputFile.createNewFile();

            // Creates new writer
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            // Loop through every Location in Location HashMap
            for (Location loc : locationsToValidate.values()) {
                // TODO: Need to fix to handle months and years once implementation is solidified
                validateDays(loc, writer);
            }

        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Something went wrong with file access in cross validation");
        }
    }

    public void validateDays(Location loc, BufferedWriter writer) throws IOException {
        HashMap<Integer, Double> dayDict = loc.getDayValueDictionary();

        for (Integer day : dayDict.keySet()) {
            solveForSingleDay(day, dayDict);
        }
    }

    public void solveForSingleDay(int day, HashMap<Integer, Double> dayDict) {

    }
}

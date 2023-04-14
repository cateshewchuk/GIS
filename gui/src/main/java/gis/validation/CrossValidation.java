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
        // This is probably not ideal
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

            writer.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Something went wrong with file access in cross validation.");
        }
    }

    // For each location object, this method validates every day
    public void validateDays(Location loc, BufferedWriter writer) throws IOException {
        HashMap<Integer, Double> dayDict = loc.getDayValueDictionary();

        // Creates a copy of the HashMap
        HashMap<String, Location> hashCopy = new HashMap<>(locationsToValidate);

        // Removes the location being worked with
        hashCopy.remove(loc.getX() + ", " + loc.getY());

        for (Integer day : dayDict.keySet()) {
            // Calculates the result by pretending it doesn't exist
            // TODO: Call method in Algorithm package that when given a HashMap of locations and day, calculate
            //  for that location and day. Method does not exist yet.

            // Method to do above here

            // Writes retrieved answer to file. Dummy placeholder of 0 is used, will be replaced when above method is completed.
            writer.write(dayDict.get(day) + ", 0");
            writer.newLine();
        }
    }


}

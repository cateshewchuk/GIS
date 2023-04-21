package gis.validation;

import gis.Algorithm.Interpolation;
import gis.model.Location;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CrossValidation {
    private HashMap<String, Location> locationsToValidate;

    private ArrayList<ValidationRow> validationRows;

    public CrossValidation(HashMap<String, Location> locationsToValidate) {
        // This is probably not ideal
        this.locationsToValidate = locationsToValidate;
        validationRows = new ArrayList<>();
    }

    // Driver method for Leave Out One Cross Validation (LOOCV)
    // See papers 3 and 5
    public void loocv() {
        //Creates LOOCV file
        createLOOCVFile();

        //Create LOOCV metrics file
        createLOOCVMetricsFile();
    }

    public void createLOOCVFile() {
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
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Something went wrong horribly wrong in the cross validation.");
        }
    }

    // For each location object, this method validates every day
    public void validateDays(Location loc, BufferedWriter writer) throws Exception {
        // Keeps copy of dayDict. This is ESSENTIAL, interpolation destroys this dict.
        HashMap<Integer, Double> dayDict = loc.getDayValueDictionary();

        // Creates a shallow copy of the HashMap. Potentially problematic?
        HashMap<String, Location> hashCopy = new HashMap<>(locationsToValidate);

        // Removes the location being worked with in the copy
        hashCopy.remove(loc.getX() + ", " + loc.getY());

        // Resets day value dict
        loc.setDayValueDictionary(new HashMap<>());

        /* Solves for all 365 days in excluded location. It does this by pretending the excluded location is the only
        location in a newly created hashmap to solve, using the same exact algorithm. Only the days that exist in the
        original are relevant, so this is slightly inefficient.
         */
        Interpolation.runInterpolation(hashCopy, loc);

        // Iterate through every day that matters
        for (Map.Entry entry: dayDict.entrySet()) {
            // If data does not exist for the day, it can't be validated.
            if ((double) entry.getValue() == -1) {
                continue;
            }

            // Writes retrieved answer to file.
            writer.write(dayDict.get(entry.getKey()) + ", " + loc.getDay((Integer) entry.getKey()));
            writer.newLine();

            // Stores each row in list for CrossValidation object.
            validationRows.add(new ValidationRow(dayDict.get(entry.getKey()), loc.getDay((Integer) entry.getKey())));
        }

        // Resets day value dict to what it was before
        loc.setDayValueDictionary(dayDict);
    }

    private void createLOOCVMetricsFile() {
        // Create file to write to
        File outputFile = new File("error_statistics_sf.txt");

        try {
            // Delete file if it exists, then creates a new one.
            outputFile.delete();
            outputFile.createNewFile();

            // Creates new writer
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            calculateStats(writer);

            writer.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Something went wrong with file access in cross validation.");
        }
    }

    // Calculates error stats, see paper 2 section 2.3.2
    public void calculateStats(BufferedWriter writer) throws IOException {
        // Sum of errors
        double errorSum = 0;

        // Squared sum of errors
        double squaredErrorSum = 0;

        // Relative error sum
        double relativeErrorSum = 0;

        // Size to divide by
        int size = validationRows.size();

        for (ValidationRow row : validationRows) {
            // Checks if data is malformed. If so, ignore it.
            if (row.getEstimated() == -1 || Double.isNaN(row.getEstimated())) {
                size--;
                continue;
            }

            // Calculates error
            double error = Math.abs(row.getEstimated() - row.getActual());

            // Add appropriately to sums
            errorSum += error;
            squaredErrorSum += error * error;
            relativeErrorSum += error / row.getActual();
        }

        // Mean Absolute Error
        double MAE = errorSum / size;

        // Mean Squared Error
        double MSE = squaredErrorSum / size;

        // Root Mean Squared Error
        double RMSE = Math.sqrt(MSE);

        // Mean Absolute Relative Error
        double MARE = relativeErrorSum / size;

        //Write errors to file
        writer.write("MAE: " + MAE);
        writer.newLine();
        writer.write("MSE: " + MSE);
        writer.newLine();
        writer.write("RMSE: " + RMSE);
        writer.newLine();
        writer.write("MARE: " + MARE);
    }
}

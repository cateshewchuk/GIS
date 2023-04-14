package gis.validation;

import gis.model.Location;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
            //  for that location and day using spatiotemporal method. Method does not exist yet.

            // Method to do above here

            // Writes retrieved answer to file. Dummy placeholder of 0 is used, will be replaced when above method is completed.
            writer.write(dayDict.get(day) + ", 0");
            writer.newLine();

            // Stores each row in list for CrossValidation object. 0 is once again used as placeholder
            validationRows.add(new ValidationRow(dayDict.get(day), 0));
        }
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

        for (ValidationRow row : validationRows) {
            // Calculates error
            double error = Math.abs(row.getEstimated() - row.getActual());

            // Add appropriately to sums
            errorSum += error;
            squaredErrorSum += error * error;
            relativeErrorSum += error / row.getActual();
        }

        // Mean Absolute Error
        double MAE = errorSum / validationRows.size();

        // Mean Squared Error
        double MSE = squaredErrorSum / validationRows.size();

        // Root Mean Squared Error
        double RMSE = Math.sqrt(MSE);

        // Mean Absolute Relative Error
        double MARE = relativeErrorSum / validationRows.size();

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

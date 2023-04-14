package gis.validation;

// Simple POJO to store pairs of actual PM2.5 measurements and their estimated results.
public class ValidationRow {
    private double actual;

    private double estimated;

    public ValidationRow(double actual, double estimated) {
        this.actual = actual;
        this.estimated = estimated;
    }

    public double getActual() {
        return actual;
    }

    public void setActual(double actual) {
        this.actual = actual;
    }

    public double getEstimated() {
        return estimated;
    }

    public void setEstimated(double estimated) {
        this.estimated = estimated;
    }
}

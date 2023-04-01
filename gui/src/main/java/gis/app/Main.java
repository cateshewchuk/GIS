package gis.app;

import gis.triangulation.NotEnoughPointsException;
import gis.triangulation.TriangulationTest;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) throws NotEnoughPointsException {
        new TriangulationTest().del();
        Application.launch(App.class, args);
    }
}

package gis.app;

import gis.triangulation.NotEnoughPointsException;
import javafx.application.Application;

public class Main {

    // See handleButtonAction method in MainController for main driver of file reading and report generation.
    public static void main(String[] args) throws NotEnoughPointsException {
        Application.launch(App.class, args);
    }
}

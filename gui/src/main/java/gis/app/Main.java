package gis.app;

import gis.triangulation.NotEnoughPointsException;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) throws NotEnoughPointsException {
        Application.launch(App.class, args);
    }
}

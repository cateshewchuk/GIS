package gis.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("table-view.fxml")));
            Scene scene = new Scene(root,800,800);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/application.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

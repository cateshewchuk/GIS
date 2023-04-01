module gis.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires java.desktop;

    opens gis.app to javafx.fxml;
    exports gis.app;
    exports gis.model;
    opens gis.model to javafx.fxml;
    exports gis.triangulation;
    opens gis.triangulation to javafx.fxml;
}
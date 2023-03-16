package gis.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class DialogModel {

    private TextField InitialFilePath;
    private TextField filepathInitialData;
    private ComboBox timeDomain;

    private static DialogModel instance = new DialogModel();
    public static DialogModel getInstance(){
        return instance;
    }


    public TextField getInitialFilePath() {
        return InitialFilePath;
    }

    public void setInitialFilePath(TextField InitialFilePath) {
        this.InitialFilePath = InitialFilePath;
    }

    public TextField getfilepathInitialData() {
        return filepathInitialData;
    }

    public void setfilepathInitialData(TextField filepathInitialData) {
        this.filepathInitialData = filepathInitialData;
    }

    public ComboBox gettimeDomain() {
        return timeDomain;
    }

    public void settimeDomain(ComboBox timeDomain) {
        this.timeDomain = timeDomain;
    }

}
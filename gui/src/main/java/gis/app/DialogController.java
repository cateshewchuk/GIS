package gis.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.TextField;
import java.util.*;

public class DialogController implements Initializable {

    private Stage parentStage;
    @FXML
    private TextField InitialFilePath;
    @FXML
    private TextField interpolateDataFilePath;
    @FXML
    public ComboBox timeDomain;
    public String filepathInitialData;
    public String filepathInterpolateData = "";

    ObservableList<String> timeDomainList = FXCollections.observableArrayList("Day", "Month", "Year");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeDomain.setItems(timeDomainList);
        DialogModel.getInstance().setInitialFilePath(InitialFilePath);
        DialogModel.getInstance().setfilepathInitialData(interpolateDataFilePath);
        DialogModel.getInstance().settimeDomain(timeDomain);
    }

    void setStage(Stage temp) {
        parentStage = temp;
    }

    @FXML
    private void initialDataImport(ActionEvent event) {
        filepathInitialData = browseFile();
        InitialFilePath.setText(filepathInitialData);
    }

    @FXML
    private void DataInterpolateDialog(ActionEvent event) {
        filepathInterpolateData = browseFile();
        interpolateDataFilePath.setText(filepathInterpolateData);
    }

    private String getFilePath() { return InitialFilePath.getText(); }

    public void setFilePath(String fp) {
        InitialFilePath.setText(fp);
    }

    private String browseFile() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(stage);
        String path = file.getPath();

        return path;
    }


}

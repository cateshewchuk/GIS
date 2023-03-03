package gis.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


public class MainController implements Initializable {

    @FXML
    private TableView<MasterTable> table;
    @FXML
    public TableColumn<MasterTable, Integer> tableID;
    @FXML
    public TableColumn<MasterTable, String> time;
    @FXML
    public TableColumn<MasterTable, String> xCol;
    @FXML
    public TableColumn<MasterTable, String> yCol;
    @FXML
    public TableColumn<MasterTable, String> pms;
    @FXML public Button importBtn;
    public ComboBox<String> timeDomain;
    private String timeF = "";
    private Object selected = "cancelled.";
    private String filepath = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeDomain.getItems().removeAll(timeDomain.getItems());
        timeDomain.getItems().addAll("Day", "Month", "Year");
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == importBtn) {
            // show dialog
            ChoiceDialog<String> dialog = new ChoiceDialog<String>("Day", "Month", "Year");
            dialog.setTitle("Required");
            dialog.setHeaderText("Filter by Time Domain");
            dialog.setContentText("Select Time type");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                selected = result.get();
                // updates combobox
                timeDomain.getSelectionModel().select(selected.toString());
                // continue to importing txt file.
                importFile();
            }
        }
    }

    @FXML
    private void handleComboBoxAction(ActionEvent event) {
        int selectedIndex = timeDomain.getSelectionModel().getSelectedIndex();
        Object selectedItem = timeDomain.getSelectionModel().getSelectedItem();
        selected = selectedItem;
        // System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
        //System.out.println("   ComboBox.getValue(): " + timeDomain.getValue());

        updateTable();
    }

    private void updateTable() {

        List<MasterTable> metric = readMetricFromCSV(filepath);
        ObservableList<gis.app.MasterTable> masterTable = FXCollections.observableArrayList(metric);
        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        tableID.setCellValueFactory(new PropertyValueFactory<>("TableID"));
        time.setCellValueFactory(new PropertyValueFactory<>("Time"));
        xCol.setCellValueFactory(new PropertyValueFactory<>("Xcol"));
        yCol.setCellValueFactory(new PropertyValueFactory<>("Ycol"));
        pms.setCellValueFactory(new PropertyValueFactory<>("Pms"));

        //add your data to the table here.
        table.setItems(masterTable);

    }

    private void importFile() {

        filepath = browseFile();
        updateTable();
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
        System.out.println("path: " + path);
        return path;
    }

    public List<MasterTable> readMetricFromCSV(String fileName) {

        List<MasterTable> metricsss = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);


        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();
            // start loop at 2nd line, skipping first line
            while ((line = br.readLine()) != null) {


                // are read
                String[] attributes = line.split("\\s+");
                MasterTable valueOfMetric = createMetric(attributes);
                metricsss.add(valueOfMetric); // adding metric into ArrayList
                // skip empty line
                // line.isEmpty() || line.trim().equals("") ||
                // line.trim().equals("\n"))
                br.readLine();

                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return metricsss;
    }

    private MasterTable createMetric(String[] metadata) {
        int id = Integer.parseInt(metadata[0]);
        int year = Integer.parseInt(metadata[1]);
        int month = Integer.parseInt(metadata[2]);
        int day = Integer.parseInt(metadata[3]);
        String x = metadata[4];
        String y = metadata[5];
        String pms = metadata[6];

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy");

        switch (selected.toString()) {
            case "Day" -> timeF = format1.format(calendar.getTime());
            case "Year" -> timeF = format3.format(calendar.getTime());
            case "Month" ->
                // year/month
                    timeF = format2.format(calendar.getTime());
        }
        //System.out.println("timeF: " + timeF);

        return new MasterTable(id, timeF, x, y, pms);//,cc
    }

    // add your data here from any source


}
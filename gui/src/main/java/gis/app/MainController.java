package gis.app;

import gis.model.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.awt.Desktop;
import java.util.stream.Collectors;

import static java.lang.Integer.sum;


public class MainController implements Initializable {

    @FXML
    private TableView<MasterTable> table;
    @FXML
    public TableColumn<MasterTable, Integer> tableID;
    @FXML
    public TableColumn<MasterTable, Integer> Year;
    @FXML
    public TableColumn<MasterTable, String> xCol;
    @FXML
    public TableColumn<MasterTable, String> yCol;
    @FXML
    public TableColumn<MasterTable, String> pms;
    @FXML
    public Button importBtn;
    public ComboBox<String> timeDomain;
    public String filepathInitialData = "";
    public String filepathInterpolateData = "";
    private Object selected = "Day.";

    private String timeF = "";
    private ObservableList<gis.app.MasterTable> masterTable = FXCollections.observableArrayList();

    // Create a HashMap object called capitalCities
    private HashMap<String, Location> capitalCities = new HashMap<String, Location>();

    // create GENERAL hashmap of arrays with key being equal to [x,y] and the object will data
    private Map<String, HashMap<Integer, Location>> dataPoints = new HashMap<String, HashMap<Integer, Location>>();

    // what the outputlooks like
   /* Parent Key=-85.818021, 39.807323 [ {Month Key=1, value=x = -85.818021   y = 39.807323
    Day/Measurement = [{1=0.0, 2=0.0, 3=0.0, 4=0.0, 5=0.0, 6=0.0, 7=0.0, 8=0.0, 9=0.0, 10=0.0, 11=0.0, 12=0.0, 13=0.0, 14=0.0, 15=0.0, 16=0.0, 17=0.0, 18=0.0, 19=0.0, 20=0.0, 21=0.0, 22=0.0, 23=0.0, 24=0.0, 25=0.0, 26=0.0, 27=0.0, 28=0.0, 29=0.0, 30=0.0, 31=0.0}] }]*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        importBtn.fireEvent(new ActionEvent());
        //importInitialFile();
    }

    public static List getDatesBetweenUsingJava7(Date startDate, Date endDate) {
        List datesInRange = new ArrayList<>();
        Calendar calendar = getCalendarWithoutTime(startDate);
        Calendar endCalendar = getCalendarWithoutTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        return datesInRange;
    }

    private static Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    /* Get Initial Data Set File */


    private void locationDataSetImport(String filepathInterpolateData) {
        //List<MasterTable> iFile =
        createCols(filepathInterpolateData);
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
        //System.out.println("path: " + path);
        return path;
    }

    private void createCols(String filepathInterpolateData) {
        table.getItems().clear();

        TableColumn id = new TableColumn("ID");
        TableColumn year = new TableColumn("YEAR");
        TableColumn month = new TableColumn("MONTH");
        TableColumn day = new TableColumn("Day");
        TableColumn x = new TableColumn("X");
        TableColumn y = new TableColumn("Y");
        TableColumn pms = new TableColumn("PM25");


        /* Upload Data to Table */
        List<MasterTable> metric = readNewFile(this.filepathInterpolateData);
        masterTable = FXCollections.observableArrayList(metric);

        id.setCellValueFactory(new PropertyValueFactory<>("tableID"));
        year.setCellValueFactory(new PropertyValueFactory<>("Year"));

        switch (selected.toString()) {
            case "Day" -> table.getColumns().addAll(id, year, month, day, x, y, pms);
            case "Year" -> table.getColumns().addAll(id, year, x, y, pms);
            case "Month" -> table.getColumns().addAll(id, year, month, x, y, pms);

        }

        month.setCellValueFactory(new PropertyValueFactory<>("Month"));
        day.setCellValueFactory(new PropertyValueFactory<>("Day"));
        x.setCellValueFactory(new PropertyValueFactory<>("Xcol"));
        y.setCellValueFactory(new PropertyValueFactory<>("Ycol"));
        pms.setCellValueFactory(new PropertyValueFactory<>("pms"));


        //add your data to the table here.
        table.setItems(masterTable);
        createFile();
    }

    private List<MasterTable> readNewFile(String fileName) {
        List<MasterTable> metricsss = new ArrayList<>();

        Path pathToFile = Paths.get(fileName);
        int lineCount = 0;

        // get array of dates
        LocalDate ld = LocalDate.of(2009, Month.JANUARY, 1);
        LocalDate endDate = ld.plusYears(1);

        List<LocalDate> workDays = new ArrayList<>(365);

        while (ld.isBefore(endDate)) {
            workDays.add(ld);
            ld = ld.plusDays(1);
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> formats = workDays.stream().map(value -> value.format(format)).collect(Collectors.toList());


        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.UTF_16LE)) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                String[] attributes = line.split("\\s+");
                Double x = Double.valueOf(attributes[1]);
                Double y = Double.valueOf(attributes[2]);
                String dataSourceKey = attributes[1] + ", " + attributes[2];

                //create array of employee object
                Location[] loc = new Location[12];
                HashMap<Integer, Location> monthstocome = new HashMap<>();

                Location newLoc = new Location(x, y, Integer.parseInt(attributes[0]));

                for (int i = 0; i < 12; i++) {
                    int a = sum(i, 1);
                    monthstocome.put(a, newLoc);

                    //loc[i] = new Location(x,y);
                    for (String value : formats) {
                        // Parses the date
                        LocalDate dt = LocalDate.parse(value);
                        int d = dt.getDayOfMonth();
                        int m = dt.getMonthValue();
                        //if month equals month then set day
                        if (m == a) {
                            //loc[i].setDay(d, 0.0);
                            newLoc.setDay(d, 0.0);
                        }
                    }
                }

                dataPoints.put(dataSourceKey, monthstocome);
                lineCount++;


            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //make sure row length and hashmap length match, to verify all points are being imported
        System.out.println(dataPoints.size());
        System.out.println(lineCount);

        Path initialDataSetPath = Paths.get(filepathInitialData);
        ///dataPoints.get("-86.501121, 32.500172").get(1).setDay(1, 2.0);
        /* loop through initial data input set */
        try (BufferedReader br2 = Files.newBufferedReader(initialDataSetPath,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line2 = br2.readLine();
            // start loop at 2nd line, skipping first line
            while ((line2 = br2.readLine()) != null) {
                String[] attributes2 = line2.split("\\s+");
                String dataSourceKey2 = attributes2[4] + ", " + attributes2[5];
                int month2 = Integer.parseInt(attributes2[2]);
                int day2 = Integer.parseInt(attributes2[3]);
                Double pm25 = Double.parseDouble(attributes2[6]);
                //System.out.println(dataSourceKey2);
                //dataPoints.get("-110.76118, 43.47808").get(1).setDay(1, 2.0);

                // Check is key exists in the Map
                boolean isKeyPresent = dataPoints.containsKey(dataSourceKey2);
                if (isKeyPresent) {
                    // insert default measurements associated with locations and month, day, years
                    //System.out.println(dataPoints.get(dataSourceKey2));
                    dataPoints.get(dataSourceKey2).get(month2).setDay(day2, pm25);
                }


            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


        for (Map.Entry<String, HashMap<Integer, Location>> entryA : dataPoints.entrySet()) {
            String[] attr = new String[7];

            // get the xy key
            String xyKey = entryA.getKey();
            // get the month hashmap associated with xy
            HashMap<Integer, Location> monthLocAssocKey = entryA.getValue();
            //System.out.println("Parent XY Key=" + xyKey);
            for (Map.Entry<Integer, Location> entryB : monthLocAssocKey.entrySet()) {
                Integer monthKey = entryB.getKey();
                Location locObj = entryB.getValue();

                // print out hashmap for key month = obj location association
                // month key is the month points were taken
                //System.out.println("Month Key=" + monthKey + ", value=" + value2.showData() );
                // set id
                attr[0] = Integer.toString(locObj.getID());
                // set year
                attr[1] = "2009"; // pass in year someway

                // loop through the days to set day, month and pms
                for (Map.Entry<Integer, Double> entryC : locObj.getDayValueDictionary().entrySet()) {
                    Integer day = entryC.getKey();
                    // set month
                    attr[2] = Integer.toString(monthKey);
                    Double measurement = entryC.getValue();

                    // set day
                    attr[3] = Integer.toString(day);
                    // set pm25
                    attr[6] = Double.toString(measurement);

                    // set x
                    attr[4] = Double.toString(locObj.getX());

                    // set y
                    attr[5] = Double.toString(locObj.getY());

                    MasterTable valueOfMetric = createPrettyData(attr);
                    metricsss.add(valueOfMetric); // adding metric into ArrayList
                }

            }


               /* Another way to print it out
               for(Location val : value.values()) {System.out.println(val.showData());}*/
            //System.out.println("\n");

        }


        sort(metricsss);
        return metricsss;
    }

    // sort table
    public static void sort(List<MasterTable> list) {
        list.sort((o1, o2)
                -> Integer.valueOf(o1.getTableID()).compareTo(
                Integer.valueOf(o2.getTableID()))
        );
    }


    /*This creates a flat string array, organizes data, so it cant be neatly print to gui and output file */

    private MasterTable createPrettyData(String[] metadata) {

        int id = Integer.parseInt(metadata[0]);
        int year = Integer.parseInt(metadata[1]); // get year somehow
        int month = Integer.parseInt(metadata[2]);
        int day = Integer.parseInt(metadata[3]);
        Double x = Double.parseDouble(metadata[4]);
        Double y = Double.parseDouble(metadata[5]);
        Double pms = Double.parseDouble(metadata[6]);

        /* for when I figure out how to do the time domain stuff
        switch (selected.toString()) {
            case "Day":
        }*/


        // System.out.println("id " + id + "year " + " month " + month + " day " + day + " x " + x + " y " + y + " pm25 " + pms);
        return new MasterTable(id, year, month, day, x, y, pms);//,cc
    }

    private void createFile() {
        String home = System.getProperty("user.home");

        //   FileInputStream fi = new FileInputStream(new File(home+"/Downloads/county_id_t_w.txt"));
        // Write the list to a text file
        try {
            writeToTextFile(home + "/Downloads/county_id_t_w.txt", masterTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToTextFile(String filename, ObservableList<MasterTable> students)
            throws IOException {


        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filename)))) {
            String identifier1 = String.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", "ID", "YEAR", "MONTH", "DAY", "X", "Y", "PM25");
            bw.write(identifier1);

            for (MasterTable student : students) {
                bw.write(String.format("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", student.getTableID(), student.getYear(), student.getMonth(), student.getDay(), student.getXcol(), student.getYcol(), student.getPms()));
            }


        }

        File file = new File(filename);

        Desktop desktop = Desktop.getDesktop();
        // if(file.exists()) desktop.open(file);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dialog.fxml"));
        DialogController controller = loader.getController();
        Dialog dialog = new Dialog<>();
        dialog.setHeaderText("Impt! Your input data set MUST have a format of (id, [time], x, y, measurement).\n\nBefore starting the import process please, define your [Time] Domain Filter below.");
        dialog.setContentText("Select Time Domain");
        DialogPane confirmationDialogView = null;
        try {
            dialog.setDialogPane(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.showAndWait();

        filepathInitialData = DialogModel.getInstance().getInitialFilePath().getText();
        filepathInterpolateData = DialogModel.getInstance().getfilepathInitialData().getText();
        selected = (String) DialogModel.getInstance().gettimeDomain().getValue();
        locationDataSetImport(filepathInterpolateData);
    }

    @FXML
    private void handleComboBoxAction(ActionEvent event) {
    }

    @FXML
    private void interpolate(ActionEvent actionEvent) {
        browseFile();
    }
}
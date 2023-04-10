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
import static java.util.Map.entry;

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
    private Object selected = "Day";
    private String timeF = "";
    private ObservableList<gis.app.MasterTable> masterTable = FXCollections.observableArrayList();

    // Create a HashMap object called capitalCities
    private HashMap<String, Location> capitalCities = new HashMap<String, Location>();

    // Dict to encode month and day into just day
    final private int[][] MONTH_DICT = {{1, 0}, {2, 31}, {3, 59}, {4, 90}, {5, 120}, {6, 151},
                                       {7, 181}, {8, 212}, {9, 243}, {10, 273}, {11, 304}, {12, 334}};

    /*This is a static HashMap that tracks all the locations that must be solved for, aka County

      String - "x, y" where x and y are the x and y values of the location.
      Location - Location class object
    */
    private static HashMap<String, Location> locationsToSolve = new HashMap<>();
    /*This is a static HashMap that tracks the locations and the data given for them, aka PM2009

      String - "x, y" where x and y are the x and y values of the location.
      Location - Location class object
    */
    private static HashMap<String, Location> locationDataGiven = new HashMap<>();


    // what the output looks like FOR THE GENERAL ARRAY WITH LOCATION CLASS
   /* Parent Key=-85.818021, 39.807323 [ {Month Key=1, value=x = -85.818021   y = 39.807323
    Day/Measurement = [{1=0.0, 2=0.0, 3=0.0, 4=0.0, 5=0.0, 6=0.0, 7=0.0, 8=0.0, 9=0.0, 10=0.0, 11=0.0, 12=0.0, 13=0.0, 14=0.0, 15=0.0, 16=0.0, 17=0.0, 18=0.0, 19=0.0, 20=0.0, 21=0.0, 22=0.0, 23=0.0, 24=0.0, 25=0.0, 26=0.0, 27=0.0, 28=0.0, 29=0.0, 30=0.0, 31=0.0}] }]*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        /* EVERYTHING IN THE INITIALIZER IS TO JUST FIGURE OUT HOW TO GET PM2009 INTO A MATRIX A*/
        //Path initialDataSetPath = Paths.get("C:\\Users\\ksweat\\Downloads\\pm25_2009_measured.txt");

        // get array of dates
        LocalDate ld = LocalDate.of(2009, Month.JANUARY, 1);
        LocalDate endDate = ld.plusYears(1);

        List<LocalDate> workDays = new ArrayList<>(365);

        while (ld.isBefore(endDate)) {
            workDays.add(ld);
            ld = ld.plusDays(1);
        }

        Double counter = 0.0;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> formats = workDays.stream().map(value -> value.format(format)).collect(Collectors.toList());
        for (String value : formats) {
            // Parses the date
            LocalDate dt = LocalDate.parse(value);
            int d = dt.getDayOfMonth();
            Month m = dt.getMonth();
            String param = m.toString() + Integer.toString(d);
            counter++;
            // TODO: This was broken by locationDataGiven refactor below, this needs to be fixed.
            //locationDataGiven.put(param, counter);
        }

        //System.out.println(pm2009);

        ///dataPoints.get("-86.501121, 32.500172").get(1).setDay(1, 2.0);
        /* loop through initial data input set */
        /*
        try (BufferedReader br2 = Files.newBufferedReader(initialDataSetPath,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line2 = br2.readLine();
            // start loop at 2nd line, skipping first line

            // row incrementer to set the matrix row, used of pm2009
            int row = 0;
            // find and set the total number of rows in pm2009
            long lines = 0;
            lines = Files.lines(initialDataSetPath).count();
            Integer lineLength = new Integer((int) lines);

            // pm2009 matrix
            double[][] A = new double[lineLength][4];
            //country matrix
            double[][] B = new double[lineLength][3];

            while ((line2 = br2.readLine()) != null) {
                String[] attributes2 = line2.split("\\s+");
                String dataSourceKey2 = attributes2[4] + ", " + attributes2[5];
                int month2 = Integer.parseInt(attributes2[2]);
                int day2 = Integer.parseInt(attributes2[3]);
                Double pm25 = Double.parseDouble(attributes2[6]);
               String currentMonth = Month.of(month2).name();
               String newParam = currentMonth.toString() + Integer.toString(day2);

                //A.add({attributes2[4], attributes2[5], attributes2[6]});
                // TODO: This was broken by locationDataGiven refactor below, this needs to be fixed.
                //A[row][0] = locationDataGiven.get(newParam);
                A[row][1] = Double.valueOf(attributes2[4]);
                A[row][2] = Double.valueOf(attributes2[5]);
                A[row][3] = pm25;

                row++;

            }

            // print data of each row
            for (double[] eachRow : A) {
               System.out.println(Arrays.toString(eachRow));
            }
            //System.out.println("A");
           // System.out.println(A[0][3]);


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }



        */
        importBtn.fireEvent(new ActionEvent());
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
        List<MasterTable> metric = readToSolveLocations(this.filepathInterpolateData);
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

    /*THIS IS READING THE COUNTRY.TXT FILE AND CREATING ALL THE ROWS AND COLUMNS AND SETTING UP THE LOCATION CLASS*/
    private List<MasterTable> readToSolveLocations(String fileName) {
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

        // loop through country.txt file
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.UTF_16LE)) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                String[] attributes = line.split("\\s+");
                Double x = Double.valueOf(attributes[1]);
                Double y = Double.valueOf(attributes[2]);
                String dataSourceKey = attributes[1] + ", " + attributes[2];


                Location newLoc = new Location(x, y, Integer.parseInt(attributes[0]));
                locationsToSolve.put(dataSourceKey, newLoc);

                lineCount++;


            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //make sure row length and hashmap length match, to verify all points are being imported
        //System.out.println(dataPoints.size());
        //System.out.println(lineCount);


        /*Loop Through add the params to the MasterTable Class,
        this is how I am adding id, year, month, day, x, y, pm to the table gui
        and then looping through and writing it to text file */
        for (Map.Entry<String, Location> entryA : locationsToSolve.entrySet()) {
            String[] attr = new String[7];

            // get the xy key
            String xyKey = entryA.getKey();
            // get the month hashmap associated with xy
            Location locObj = entryA.getValue();
            //System.out.println("Parent XY Key=" + xyKey);

            // print out hashmap for key month = obj location association
            // month key is the month points were taken
            //System.out.println("Month Key=" + monthKey + ", value=" + value2.showData() );
            // set id
            attr[0] = Integer.toString(locObj.getID());
            // set year
            // TODO: Can't assume year is always 2009, fix in the future
            attr[1] = "2009"; // pass in year someway


            // loop through the days to set day, month and pms
            // TODO: This only handles days currently, should handle months and years in future. Also does not account
            //  for leap years.
            for (int i = 1; i < 366; i++) {
                // Decodes day value (1-365) into [month, day]
                int[] decodedMonthDay = decodeMonthDay(i);

                // set month
                attr[2] = Integer.toString(decodedMonthDay[0]);

                // set day
                attr[3] = Integer.toString(decodedMonthDay[1]);
                // set pm25, either by getting an existing value or leaving it blank to indicate no data
                attr[6] = locObj.getDayValueDictionary().containsKey(i) ? Double.toString(locObj.getDay(i)) : "0.0";

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


        sort(metricsss);
        return metricsss;
    }

    /* This method reads in the data for the locations already given and stores them into locationDataGiven.
    * This method does not interact with the GUI at all
    */
    private void readDataGivenLocations(String fileName) {
        Path pathToFile = Paths.get(fileName);

        // Reads file
        try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
            String line = br.readLine();

            // Continues reading until no more lines exist
            while ((line = br.readLine()) != null) {
                // Splits line based on regex
                String[] attributes = line.split("\\s+");

                // Variables retrieved
                // TODO: Needs to handle year eventually
                int id = Integer.parseInt(attributes[0]);
                int month = Integer.parseInt(attributes[2]);
                int day = Integer.parseInt(attributes[3]);
                double x = Double.parseDouble(attributes[4]);
                double y = Double.parseDouble(attributes[5]);
                double pm25 = Double.parseDouble(attributes[6]);

                String key = attributes[4] + ", " + attributes[5];

                // Checks if the HashMap already has Location. If not, create the location.
                if (!locationDataGiven.containsKey(key)) {
                    Location newLoc = new Location(x, y, id);
                    locationDataGiven.put(key, newLoc);
                }

                // Converts month and day to just day
                int newDay = transcribeMonthDay(day, month);

                // Adds new data point to location object
                locationDataGiven.get(key).setDay(newDay, pm25);

                line = br.readLine();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // sort table
    public static void sort(List<MasterTable> list) {
        list.sort((o1, o2)
                -> Integer.valueOf(o1.getTableID()).compareTo(
                Integer.valueOf(o2.getTableID()))
        );
    }

    // Converts month and day to just a day between 1-365.
    private int transcribeMonthDay(int day, int month) {
        for (int[] array : MONTH_DICT) {
            if (array[0] == month) {
                return array[1] + day;
            }
        }

        // If the month somehow does not match
        return -1;
    }

    // Decodes a month and day from a transcribed day in transcribeMonthDay
    private int[] decodeMonthDay(int day) {
        for (int i = 11; i >= 0; i--) {
            if (day > MONTH_DICT[i][1]) {
                return new int[]{MONTH_DICT[i][0], day - MONTH_DICT[i][1]};
            }
        }

        // If the month somehow does not match
        return new int[]{-1, -1};
    }

    public static List<Date> getDaysBetweenDates(Date startDate, Date endDate){
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDate);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDate);

        while(cal1.before(cal2) || cal1.equals(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
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

        // This sets the data that needs to be interpolated both for the GUI and the locationsToSolve HashMap
        locationDataSetImport(filepathInterpolateData);

        // This sets the locationDataGiven HashMap
        readDataGivenLocations(filepathInitialData);
    }

    @FXML
    private void handleComboBoxAction(ActionEvent event) {
    }

    @FXML
    private void interpolate(ActionEvent actionEvent) {
        browseFile();
    }


}

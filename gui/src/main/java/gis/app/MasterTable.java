package gis.app;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.math.*;

public class MasterTable{

    private SimpleIntegerProperty tableID;
    private SimpleIntegerProperty Year;
    private SimpleIntegerProperty Month;
    private SimpleIntegerProperty Day;
    private SimpleDoubleProperty xCol;
    private SimpleDoubleProperty yCol;
    private SimpleDoubleProperty pms;


    public MasterTable(Integer tableID, Integer Year, Integer Month,Integer Day, Double xCol, Double yCol, Double pms) {
        this.tableID = new SimpleIntegerProperty(tableID);
        this.Year = new SimpleIntegerProperty(Year);
        this.Month = new SimpleIntegerProperty(Month);
        this.Day = new SimpleIntegerProperty(Day);

        this.xCol = new SimpleDoubleProperty(xCol);
        this.yCol = new SimpleDoubleProperty(yCol);
        this.pms = new SimpleDoubleProperty(pms);

    }



    public int getTableID() {
        return tableID.get();
    }

    public void setTableID(int tableID) {
        this.tableID = new SimpleIntegerProperty(tableID);
    }

    public int getYear() {
        return Year.get();
    }

    public void setYear(int Year) {
        this.Year = new SimpleIntegerProperty(Year);
    }

    public int getMonth() {
        return Month.get();
    }

    public void setMonth(int Month) {
        this.Month = new SimpleIntegerProperty(Month);
    }


    public int getDay() {
        return Day.get();
    }

    public void setDay(int Day) {
        this.Day = new SimpleIntegerProperty(Day);
    }


    public Double getXcol() {
        return xCol.get();
    }

    public void setXcol(Double xCol) {
        this.xCol = new SimpleDoubleProperty(xCol);
    }

    public Double getYcol() {
        return yCol.get();
    }

    public void setYcol(Double yCol) {
        this.yCol = new SimpleDoubleProperty(yCol);
    }

    public Double getPms() {
        return pms.get();
    }

    public void setPms(Double pms) {
        this.pms = new SimpleDoubleProperty(pms);
    }


}
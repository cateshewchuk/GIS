package gis.app;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MasterTable {

    private SimpleIntegerProperty tableID;
    private SimpleStringProperty time;
    private SimpleStringProperty xCol;
    private SimpleStringProperty yCol;
    private SimpleStringProperty pms;


    public MasterTable(Integer tableID, String time, String xCol, String yCol, String pms) {
        this.tableID = new SimpleIntegerProperty(tableID);
        this.time = new SimpleStringProperty(time);
        this.xCol = new SimpleStringProperty(xCol);
        this.yCol = new SimpleStringProperty(yCol);
        this.pms = new SimpleStringProperty(pms);

    }

    public int getTableID() {
        return tableID.get();
    }

    public void setTableID(int tableID) {
        this.tableID = new SimpleIntegerProperty(tableID);
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time = new SimpleStringProperty(time);
    }

    public String getXcol() {
        return xCol.get();
    }

    public void setXcol(String xCol) {
        this.xCol = new SimpleStringProperty(xCol);
    }

    public String getYcol() {
        return yCol.get();
    }

    public void setYcol(String yCol) {
        this.yCol = new SimpleStringProperty(yCol);
    }

    public String getPms() {
        return pms.get();
    }

    public void setPms(String pms) {
        this.pms = new SimpleStringProperty(pms);
    }


}
package com.elena;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MusicCatalogDataModel extends AbstractTableModel {

    private int rowCount = 0;
    private int colCount = 0;
    private ResultSet resultSet;

    public MusicCatalogDataModel(ResultSet rs) {
        this.resultSet = rs;
        setup();
    }

    private void setup(){

        countRows();

        try{
            colCount = resultSet.getMetaData().getColumnCount();

        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }

    }


    public void updateResultSet(ResultSet newRS){
        resultSet = newRS;
        setup();
    }


    private void countRows() {
        rowCount = 0;
        try {
            resultSet.beforeFirst();
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while (resultSet.next()) {
                rowCount++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }
    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount(){
        return colCount;
    }

    @Override
    public Object getValueAt(int row, int col){
        try{
            resultSet.absolute(row+1);
            Object o = resultSet.getObject(col+1);
            return o.toString();
        }catch (SQLException se) {
            System.out.println(se);
            return se.toString();

        }
    }


    //Delete row, return true if successful, false otherwise
    public boolean deleteRow(int row){
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            //Tell table to redraw itself
            fireTableDataChanged();
            return true;
        }catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }
    }


    //returns true if successful, false if error occurs
    public boolean insertRow(int barcode, String type, String title, String author, String status,
                             double price, int userID) {

        try {
            //Move to insert row, insert the appropriate data in each column, insert the row, move cursor back to where it was before we started
            resultSet.moveToInsertRow();
            resultSet.updateInt("Barcode", barcode);
            resultSet.updateString("Type", type);
            resultSet.updateString("Title", title);
            resultSet.updateString("Author", author);
            resultSet.updateDouble("Price", price);
            resultSet.updateString("Status", status);
            resultSet.updateInt("UserID", userID);
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            fireTableDataChanged();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding row");
            System.out.println(e);
            return false;
        }

    }

    @Override
    public String getColumnName(int col){
        try {
            return resultSet.getMetaData().getColumnName(col + 1);
        } catch (SQLException se) {
            System.out.println("Error fetching column names" + se);
            return "?";
        }
    }


}

package com.elena;
import java.sql.*;
public class MusicDatabase {

    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "music_library";
    private static final String USER = "Elena";
    private static final String PASS = "belochka2015";

    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;

    private static MusicDataModel MusicDataModel;

    /*public static void main(String args[]) {

        //setup creates database (if it doesn't exist), opens connection, and adds sample data

        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllMusic()) {
            System.exit(-1);
        }

        //If no errors, then start GUI
        MusicForm tableGUI = new MusicForm(MusicDataModel);

    }

    //Create or recreate a ResultSet containing the whole database, and give it to musicDataModel
    public static boolean loadAllItems(){

        try{

            if (rs!=null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM  CATALOG ";
            rs = statement.executeQuery(getAllData);

            if (MusicDataModel == null) {
                //If no current musicDataModel, then make one
                MusicDataModel = new MusicDataModel(rs);
            } else {
                //Or, if one already exists, update its ResultSet
                MusicDataModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading items.");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }*/

    public static boolean setup(){
        try {

            //Load driver class
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Quitting");
                return false;
            }

            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASS);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            if (!catalogTableExists()) {
                //Create a table in the database with 8 columns
                String createTableSQL = "CREATE TABLE IF NOT EXISTS CATALOG (" +
                        "Barcode long, Type varchar(10),Title varchar(50), Author varchar(50), " +
                        "Price double,Status varchar(50), Due_Date Date, CusotmerID long))";
                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);

                System.out.println("Created catalog table");
                //adding some data
                String addDataSQL = "INSERT INTO CATALOG VALUES (31972110254029, 'DVD','Karmen', 'Bize',12.5," +
                        "'CKO', DATE (STR_TO_DATE ('12/28/2015','%m/%d/%Y'), 00257122)";  //DATE(STR_TO_DATE(date_field, '%m/%d/%Y'
                statement.executeUpdate(addDataSQL);
                String addDataSQL = "INSERT INTO CATALOG VALUES (31972110254030, 'CD','25', 'Adele',19.99, 'Checked In',NULL , NULL )";
                statement.executeUpdate(addDataSQL);
                String addDataSQL = "INSERT INTO CATALOG VALUES (31972133254029, 'Book','How to start famous and avoid drugs: true story.'," +
                        " 'Author, Famous',49.99,'Lost', NULL ,NULL )";
                statement.executeUpdate(addDataSQL);
                String addDataSQL = "INSERT INTO CATALOG VALUES (31972133904029,'Audio Tape','Sounds of Ocean', 'Ocean'," +
                        "15.99,'Damaged', NULL, NULL')";
                statement.executeUpdate(addDataSQL);
                String addDataSQL = "INSERT INTO CATALOG VALUES (31972763904029,'CD','Sound of Rain', 'Rain'," +
                        "5.99,'CKO', DATE (STR_TO_DATE ('12/31/2015','%m/%d/%Y'), 7654327)";
                statement.executeUpdate(addDataSQL);
                String addDataSQL = "INSERT INTO CATALOG VALUES (31972982888333,'CD','Russian Songs', 'Pugacheva, Alla'" +
                        "19.99,'Checked In', NULL,NULL)";
                statement.executeUpdate(addDataSQL);

            }
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean movieTableExists() throws SQLException {

        String checkTablePresentQuery = "SHOW TABLES LIKE CATALOG";   //Can query the database schema
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next()) {    //If ResultSet has a next row, it has at least one row... that must be our table
            return true;
        }
        return false;

    }

    //Close the ResultSet, statement and connection, in that order.
    public static void shutdown() {
        try {
            if (rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se) {
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }



}


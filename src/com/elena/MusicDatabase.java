package com.elena;
import java.sql.*;

// This is code is based on the MovieRatings example provided by Clara. Modified by Elena R.
//
// This program is app for librarians to set  music items catalog
// In future, it can be extended to have a database table - Users and use Users to track items borrowed by users
// (refer to the comments in code: for creating a Users  table and adding initial records)
// The current code allows to add and delete records for music items.
//
// Each music record contains the following data:
// item barcode - 10-digit number
// type - type of music item: CD/DVD/Book/Music notes
// title of the item
// author of the item (if does not apply, use 'NA')
// item price
// status - Lost/Damaged/new/Check In/Check out
// userId - set for future features; it will allow to connect catalog items with users borrowing the item
//
//final project
public class MusicDatabase {

    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "music_library";  // the database name
    private static final String USER = "Elena";
    private static final String PASS = "belochka2015";

    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;


    public static MusicCatalogDataModel musicCatalogDataModel;
    public static MusicUsersDataModel musicUsersDataModel;  // will be used in future feature with Users table

    public static void main(String args[]) {

        //creates database (if it doesn't exist), opens connection, and adds sample data
        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllCatalogItems())
              //  || !loadAllUsersItems())   // this code will be used when Users table is added to the app
        {
            System.exit(-1);
        }

        //If no errors, start GUI
        try {
            MusicLibraryForm tableGUI = new MusicLibraryForm(musicCatalogDataModel); // will use GUI for Catalog table
        }
        catch ( Exception ex)
        {

        }
    }


    //Create or recreate a ResultSet containing the Catalog table data, and give it to musicDataModel
    public static boolean loadAllCatalogItems() {

        try {

            if (rs != null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM  CATALOG";
            rs = statement.executeQuery(getAllData);

            if (musicCatalogDataModel == null) {
                //If no current musicDataModel, then make one
                musicCatalogDataModel = new MusicCatalogDataModel(rs);
            } else {
                //Or, if one already exists, update its ResultSet
                musicCatalogDataModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading items.");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }

    /*  ---------------------- this code will be used for future feature with Users table
    //Create or recreate a ResultSet containing the whole database, and give it to musicDataModel
    public static boolean loadAllUsersItems() {

        try {

            if (rs != null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM  USERS";
            rs = statement.executeQuery(getAllData);

            if (musicUsersDataModel == null) {
                //If no current musicDataModel, then make one
                musicUsersDataModel = new MusicUsersDataModel(rs);
            } else {
                //Or, if one already exists, update its ResultSet
                musicUsersDataModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading items.");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }

----------------------------------------------------------    */
    public static boolean setup() {
        try {

            //Load driver class
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Will exit.");
                return false;
            }

            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASS);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            if (!tableExists("catalog")) {
                //Create Catalog table in the database: catalog with barcode as a primary key
                //Note: userId is set to 0 indicating that no user is associated with this item
                String createTableSQL = "CREATE TABLE catalog (" +
                        "Barcode int, Type varchar(20), Title varchar(50), Author varchar(50), " +
                        "Price double, Status varchar(50), userID int, PRIMARY KEY(Barcode))";

                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);
                System.out.println("Created catalog table");

                //adding initial data
                String addDataSQL = "INSERT INTO CATALOG VALUES (1234564029, 'DVD','Karmen', 'Bize', 12.5, 'Checked Out', 0)";
                statement.executeUpdate(addDataSQL);

                addDataSQL = "INSERT INTO CATALOG VALUES (1234564030, 'CD','25', 'Adele',19.99, 'Checked In', 0 )";
                statement.executeUpdate(addDataSQL);

                addDataSQL = "INSERT INTO CATALOG VALUES (1234564031, 'Book','How to start famous.'," +
                        " 'Author, Famous', 49.99,'Lost', 0 )";
                statement.executeUpdate(addDataSQL);

                addDataSQL = "INSERT INTO CATALOG VALUES (1234564032,'Audio Tape','Sounds of Ocean', 'Ocean'," +
                        "15.99,'Damaged', 0)";
                statement.executeUpdate(addDataSQL);

                addDataSQL = "INSERT INTO CATALOG VALUES (1234564033,'CD','Sound of Rain', 'Rain'," +
                        "5.99,'CKO', 0)";
                statement.executeUpdate(addDataSQL);

                addDataSQL = "INSERT INTO CATALOG VALUES (1234564034,'CD','Russian Songs', 'Pugacheva, Alla'," +
                        "19.99,'Checked In', 0)";
                statement.executeUpdate(addDataSQL);

                addDataSQL = "INSERT INTO CATALOG VALUES (1234564035,'DVD','Folk Song', 'Italian Folk Band'," +
                        "26.09,'New', 0)";
                statement.executeUpdate(addDataSQL);
            }

/* ----------------------------------- this code is ready to be used when adding Users feature
            //Create a table in the database: USERS with user_id as a primary key
            if (!tableExists("users")) {
                String createTableSQL = "CREATE TABLE USERS( userID integer auto_increment primary key, " +
                        "userBarcode long, firstname varchar(50), lastname varchar(50),fines float)";

                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);
                System.out.println("Created users table");

                //add some data into users table
                String addDataSQL = " INSERT INTO USERS (userBarcode, firstname, lastname, fines) values (123456,'Harry', 'Potter', 0.0)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = " INSERT INTO USERS (userBarcode, firstname, lastname, fines) values (234569,'Bob', 'Sponge', 0.0)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = " INSERT INTO USERS (userBarcode, firstname, lastname, fines) values (876540,'Peppa', 'Pig', 50.00)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = " INSERT INTO USERS (userBarcode, firstname, lastname, fines) values (927264,'Cinderella', 'Princess', 0.0)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = " INSERT INTO USERS (userBarcode, firstname, lastname, fines) values (131415,'Anna', 'Smith', 0.0)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = " INSERT INTO USERS (userBarcode, firstname, lastname, fines) values (927254,'John', 'Smith', 0.0)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = " INSERT INTO USERS (userBarcode, firstname, lastname, fines) values (9333264,'George', 'Clooney', 0.0)";
                statement.executeUpdate(addDataSQL);


            }
            -----------------------------------------           */
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    // checks if table already exist
    private static boolean tableExists(String tableName) throws SQLException {

        String checkTablePresentQuery = "SHOW TABLES LIKE '" + tableName + "'";
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next()) {
            return true;
        }
        return false;

    }

    //Close the ResultSet, statement and connection, in that order.
    public static void shutdown(){
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
        } catch (SQLException se){
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
}


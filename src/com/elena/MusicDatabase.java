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

    public static MusicCatalogDataModel musicCatalogDataModel;
    public static MusicUsersDataModel musicUsersDataModel;

    public static void main(String args[]) {

        //setup creates database (if it doesn't exist), opens connection, and adds sample data

        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllItems()) {
            System.exit(-1);
        }

        //If no errors, then start GUI
        try {
            MusicFormGUI tableGUI = new MusicFormGUI(musicUsersDataModel);//musicCatalogDataModel);//, musicUsersDataModel);
        }
        catch ( Exception ex)
        {

        }
    }

    //Create or recreate a ResultSet containing the whole database, and give it to musicDataModel
    public static boolean loadAllItems() {

        try {

            if (rs != null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM  CATALOG ";
            rs = statement.executeQuery(getAllData);

            if (musicCatalogDataModel == null) {
                //If no current musicDataModel, then make one
                musicCatalogDataModel = new MusicCatalogDataModel(rs);
            } else {
                //Or, if one already exists, update its ResultSet
                musicCatalogDataModel.updateResultSet(rs);
            }

            getAllData = "SELECT * FROM  USERS ";
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

    public static boolean setup() {
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

            // The first argument ResultSet.TYPE_SCROLL_INSENSITIVE
            // allows us to move the cursor both forward and backwards through the RowSet
            // we get from this statement.

            // Another option is TYPE_SCROLL_SENSITIVE, which means the ResultSet will be updated when
            // something *else* changes the database. If your DB server was shared, you might need to be concerned about this.)

            // The TableModel will need to go forward and backward through the ResultSet.
            // by default, you can only move forward - it's less
            // resource-intensive than being able to go in both directions.
            // If you set one argument, you need the other.
            // The second one (CONCUR_UPDATABLE) means you will be able to change the ResultSet and these
            // changes will be made to the DB.... so long as you have a table with a primary key in it. (Otherwise
            // your database isn't able to definitively identify what has been changed).
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Does the table exist? If not, create it.
            if (!tableExists("catalog")) {
                //Create a table in the database with 3 columns: Movie title, year and rating
                String createTableSQL = "CREATE TABLE catalog (" +
                        "Barcode int, Type varchar(20), Title varchar(50), Author varchar(50), " +
                        "Price double, Status varchar(50), Due_Date Date, userID long, PRIMARY KEY(Barcode))";

                System.out.println(createTableSQL);
                //statement.executeUpdate(createTableSQL);

                System.out.println("Created catalog table");

                //adding some data
                String addDataSQL = "INSERT INTO CATALOG VALUES (4029, 'DVD','Karmen', 'Bize',12.5," +
                        "'CKO', NOW()+21,'')";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO CATALOG VALUES (4030, 'CD','25', 'Adele',19.99, 'Checked In',0 , 0 )";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO CATALOG VALUES (4031, 'Book','How to start famous and avoid drugs: true story.'," +
                        " 'Author, Famous',49.99,'Lost', 0 ,'' )";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO CATALOG VALUES (4032,'Audio Tape','Sounds of Ocean', 'Ocean'," +
                        "15.99,'Damaged', 0, 0)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO CATALOG VALUES (4033,'CD','Sound of Rain', 'Rain'," +
                        "5.99,'CKO', 0,0)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO CATALOG VALUES (4034,'CD','Russian Songs', 'Pugacheva, Alla'," +
                        "19.99,'Checked In', 0,0)";
                statement.executeUpdate(addDataSQL);
            }

            //Does the table exist? If not, create it.
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
                addDataSQL = " INSERT INTO USERS (userBarcode, firstname, lastname, fines) values (876540,'Peppa', 'Pig', 0.0)";
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

            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean tableExists(String tableName) throws SQLException {

        String checkTablePresentQuery = "SHOW TABLES LIKE '" + tableName + "'";   //Can query the database schema
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next()) {    //If ResultSet has a next row, it has at least one row... that must be our table
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


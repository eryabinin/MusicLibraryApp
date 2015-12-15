package com.elena;
import java.sql.*;
//import java.util.Date;


public class MusicDatabase {

    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "music_library";
    private static final String USER = "Elena";   //"Elena"
    private static final String PASS = "belochka2015";


    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;


    public static MusicCatalogDataModel musicCatalogDataModel;
  //  public static MusicUsersDataModel musicUsersDataModel;

    public static void main(String args[]) {

        //setup creates database (if it doesn't exist), opens connection, and adds sample data

        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllCatalogItems())
              //  || !loadAllUsersItems())
        {
            System.exit(-1);
        }

        //If no errors, then start GUI
        try {
            MusicLibraryForm tableGUI = new MusicLibraryForm(musicCatalogDataModel);//musicCatalogDataModel);//, musicUsersDataModel);
        }
        catch ( Exception ex)
        {

        }
    }


    //Create or recreate a ResultSet containing the whole database, and give it to musicDataModel
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

    /*
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

*/
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
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            if (!tableExists("catalog")) {
                //Create a table in the database: catalog with barcode as a primary key
                String createTableSQL = "CREATE TABLE catalog (" +
                        "Barcode int, Type varchar(20), Title varchar(50), Author varchar(50), " +
                        "Price double, Status varchar(50), userID int, PRIMARY KEY(Barcode))";

                System.out.println(createTableSQL);
                statement.executeUpdate(createTableSQL);
                System.out.println("Created catalog table");

                //adding some data

                String addDataSQL = "INSERT INTO CATALOG VALUES (1234564029, 'DVD','Karmen', 'Bize', 12.5, 'CKO', 0)";
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
                        "26.09,'Checked In', 0)";
                statement.executeUpdate(addDataSQL);
            }

/*
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
            */
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

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


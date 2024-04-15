package core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Map;

public class DBConnect {

    private String username;
    private String password;
    private static Connection con;
    public DBConnect(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        this.con = DriverManager.getConnection(
                "jdbc:mysql://smcse-stuproj00.city.ac.uk:3306/in2033t09", //"jdbc:mysql://localhost:3306/mydatabase",
                username,
                password);
        System.out.println("Success");
    }


   public static void uploadMockMenu(Map<Integer, String[]> menu) throws SQLException {
        Statement stmt = con.createStatement();
        //ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Dish");
        disableForeignKeyChecks(con);
        String dropTableQuery = "DROP TABLE IF EXISTS Dish";
        stmt.executeUpdate(dropTableQuery);
        System.out.println("Dish Tables dropped successfully.");
        MockData.addMenuData();
        MockData.createMenu();
        MockData.addWines();

        // Recreate the Dish table
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Dish (" +
                "id INT NOT NULL PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "price VARCHAR(255)," +
                "description VARCHAR(255)," +
                "allergens VARCHAR(255)" +
                ")";
        stmt.executeUpdate(createTableQuery);
        enableForeignKeyChecks(con);
        System.out.println("Dish Table created successfully.");

        for (Map.Entry<Integer, String[]> entry : menu.entrySet()) {
            Integer id = entry.getKey();
            String[] data = entry.getValue();
            String name = data[0];
            String price = data[1];
            String description = data[2];
            String allergens = data[3];

            String sql = "INSERT INTO Dish (id, name, price, description, allergens) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, price);
                pstmt.setString(4, description);
                pstmt.setString(5, allergens);

                // Execute the INSERT statement
                System.out.println("Dish Data uploaded successfully!");
                pstmt.executeUpdate();
            }   catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void uploadWineMenu(Map<Integer, String[]> menu) throws SQLException {
        Statement stmt = con.createStatement();
        //ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Dish");
        disableForeignKeyChecks(con);
        String dropTableQuery = "DROP TABLE IF EXISTS Wine";
        stmt.executeUpdate(dropTableQuery);
        System.out.println("Wine Tables dropped successfully.");
        MockData.addMenuData();
        MockData.createMenu();
        MockData.addWines();

        // Recreate the Dish table
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Wine (" +
                "id INT NOT NULL PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "price VARCHAR(255)," +
                "description VARCHAR(255)," +
                "vintage VARCHAR(255)" +
                ")";
        stmt.executeUpdate(createTableQuery);
        enableForeignKeyChecks(con);
        System.out.println("Wine Table created successfully.");

        for (Map.Entry<Integer, String[]> entry : menu.entrySet()) {
            Integer id = entry.getKey();
            String[] data = entry.getValue();
            String name = data[0];
            String price = data[1];
            String description = data[2];
            String vintage = data[3];

            String sql = "INSERT INTO Wine (id, name, price, description, vintage) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, price);
                pstmt.setString(4, description);
                pstmt.setString(5, vintage);

                // Execute the INSERT statement
                System.out.println("Data uploaded successfully!");
                pstmt.executeUpdate();
            }   catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private static void disableForeignKeyChecks(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");
        }
    }

    private static void enableForeignKeyChecks(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");
        }
    }

    public static String login(String password) {

        try {
            String query1 = "SELECT * FROM Staff WHERE Password = ? AND EndOfContract IS NULL";
            PreparedStatement statement1 = con.prepareStatement(query1);
            statement1.setString(1, password);
            ResultSet resultSet1 = statement1.executeQuery();
            String username;

            if (resultSet1.next()) {
                username = resultSet1.getString("Name");
            } else {
                username = null;
            }

            statement1.close();
            resultSet1.close();

            String query2 = "SELECT Role FROM Staff WHERE Name = ? AND EndOfContract IS NULL";
            PreparedStatement statement2 = con.prepareStatement(query2);
            statement2.setString(1, username);
            ResultSet resultSet2 = statement2.executeQuery();

            String role;

            if (resultSet2.next()) {
                role = resultSet2.getString("Role");
            } else {
                role = null;
            }


            statement2.close();
            resultSet2.close();

            return username + ":" + role;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList getAllRoleName(){

        ObservableList<String> allRoleNames = FXCollections.observableArrayList();

        try{
            String query1 = "SELECT Name,Role from Staff WHERE EndOfContract IS NULL";
            Statement statement1 = con.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(query1);

            while (resultSet1.next()){
                String name = resultSet1.getString("Name");
                String role = resultSet1.getString("Role");

                allRoleNames.add(role + " " + name);
            }
            return allRoleNames;

        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

<<<<<<< Updated upstream
    public static void uploadMockMenu(Map<Integer, String[]> menu) throws SQLException {
        Statement stmt = con.createStatement();
        //ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Dish");
        disableForeignKeyChecks(con);
        String dropTableQuery = "DROP TABLE IF EXISTS Dish";
        stmt.executeUpdate(dropTableQuery);
        System.out.println("Tables dropped successfully.");
        MockData.addMenuData();
        MockData.createMenu();
        MockData.addWines();

        // Recreate the Dish table
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Dish (" +
                "id INT NOT NULL PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "price VARCHAR(255)," +
                "description VARCHAR(255)," +
                "allergens VARCHAR(255)" +
                ")";
        stmt.executeUpdate(createTableQuery);
        enableForeignKeyChecks(con);
        System.out.println("Table created successfully.");

        for (Map.Entry<Integer, String[]> entry : menu.entrySet()) {
            Integer id = entry.getKey();
            String[] data = entry.getValue();
            String name = data[0];
            String price = data[1];
            String description = data[2];
            String allergens = data[3];

            String sql = "INSERT INTO Dish (id, name, price, description, allergens) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, price);
                pstmt.setString(4, description);
                pstmt.setString(5, allergens);

                // Execute the INSERT statement
                System.out.println("Data uploaded successfully!");
                pstmt.executeUpdate();
            }   catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
=======
//    public static boolean insertCSV(String... values){
//
//        try{
//            String query = "INSERT INTO Shift (StaffName,CreatedDate,ShiftDate,DayOfWeek) VALUES (?,?,?,?)";
//            return true;
//        }
//        catch (SQLException e){
//            e.printStackTrace();
//            return false;
//        }
//
//        // try run main class in out dir
//
//    }

}
>>>>>>> Stashed changes

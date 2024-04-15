package core;

import scenes.MockData;

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
        uploadMockMenu();
    }


    public static void uploadMockMenu() throws SQLException {
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
                "ID INT NOT NULL PRIMARY KEY," +
                "Name VARCHAR(255) NOT NULL," +
                "Type VARCHAR(255)," +
                "Collation VARCHAR(255)," +
                "Allergens VARCHAR(255)" +
                ")";
        stmt.executeUpdate(createTableQuery);
        enableForeignKeyChecks(con);
        System.out.println("Table created successfully.");

        for (Map.Entry<Integer, String[]> entry : MockData.menu.entrySet()) {
            Integer id = entry.getKey();
            String[] data = entry.getValue();
            String name = data[0];
            String price = data[1];
            String description = data[2];
            String allergens = data[3];

            String sql = "INSERT INTO Dish (ID, Name, Type, Collation, Allergens) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, "int(11)");
                pstmt.setString(4, "UTF8_GENERAL_CI");
                pstmt.setString(5, allergens);

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
            return null;
        }
    }
}

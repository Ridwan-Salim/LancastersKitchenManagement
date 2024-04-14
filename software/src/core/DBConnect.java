package core;

import java.sql.*;

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
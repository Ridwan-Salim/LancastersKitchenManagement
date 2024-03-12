import java.sql.*;

public class DBConnect {
    public Connection connectToAndQueryDatabase(String username, String password) throws SQLException {

        /*
        * 1) Download the mysql connector Martin put in week 3 resources and save it, then go to intellij
        * 2) File>Project Structure>Modules>Dependencies> + > [find the folder where you saved it and select]
        * 3) in the below call for the url section use "jdbc:mysql://smcse-stuproj00.city.ac.uk:3306/in2033t09"
        * 4) Username and password are in the email Martin sent - use that in the call in main
        * */
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydatabase",
                username,
                password);
        System.out.println("Success");
        // Example way to execute statements (Create some tables in the phpmyadmin first and modify)
        /*Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM dish");

        while (rs.next()) {
            int x = rs.getInt("id");
            String s = rs.getString("name");
            float f = rs.getFloat("price");
            System.out.println(x);
            System.out.println(s);
            System.out.println(f);
        }*/
        return con; // Returning it so we can use the same instance in other classes, remove if using same class for all SQL statements
    }
}
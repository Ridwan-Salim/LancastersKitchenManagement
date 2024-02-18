import java.sql.*;

public class DBConnect {
    public void connectToAndQueryDatabase(String username, String password) throws SQLException {

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

        // Example way to execute statements (Create some tables in the phpmyadmin first and modify)
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT a, b, c FROM Table1");

        while (rs.next()) {
            int x = rs.getInt("a");
            String s = rs.getString("b");
            float f = rs.getFloat("c");
            System.out.println(x);
            System.out.println(s);
            System.out.println(f);
        }
    }
}
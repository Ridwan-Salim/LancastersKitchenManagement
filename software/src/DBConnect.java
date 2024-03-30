import java.sql.*;

public class DBConnect {

    private String username;
    private String password;
    private Connection con;
    public DBConnect(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        this.con = DriverManager.getConnection(
                "jdbc:mysql://smcse-stuproj00.city.ac.uk:3306/in2033t09", //"jdbc:mysql://localhost:3306/mydatabase",
                username,
                password);
        System.out.println("Success");
    }
    public void query(String statement) throws SQLException {
        Statement stmt = this.con.createStatement();
        //ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM Dish");
        ResultSet rs = stmt.executeQuery(statement);

        while (rs.next()) {
            int x = rs.getInt("id");
            String s = rs.getString("name");
            float f = rs.getFloat("price");
            System.out.println("ID: " + x);
            System.out.println("Name: " + s);
            System.out.println("Price: " + f);
        }
    }
}
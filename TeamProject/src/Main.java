import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnect connect = new DBConnect();
        // This is where you put the credentials Martin gave in email, use admin for DDL perms
        //String adminUsername = "in2033t09_a";
        //String adminPassword = "C9byIeKbkE0";

        //connect to your local mySQL server
        String adminUsername = "root";
        String adminPassword = "";
        connect.connectToAndQueryDatabase(adminUsername, adminPassword);
    }
}
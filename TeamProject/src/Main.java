import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DBConnect connect = new DBConnect();
        // This is where you put the credentials Martin gave in email, use admin for DDL perms
        String adminUsername = "";
        String adminPassword = "";
        connect.connectToAndQueryDatabase(adminUsername, adminPassword);
    }
}
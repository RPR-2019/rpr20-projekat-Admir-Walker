package app.settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private static Database instance = null;
    private static Connection connection = null;

    private Database() throws SQLException {
        String connectionURL = "jdbc:mysql://localhost/filemanager?user=root&password=root&serverTimezone=GMT";
        connection = DriverManager.getConnection(connectionURL);
    }
    public static Database getInstance() throws SQLException {
        if(instance == null) instance = new Database();
        return instance;
    }
    public static void removeInstance() throws SQLException{
        if(instance == null) return;
        connection.close();
        instance = null;
    }

    public PreparedStatement addPreparedStatement(String statement) throws SQLException {
        return connection.prepareStatement(statement);
    }

}

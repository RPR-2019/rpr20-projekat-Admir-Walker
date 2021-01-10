package app.models;

import app.settings.Database;
import app.classes.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private static UserDAO instance;
    private static Database database;

    private PreparedStatement provjeriLogin;

    private UserDAO() throws SQLException {
        database = Database.getInstance();
        provjeriLogin = database.addPreparedStatement("select count(*) from user where email=? and password=?");
    }
    public static UserDAO getInstance() throws SQLException {
        if(instance == null) instance = new UserDAO();
        return instance;
    }
    public static void removeInstance() throws SQLException {
        if(instance == null) return;
        Database.removeInstance();
        instance = null;
    }

    public boolean provjeriLogin(User user){
        try {
            provjeriLogin.setString(1, user.getEmail());
            provjeriLogin.setString(2, user.getPassword());
            ResultSet resultSet = provjeriLogin.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1) == 1; // Ako je pronadjen user sa ovakvim podacima
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}

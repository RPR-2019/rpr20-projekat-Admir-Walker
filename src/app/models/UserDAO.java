package app.models;

import app.classes.User;
import app.classes.UserType;
import app.exceptions.UserTypeNotFoundException;
import app.settings.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private static UserDAO instance;
    private static Database database;

    private PreparedStatement provjeriLoginIDajPodatke;
    private PreparedStatement dajPodatkePrekoID;

    private UserDAO() throws SQLException {
        database = Database.getInstance();
        provjeriLoginIDajPodatke = database.addPreparedStatement("select * from user where email=? and password=?");
        dajPodatkePrekoID = database.addPreparedStatement("select * from user where userID = ?");
    }

    public static UserDAO getInstance() throws SQLException {
        if (instance == null) instance = new UserDAO();
        return instance;
    }

    public static void removeInstance() throws SQLException {
        if (instance == null) return;
        Database.removeInstance();
        instance = null;
    }

    public User regenerisiPodatke(ResultSet resultSet) {
        User user = new User();

        try {
            user.setId(resultSet.getInt(1));
            user.setFirstName(resultSet.getString(2));
            user.setLastName(resultSet.getString(3));
            user.setEmail(resultSet.getString(4));
            user.setUserType(UserType.of(resultSet.getInt(6)));
        } catch (SQLException | UserTypeNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public User provjeriLogin(User user) {
        try {
            provjeriLoginIDajPodatke.setString(1, user.getEmail());
            provjeriLoginIDajPodatke.setString(2, user.getPassword());
            ResultSet resultSet = provjeriLoginIDajPodatke.executeQuery();
            if (resultSet.next()) { // ako je nadjeno poklapanje maila i passworda, postavit cemo sve podatke
                user = regenerisiPodatke(resultSet);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User dajKorisnikaPrekoID(int id) {
        try {
            dajPodatkePrekoID.setInt(1, id);
            ResultSet resultSet = dajPodatkePrekoID.executeQuery();

            if (resultSet.next()) {
                return regenerisiPodatke(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

}

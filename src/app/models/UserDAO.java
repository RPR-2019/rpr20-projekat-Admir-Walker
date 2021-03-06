package app.models;

import app.classes.User;
import app.classes.UserType;
import app.exceptions.UserTypeNotFoundException;
import app.settings.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static UserDAO instance;
    private static Database database;

    private PreparedStatement checkLoginAndAssignData;
    private PreparedStatement assignDataViaID;
    private PreparedStatement fetchProfessors;

    private UserDAO() throws SQLException {
        database = Database.getInstance();
        checkLoginAndAssignData = database.addPreparedStatement("select * from user where email=? and password=?");
        assignDataViaID = database.addPreparedStatement("select * from user where userID = ?");
        fetchProfessors = database.addPreparedStatement("select * from user where userType = 3 order by firstName;");
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

    public User regenerateData(ResultSet resultSet) {
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

    public User checkLogin(User user) {
        try {
            checkLoginAndAssignData.setString(1, user.getEmail());
            checkLoginAndAssignData.setString(2, user.getPassword());
            ResultSet resultSet = checkLoginAndAssignData.executeQuery();
            if (resultSet.next()) { // ako je nadjeno poklapanje maila i passworda, postavit cemo sve podatke
                user = regenerateData(resultSet);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User fetchUserViaID(int id) {
        try {
            assignDataViaID.setInt(1, id);
            ResultSet resultSet = assignDataViaID.executeQuery();

            if (resultSet.next()) {
                return regenerateData(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
    public List<User> fetchProfessors(){
        try {
            List<User> professors = new ArrayList<>();
            ResultSet resultSet = fetchProfessors.executeQuery();
            while (resultSet.next()){
                professors.add(new User(
                   resultSet.getInt(1),
                   resultSet.getString(2),
                   resultSet.getString(3),
                   resultSet.getString(4),
                   "",
                   UserType.of(resultSet.getInt(6))
                ));
            }
            return professors;
        } catch (SQLException | UserTypeNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}

package app.models;

import app.classes.Subject;
import app.classes.User;
import app.settings.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    private static SubjectDAO instance;
    private static Database database;

    private PreparedStatement dajSvePredmete;

    private SubjectDAO() throws SQLException {
        database = Database.getInstance();
        dajSvePredmete = database.addPreparedStatement("select * from subject order by name asc");
    }

    public static SubjectDAO getInstance() throws SQLException {
        if (instance == null) instance = new SubjectDAO();
        return instance;
    }

    public static void removeInstance() throws SQLException {
        if (instance == null) return;
        Database.removeInstance();
        instance = null;
    }

    public Subject regenerisiPodatke(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int profID = resultSet.getInt(3);
            User professor = UserDAO.getInstance().dajKorisnikaPrekoID(profID);
            return new Subject(id, name, professor);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<Subject> dajListuSvihPredmeta() {
        try {
            ResultSet resultSet = dajSvePredmete.executeQuery();
            List<Subject> subjectList = new ArrayList<>();
            while (resultSet.next()) {
                Subject subject = regenerisiPodatke(resultSet);
                if (subject != null) {
                    subjectList.add(subject);
                }
            }
            return subjectList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}

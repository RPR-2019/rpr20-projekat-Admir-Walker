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

    private PreparedStatement fetchAllSubjects;
    private PreparedStatement addSubject;
    private PreparedStatement deleteSubject;
    private PreparedStatement updateSubject;

    private SubjectDAO() throws SQLException {
        database = Database.getInstance();
        fetchAllSubjects = database.addPreparedStatement("select * from subject order by name asc;");
        addSubject = database.addPreparedStatement("insert into subject(name,professor) values (?, ?);");
        deleteSubject = database.addPreparedStatement("delete from subject where subjectID = ?;");
        updateSubject = database.addPreparedStatement("update subject set name = ?, professor = ? where subjectID = ?;");
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

    public Subject regenerateData(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int profID = resultSet.getInt(3);
            User professor = UserDAO.getInstance().fetchUserViaID(profID);
            return new Subject(id, name, professor);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<Subject> fetchSubjects() {
        try {
            ResultSet resultSet = fetchAllSubjects.executeQuery();
            List<Subject> subjectList = new ArrayList<>();
            while (resultSet.next()) {
                Subject subject = regenerateData(resultSet);
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
    public void addSubject(Subject subject){
        try {
            addSubject.setString(1,subject.getName());
            addSubject.setInt(2, subject.getProfessorId());
            addSubject.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void delete(Subject subject) {
        try {
            deleteSubject.setInt(1, subject.getId());
            deleteSubject.execute();
            // Trebalo bi sve materijale vezane za ovaj predmet obrisati :)
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateSubject(Subject currentSubject) {
        try {
            updateSubject.setString(1, currentSubject.getName());
            updateSubject.setInt(2, currentSubject.getProfessorId());
            updateSubject.setInt(3, currentSubject.getId());

            updateSubject.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }
}

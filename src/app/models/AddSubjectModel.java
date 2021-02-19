package app.models;

import app.classes.Subject;
import app.classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddSubjectModel {
    private final UserDAO userDAO;
    private final SubjectDAO subjectDAO;

    public AddSubjectModel(UserDAO userDAO, SubjectDAO subjectDAO) {
        this.userDAO = userDAO;
        this.subjectDAO = subjectDAO;
    }
    public ObservableList<User> fetchProfessors(){
        return FXCollections.observableArrayList(userDAO.fetchProfessors());
    }
    public void addSubject(Subject subject){
        subjectDAO.addSubject(subject);
    }
}

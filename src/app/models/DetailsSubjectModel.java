package app.models;

import app.classes.Subject;
import app.classes.User;
import app.models.SubjectDAO;
import app.models.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DetailsSubjectModel {

    private final Subject currentSubject;
    private final SubjectDAO subjectDAO;
    private final UserDAO userDAO;

    public DetailsSubjectModel(Subject currentSubject, SubjectDAO subjectDAO, UserDAO userDAO) {
        this.currentSubject = currentSubject;
        this.subjectDAO = subjectDAO;
        this.userDAO = userDAO;
    }

    public Subject getCurrentSubject() {
        return currentSubject;
    }
    public String getSubjectID(){
        return ""+currentSubject.getId();
    }
    public String getSubjectName(){
        return currentSubject.getName();
    }
    public ObservableList<User> fetchProfessors(){
        return FXCollections.observableArrayList(userDAO.fetchProfessors());
    }
    public User getCurrentProfessor(){
        return currentSubject.getProfessor();
    }
    public void setName(String name){
        currentSubject.setName(name);
    }
    public void setProfessor(User professor){
        currentSubject.setProfessor(professor);
    }
    public void updateSubject() {
        subjectDAO.updateSubject(currentSubject);
    }
}

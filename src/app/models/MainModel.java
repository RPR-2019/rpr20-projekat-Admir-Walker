package app.models;

import app.classes.Subject;
import app.classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MainModel {
    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private final User user;
    private final SubjectDAO subjectDAO;

    public MainModel(User user, SubjectDAO subjectDAO) {
        this.user = user;
        this.subjectDAO = subjectDAO;
    }

    public void setSubjects(List<Subject> subjectList) {
        subjects = FXCollections.observableArrayList(subjectList);
    }

    public ObservableList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjectList(ObservableList<Subject> listaPredmeta) {
        this.subjects = listaPredmeta;
    }

    public User getUser() {
        return user;
    }

    public SubjectDAO getSubjectDAO() {
        return subjectDAO;
    }

    public ObservableList<Subject> search(String searchParam) {
        ObservableList<Subject> filteredSubjects = FXCollections.observableArrayList();
        subjects.forEach(subject -> {
            if (subject.isSubstring(searchParam)) {
                filteredSubjects.add(subject);
            }
        });
        return filteredSubjects;
    }
    public List<Subject> fetchSubjects(){
        List<Subject> subjectList = subjectDAO.fetchSubjects();
        subjects = FXCollections.observableArrayList(subjectList);
        return subjectList;
    }

    public void delete(Subject subject) {
        subjectDAO.delete(subject);
    }
}

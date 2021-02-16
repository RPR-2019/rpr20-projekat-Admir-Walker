package app.models;

import app.classes.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MainScreenModel {
    private ObservableList<Subject> subjects = FXCollections.observableArrayList();

    public void setSubjects(List<Subject> subjectList) {
        subjects = FXCollections.observableArrayList(subjectList);
    }

    public ObservableList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjectList(ObservableList<Subject> listaPredmeta) {
        this.subjects = listaPredmeta;
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
}

package app.classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Locale;
import java.util.Objects;

public class Subject {
    private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleObjectProperty<User> professor = new SimpleObjectProperty<>(null);


    public Subject(int id, String name, User professor) {
        this.id.set(id);
        this.name.set(name);
        this.professor.set(professor);
    }

    public Subject() {
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public User getProfessor() {
        return professor.get();
    }

    public SimpleObjectProperty<User> professorProperty() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor.set(professor);
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isSubstring(String searchParam) {
        return getName().toLowerCase(Locale.ROOT).contains(searchParam.toLowerCase(Locale.ROOT));
    }
    public int getProfessorId(){
        return getProfessor().getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(getId(), subject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

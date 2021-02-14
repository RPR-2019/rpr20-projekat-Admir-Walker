package app.classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Locale;

public class Predmet {
    private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleObjectProperty<User> profesor = new SimpleObjectProperty<>(null);


    public Predmet(int id, String name, User profesor) {
        this.id.set(id);
        this.name.set(name);
        this.profesor.set(profesor);
    }

    public Predmet() {
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

    public User getProfesor() {
        return profesor.get();
    }

    public SimpleObjectProperty<User> profesorProperty() {
        return profesor;
    }

    public void setProfesor(User profesor) {
        this.profesor.set(profesor);
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isSubstring(String searchParam){
        return getName().toLowerCase(Locale.ROOT).contains(searchParam.toLowerCase(Locale.ROOT));
    }
}

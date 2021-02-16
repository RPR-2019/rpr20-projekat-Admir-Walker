package app.classes;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Document {
    private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty path = new SimpleStringProperty("");
    private SimpleStringProperty uploadDate = new SimpleStringProperty(LocalDate.now().toString());
    private SimpleObjectProperty<Subject> subject = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<User> author = new SimpleObjectProperty<>(null);
    private SimpleBooleanProperty downloadable = new SimpleBooleanProperty(false);
    private SimpleIntegerProperty size = new SimpleIntegerProperty(0);
    private SimpleStringProperty type = new SimpleStringProperty("");

    public Document(int id, String name, String path, String uploadDate, Subject subject, User author, boolean downloadable, int size, String type) {
        this.id.set(id);
        this.name.set(name);
        this.path.set(path);
        this.uploadDate.set(uploadDate);
        this.subject.set(subject);
        this.author.set(author);
        this.downloadable.set(downloadable);
        this.size.set(size);
        this.type.set(type);
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

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getUploadDate() {
        return uploadDate.get();
    }

    public SimpleStringProperty uploadDateProperty() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate.set(uploadDate);
    }

    public Subject getSubject() {
        return subject.get();
    }

    public SimpleObjectProperty<Subject> subjectProperty() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject.set(subject);
    }

    public User getAuthor() {
        return author.get();
    }

    public SimpleObjectProperty<User> authorProperty() {
        return author;
    }

    public void setAuthor(User author) {
        this.author.set(author);
    }

    public boolean isDownloadable() {
        return downloadable.get();
    }

    public SimpleBooleanProperty downloadableProperty() {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable.set(downloadable);
    }

    public int getSize() {
        return size.get();
    }

    public SimpleIntegerProperty sizeProperty() {
        return size;
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}

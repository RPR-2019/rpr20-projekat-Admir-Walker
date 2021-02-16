package app.classes;

import javafx.scene.control.Alert;

public class ErrorAlert extends Alert {
    public ErrorAlert(String title, String heading, String content) {
        super(AlertType.ERROR);
        setTitle(title);
        setHeaderText(heading);
        setContentText(content);
    }
}

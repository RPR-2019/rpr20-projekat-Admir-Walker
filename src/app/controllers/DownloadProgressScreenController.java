package app.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadProgressScreenController {
    public ProgressBar progressBar;
    public Label labelProgress;
    public Button stopButton;

    public interface Stop{
        void stop();
    }
    public final Stop stop;
    public final Path path;
    public int maxSize;

    public DownloadProgressScreenController(Stop stop, Path path, int maxSize) {
        this.stop = stop;
        this.path = path;
        this.maxSize = maxSize;
    }

    @FXML
    public void initialize(){
        stopButton.setOnMouseClicked(mouseEvent -> {
            stop.stop();
            Node node = (Node) mouseEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        });
        updateProgress();
    }

    private void updateProgress() {

    }


}

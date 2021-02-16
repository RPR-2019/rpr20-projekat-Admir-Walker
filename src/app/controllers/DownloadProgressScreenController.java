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
    public interface Stop {
        void stop();
    }

    public ProgressBar progressBar;
    public Label labelProgress;
    public Button stopButton;
    public final Stop stop;
    public final Path path;
    public int maxSize;

    public DownloadProgressScreenController(Stop stop, Path path, int maxSize) {
        this.stop = stop;
        this.path = path;
        this.maxSize = maxSize;
    }

    @FXML
    public void initialize() {
        stopButton.setOnMouseClicked(mouseEvent -> {
            stop.stop();
            ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close();
        });
        updateProgress();
    }

    private synchronized void updateProgress() {
        // TO-DO naci bolji nacin za pisanje ove metode :)
        Platform.runLater(() -> {
            for (; ; ) {
                double currentSize = 0;
                try {
                    currentSize = Files.size(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                double progress = currentSize / maxSize;
                labelProgress.setText("" + Math.round(progress * 100) + "%");
                progressBar.setProgress(progress);
                if (currentSize == maxSize) {
                    progressBar.getScene().getWindow().hide();
                    break;
                }
            }
        });
    }


}

package app.controllers;

import app.classes.UploadTask;
import app.models.AddMaterialModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddMaterialController {
    public Button btnAdd;
    public Button btnCancel;
    public TextField pathField;

    private final AddMaterialModel addMaterialModel;

    public AddMaterialController(AddMaterialModel addMaterialModel) {
        this.addMaterialModel = addMaterialModel;
    }



    @FXML
    public void initialize() {
        pathField.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() > 1) {
                chooseFile();
            }
        });
        btnAdd.setOnMouseClicked(this::uploadFile);
        btnCancel.setOnMouseClicked(mouseEvent -> ((Node) mouseEvent.getSource()).getScene().getWindow().hide());
    }

    private void chooseFile() {
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle("Izaberite file za upload");
        File selectedDirectory = directoryChooser.showOpenDialog(new Stage());
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void uploadFile(MouseEvent mouseEvent) {
        if (!pathField.getText().trim().isEmpty()) {
            String uploadURL = "http://localhost/fileupload/index.php?subject=" + addMaterialModel.getSubjectID() + "&author=" + addMaterialModel.getAuthorID();
            String filePath = pathField.getText();
            File uploadFile = new File(filePath);
            UploadTask task = new UploadTask(uploadURL, uploadFile);
            task.upload(() -> Platform.runLater(() -> ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close()));
        }
    }
}

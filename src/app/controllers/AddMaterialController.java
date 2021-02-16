package app.controllers;

import app.classes.UploadTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddMaterialController {
    public Button btnAdd;
    public Button btnCancel;
    public TextField pathField;

    private int authorID;
    private int subjectID;

    public AddMaterialController(int authorID, int subjectID) {
        this.authorID = authorID;
        this.subjectID = subjectID;
    }

    @FXML
    public void initialize(){
        pathField.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() > 1){
                FileChooser directoryChooser = new FileChooser();
                directoryChooser.setTitle("Izaberite download folder");
                File selectedDirectory = directoryChooser.showOpenDialog(new Stage());
                if(selectedDirectory != null){
                    pathField.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });
        btnAdd.setOnMouseClicked(mouseEvent -> {
            if(!pathField.getText().trim().isEmpty())
            {
                String uploadURL = "http://localhost/fileupload/index.php?subject="+subjectID+"&author="+authorID;
                String filePath = pathField.getText();
                System.out.println("USLO?");
                File uploadFile = new File(filePath);
                UploadTask task = new UploadTask(uploadURL, uploadFile);
                task.upload(()->{
                    Platform.runLater(() -> {
                        ((Node)mouseEvent.getSource()).getScene().getWindow().hide();
                    });
                });
            }
        });
        btnCancel.setOnMouseClicked(mouseEvent -> {
            ((Node)mouseEvent.getSource()).getScene().getWindow().hide();
        });
    }
}

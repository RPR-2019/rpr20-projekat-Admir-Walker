package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class AddMaterialController {
    public WebView webViewForm;
    private int authorID;
    private int subjectID;

    public AddMaterialController(int authorID, int subjectID) {
        this.authorID = authorID;
        this.subjectID = subjectID;
    }

    @FXML
    public void initialize(){
        webViewForm.getEngine().load("http://localhost/fileupload/index.php?subject="+subjectID+"&author="+authorID);
    }
}

package app.controllers;

import app.classes.Document;
import app.classes.Subject;
import app.models.DocumentDAO;
import app.models.SubjectDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

public class DetailsMaterialController {

    private final Document document;
    public DocumentDAO documentDAO;
    public SubjectDAO subjectDAO;

    public TextField fieldId;
    public TextField fieldName;
    public TextField fieldPath;
    public TextField fieldDate;
    public TextField fieldAuthor;
    public TextField fieldSize;
    public ChoiceBox<Subject> choiceSubject;
    public CheckBox cboxDownloadable;
    public CheckBox cboxHide;
    public Button btnOk;
    public Button btnCancel;
    public TextField fieldType;

    public DetailsMaterialController(Document document, DocumentDAO documentDAO, SubjectDAO subjectDAO) {
        this.document = document;
        this.documentDAO = documentDAO;
        this.subjectDAO = subjectDAO;
    }

    @FXML
    public void initialize() {
        fillForm();
        btnCancel.setOnMouseClicked(this::closeStage);
        btnOk.setOnMouseClicked(mouseEvent -> {
            updateDocument();
            closeStage(mouseEvent);
        });
    }

    private void fillForm() {
        fieldId.setText("" + document.getId());
        fieldName.setText(document.getName());
        fieldPath.setText(document.getPath());
        fieldDate.setText(document.getUploadDate());
        fieldAuthor.setText(document.getAuthorFullName());
        fieldSize.setText("" + document.getSize());
        prepareSubjectCheckbox();
        cboxDownloadable.selectedProperty().set(document.isDownloadable());
        cboxHide.selectedProperty().set(false); // za sad false, omoguciti izmjene :)
        fieldType.setText(document.getType());
    }

    private void closeStage(MouseEvent mouseEvent) {
        ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close();
    }

    private void updateDocument() {
        document.setName(fieldName.getText());
        document.setSubject(choiceSubject.getSelectionModel().getSelectedItem());
        documentDAO.update(document);
    }
    private void prepareSubjectCheckbox(){
        List<Subject> subjectList = subjectDAO.fetchSubjects();
        Subject selectedSubject = document.getSubject();
        int index = subjectList.indexOf(selectedSubject);

        choiceSubject.setItems(FXCollections.observableArrayList(subjectList));
        if(index != -1){
            choiceSubject.getSelectionModel().select(index);
        }
    }
}

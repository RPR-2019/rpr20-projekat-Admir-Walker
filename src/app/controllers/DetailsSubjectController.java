package app.controllers;

import app.classes.User;
import app.models.DetailsSubjectModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DetailsSubjectController {

    public TextField fieldId;
    public TextField fieldName;
    public ChoiceBox<User> choiceProfessor;
    public Button btnOk;
    public Button btnCancel;

    private final DetailsSubjectModel detailsSubjectModel;

    public DetailsSubjectController(DetailsSubjectModel detailsSubjectModel) {
        this.detailsSubjectModel = detailsSubjectModel;
    }

    @FXML
    public void initialize(){
        fieldId.setText(detailsSubjectModel.getSubjectID());
        fieldName.setText(detailsSubjectModel.getSubjectName());
        initializeChoiceBox();
        btnOk.setOnMouseClicked(this::updateSubject);
        btnCancel.setOnMouseClicked(this::closeStage);
    }
    private void initializeChoiceBox(){
        choiceProfessor.setItems(detailsSubjectModel.fetchProfessors());
        choiceProfessor.getSelectionModel().select(findProfessor(detailsSubjectModel.getCurrentProfessor()));
    }
    private void updateSubject(MouseEvent mouseEvent){
        detailsSubjectModel.setName(fieldName.getText());
        detailsSubjectModel.setProfessor(choiceProfessor.getSelectionModel().getSelectedItem());
        detailsSubjectModel.updateSubject();
        closeStage(mouseEvent);
    }
    private User findProfessor(User professor){
        return detailsSubjectModel.fetchProfessors().stream().filter(user -> user.equals(professor)).findFirst().get();
    }
    private void closeStage(MouseEvent mouseEvent){
        ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close();
    }

}

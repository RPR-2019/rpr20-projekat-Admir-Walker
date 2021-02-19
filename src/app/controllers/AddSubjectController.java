package app.controllers;

import app.classes.ErrorAlert;
import app.classes.Subject;
import app.classes.User;
import app.models.AddSubjectModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddSubjectController {

    private final AddSubjectModel addSubjectModel;
    public TextField fieldNaziv;
    public ChoiceBox<User> cboxProfessor;
    public Button btnAdd;
    public Button btnCancel;

    public AddSubjectController(AddSubjectModel addSubjectModel) {
        this.addSubjectModel = addSubjectModel;
    }
    @FXML
    public void initialize(){
        setUpCheckBox();
        setUpAddButton();
        setUpCancelButton();
    }
    private void setUpCheckBox(){
        cboxProfessor.setItems(addSubjectModel.fetchProfessors());
        cboxProfessor.getSelectionModel().selectFirst();
    }
    private void setUpAddButton(){
        btnAdd.setOnMouseClicked(this::addSubject);
    }
    private void setUpCancelButton(){
        btnCancel.setOnMouseClicked(mouseEvent -> ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close());
    }
    private void addSubject(MouseEvent mouseEvent){
        String naziv = fieldNaziv.getText();
        User professor = cboxProfessor.getSelectionModel().getSelectedItem();

        if(naziv.trim().isEmpty()){
            new ErrorAlert("Naziv prazan", "Naziv prazan", "Naziv prazan").showAndWait();
        }
        else if(professor == null){
            new ErrorAlert("Profesor nije izabran", "Profesor nije izabran", "Profesor nije izabran").showAndWait();
        }
        else{
            Subject subject = new Subject();
            subject.setName(naziv);
            subject.setProfessor(professor);
            addSubjectModel.addSubject(subject);
            ((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow()).close();
        }
    }

}

package app.controllers;

import app.classes.Subject;
import app.classes.User;
import app.models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

    private final MainModel mainModel;

    public TextField searchField;
    public ListView<Subject> subjectList;
    public Button btnAddSubject;


    public MainController(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    @FXML
    public void initialize() {
        mainModel.setSubjects(mainModel.fetchSubjects());
        setSubjectListItems();
        subjectList.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() > 1) {
                if(subjectList.getSelectionModel().getSelectedItem()!=null){
                    openMaterial();
                }
            }
        });
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                subjectList.setItems(mainModel.search(t1));
            }
        });
        btnAddSubject.setOnMouseClicked(mouseEvent -> {
            try {
                AddSubjectModel addSubjectModel = new AddSubjectModel(UserDAO.getInstance(), SubjectDAO.getInstance());
                AddSubjectController addSubjectController = new AddSubjectController(addSubjectModel);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addSubject.fxml"));
                loader.setController(addSubjectController);
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setOnHiding(windowEvent -> setSubjectListItems());
                stage.setTitle("Dodaj predmet");
                stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
                stage.show();
            } catch (SQLException | IOException exception) {
                exception.printStackTrace();
            }

        });
    }
    private void setSubjectListItems(){
        subjectList.setItems(FXCollections.observableArrayList(mainModel.fetchSubjects()));
    }
    private void openMaterial(){
        try {
            MaterialModel materialModel = new MaterialModel(mainModel.getUser(), subjectList.getSelectionModel().getSelectedItem(), DocumentDAO.getInstance());
            MaterialController materialController = new MaterialController(materialModel);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/material.fxml"));
            loader.setController(materialController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Spisak dokumenata");
            stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            stage.show();
            stage.toFront();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}

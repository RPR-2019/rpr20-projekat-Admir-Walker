package app.controllers;

import app.classes.Subject;
import app.classes.User;
import app.models.DocumentDAO;
import app.models.MainModel;
import app.models.MaterialModel;
import app.models.SubjectDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

    private final User currentUser;
    private final SubjectDAO subjectDAO;
    private final MainModel mainModel;

    public TextField searchField;
    public ListView<Subject> subjectList;


    public MainController(User user, SubjectDAO subjectDAO, MainModel mainModel) {
        this.currentUser = user;
        this.subjectDAO = subjectDAO;
        this.mainModel = mainModel;
    }

    @FXML
    public void initialize() {
        mainModel.setSubjects(subjectDAO.fetchSubjects());
        subjectList.setItems(mainModel.getSubjects());
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
    }
    private void openMaterial(){
        try {
            MaterialModel materialModel = new MaterialModel(currentUser, subjectList.getSelectionModel().getSelectedItem(), DocumentDAO.getInstance());
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

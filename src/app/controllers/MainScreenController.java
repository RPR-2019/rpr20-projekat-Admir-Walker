package app.controllers;

import app.classes.Subject;
import app.classes.User;
import app.models.DocumentDAO;
import app.models.MainScreenModel;
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

public class MainScreenController {

    private final User currentUser;
    private final SubjectDAO subjectDAO;
    private final MainScreenModel mainScreenModel;

    public TextField searchField;
    public ListView<Subject> spisakPredmeta;


    public MainScreenController(User user, SubjectDAO subjectDAO, MainScreenModel mainScreenModel) {
        this.currentUser = user;
        this.subjectDAO = subjectDAO;
        this.mainScreenModel = mainScreenModel;
    }

    @FXML
    public void initialize() {
        mainScreenModel.setSubjects(subjectDAO.fetchSubjects());
        spisakPredmeta.setItems(mainScreenModel.getSubjects());
        spisakPredmeta.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() > 1) {
                try {
                    MaterialScreenController materialScreenController = new MaterialScreenController(currentUser, spisakPredmeta.getSelectionModel().getSelectedItem(), DocumentDAO.getInstance());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/materialScreen.fxml"));
                    loader.setController(materialScreenController);
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
        });
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            if (t1 != null) {
                spisakPredmeta.setItems(mainScreenModel.search(t1));
            }
        });
    }
}

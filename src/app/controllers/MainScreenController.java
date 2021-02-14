package app.controllers;

import app.classes.Subject;
import app.classes.User;
import app.models.MainScreenModel;
import app.models.SubjectDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;

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
            if(mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() > 1)
            {
                System.out.println(spisakPredmeta.getSelectionModel().getSelectedItem().getId());
            }
        });
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            if(t1 != null){
                spisakPredmeta.setItems(mainScreenModel.search(t1));
            }
        });
    }
}

package app.controllers;

import app.classes.Predmet;
import app.classes.User;
import app.models.MainScreenModel;
import app.models.PredmetDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainScreenController {

    private final User currentUser;
    private final PredmetDAO predmetDAO;
    private final MainScreenModel mainScreenModel;

    public TextField searchField;
    public ListView<Predmet> spisakPredmeta;


    public MainScreenController(User user, PredmetDAO predmetDAO, MainScreenModel mainScreenModel) {
        this.currentUser = user;
        this.predmetDAO = predmetDAO;
        this.mainScreenModel = mainScreenModel;
    }

    @FXML
    public void initialize() {
        mainScreenModel.setListaPredmeta(predmetDAO.dajListuSvihPredmeta());
        spisakPredmeta.setItems(mainScreenModel.getListaPredmeta());
        searchField.textProperty().addListener((observableValue, s, t1) -> {
            if(t1 != null){
                spisakPredmeta.setItems(mainScreenModel.search(t1));
            }
        });
    }
}

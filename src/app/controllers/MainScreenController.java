package app.controllers;

import app.classes.Predmet;
import app.classes.User;
import app.models.MainScreenModel;
import app.models.PredmetDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class MainScreenController {

    private final User currentUser;
    private final PredmetDAO predmetDAO;
    private final MainScreenModel mainScreenModel;

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
    }
}

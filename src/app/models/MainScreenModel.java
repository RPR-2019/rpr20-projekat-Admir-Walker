package app.models;

import app.classes.Predmet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MainScreenModel {
    ObservableList<Predmet> listaPredmeta = FXCollections.observableArrayList();

    public void setListaPredmeta(List<Predmet> predmetList) {
        listaPredmeta = FXCollections.observableArrayList(predmetList);
    }

    public ObservableList<Predmet> getListaPredmeta() {
        return listaPredmeta;
    }
}

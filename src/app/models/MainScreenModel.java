package app.models;

import app.classes.Predmet;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MainScreenModel {
    private ObservableList<Predmet> listaPredmeta = FXCollections.observableArrayList();

    public void setListaPredmeta(List<Predmet> predmetList) {
        listaPredmeta = FXCollections.observableArrayList(predmetList);
    }

    public ObservableList<Predmet> getListaPredmeta() {
        return listaPredmeta;
    }

    public void setListaPredmeta(ObservableList<Predmet> listaPredmeta) {
        this.listaPredmeta = listaPredmeta;
    }

    public ObservableList<Predmet> search(String searchParam){
        ObservableList<Predmet> trazeniPredmeti = FXCollections.observableArrayList();
        listaPredmeta.forEach(predmet -> {
            if(predmet.isSubstring(searchParam)){
                trazeniPredmeti.add(predmet);
            }
        });
        return trazeniPredmeti;
    }
}

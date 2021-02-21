package app.controllers;

import app.classes.MenuPopUp;
import app.classes.Subject;
import app.classes.UserType;
import app.models.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController implements MenuPopUp.ContextAction {

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
       if(mainModel.getUserType().equals(UserType.PROFESSOR)){
           setupControls();
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
    }
    private void setSubjectListItems(){
        subjectList.setItems(FXCollections.observableArrayList(mainModel.fetchSubjects()));
    }
    private void openMaterial(){
        try {
            MaterialModel materialModel = new MaterialModel(mainModel.getUser(), subjectList.getSelectionModel().getSelectedItem(), DocumentDAO.getInstance());
            MaterialController materialController = new MaterialController(materialModel);
            String partOfPath = materialModel.getUserType().equals(UserType.PROFESSOR) ? "professors" : "users";
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/"+partOfPath+"/material.fxml"));
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
    private void setupControls() {
        MenuPopUp menuPopUp = new MenuPopUp(this);
        ContextMenu contextMenu = menuPopUp.createContextMenu();
        subjectList.setContextMenu(contextMenu);
    }

    @Override
    public void detailsAction() {
        if (subjectList.getSelectionModel().getSelectedItem() != null) {
            openDetails(subjectList.getSelectionModel().getSelectedItem());
        }
    }

    @Override
    public void deleteAction() {
        if (subjectList.getSelectionModel().getSelectedItem() != null) {
            deleteDocument(subjectList.getSelectionModel().getSelectedItem());
        }
    }
    private void openDetails(Subject subject) {
        try {
            DetailsSubjectModel detailsSubjectModel = new DetailsSubjectModel(subject, SubjectDAO.getInstance(), UserDAO.getInstance());
            DetailsSubjectController detailsSubjectController = new DetailsSubjectController(detailsSubjectModel);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailsSubject.fxml"));
            loader.setController(detailsSubjectController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setOnHiding(windowEvent -> {
                setSubjectListItems();
            });
            stage.setTitle("Detalji o dokumentu");
            stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteDocument(Subject subject) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Upozorenje o brisanju");
        alert.setContentText("Da li zelite obrisati");
        alert.showAndWait();
        if (alert.getResult().getButtonData().isDefaultButton()) {
            mainModel.delete(subject);
            setSubjectListItems();
        }
    }
}

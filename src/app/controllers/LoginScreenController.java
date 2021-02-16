package app.controllers;

import app.classes.ErrorAlert;
import app.classes.User;
import app.models.MainScreenModel;
import app.models.SubjectDAO;
import app.models.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginScreenController {

    public TextField fieldEmail;
    public PasswordField fieldPassword;
    private UserDAO userDAO;

    public LoginScreenController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void login(ActionEvent actionEvent) throws IOException, SQLException {
        User user = new User(0, "", "", fieldEmail.getText().toLowerCase(), fieldPassword.getText(), null);
        user = userDAO.checkLogin(user);
        if (user != null) {
            MainScreenController mainScreenController = new MainScreenController(user, SubjectDAO.getInstance(), new MainScreenModel());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainScreen.fxml"));
            loader.setController(mainScreenController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Spisak predmeta");
            stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            stage.show();

            Node node = (Node) actionEvent.getSource();
            Stage currentStage = (Stage) node.getScene().getWindow();
            currentStage.close();
        } else {
            ErrorAlert errorAlert = new ErrorAlert("Login neuspješan", "Login neuspješan", "Pogrešno korisničko ime ili password. Pokušajte ponovo");
            errorAlert.showAndWait();
        }
    }

    public void exit(ActionEvent actionEvent) throws SQLException {
        UserDAO.removeInstance();
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }
}

package app.controllers;

import app.classes.ErrorAlert;
import app.classes.User;
import app.models.UserDAO;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginScreenController {

    public TextField fieldEmail;
    public PasswordField fieldPassword;
    private UserDAO userDAO;

    public LoginScreenController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public void login(ActionEvent actionEvent) {
        User user = new User(0,"","",fieldEmail.getText().toLowerCase(),fieldPassword.getText(), null);
        if(userDAO.provjeriLogin(user)){
            // TO-DO otvaranje nove forme
            System.out.println("Login uspjesan?");
        }
        else{
            ErrorAlert errorAlert = new ErrorAlert("Login failed", "Login failed", "Wrong username or password, please try again");
            errorAlert.showAndWait();
        }
    }

    public void exit(ActionEvent actionEvent) throws SQLException {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        UserDAO.removeInstance();
        stage.close();
    }
}

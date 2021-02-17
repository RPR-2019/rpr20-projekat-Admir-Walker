package app.controllers;

import app.models.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.EmptyNodeQueryException;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class LoginControllerTest {
    @Start
    public void start(Stage stage) throws SQLException, IOException {
        UserDAO userDAO = UserDAO.getInstance();
        LoginController loginController = new LoginController(userDAO);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        loader.setController(loginController);
        Parent root = loader.load();
        stage.setTitle("Login");
        stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testUspjesanLogin(FxRobot robot) {
        TextField email = robot.lookup("#fieldEmail").queryAs(TextField.class);
        PasswordField password = robot.lookup("#fieldPassword").queryAs(PasswordField.class);
        Button loginButton = robot.lookup("#loginButton").queryAs(Button.class);

        email.setText("gost");
        password.setText("gost");
        robot.clickOn(loginButton);


        assertThrows(EmptyNodeQueryException.class,() -> {
            robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        });
    }

    @Test
    public void testNeuspjesanLogin(FxRobot robot) {
        TextField email = robot.lookup("#fieldEmail").queryAs(TextField.class);
        PasswordField password = robot.lookup("#fieldPassword").queryAs(PasswordField.class);
        Button loginButton = robot.lookup("#loginButton").queryAs(Button.class);

        email.setText("000");
        password.setText("000");
        robot.clickOn(loginButton);

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertEquals("Login neuspješan", dialogPane.getHeaderText());
    }
}
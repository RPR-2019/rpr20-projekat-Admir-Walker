package app;

import app.controllers.LoginScreenController;
import app.models.UserDAO;
import app.settings.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        UserDAO userDAO = UserDAO.getInstance();
        LoginScreenController loginScreenController = new LoginScreenController(userDAO);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginScreen.fxml"));
        loader.setController(loginScreenController);
        Parent root = loader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            Database database = Database.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}

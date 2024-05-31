package Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class MainPage extends Application {

    @Override
    public void start(Stage stage) {
        String rootPath = "/fxml/MainPage.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rootPath));
            stage.setTitle("ERP system");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            Logger.error("Cannot start application, as main page is not found or cannot be loaded. Please check whether the file is at " + rootPath);
        }
    }
}

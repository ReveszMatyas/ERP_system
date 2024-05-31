package Controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.ProgressBarTreeTableCell;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class MainPage extends Application {

    @Override
    public void start(Stage stage) {
        String rootPath = "/fxml/MainPage.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(rootPath));
            if (root == null){
                Logger.error("Cannot start application, please check "+ rootPath);
                return;
            }
            stage.setTitle("ERP system");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            stage.show();
        } catch (IOException e){
            Logger.error("Cannot start application, as main page is not found or cannot be loaded. Please check whether the file is at " + rootPath);
        }



    }
}

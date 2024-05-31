package Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class NavigationButtonsController {
    @FXML
    private void displayStock(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String pth = "/fxml/TotalStock.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pth));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e)
        {
            Logger.error("An error occurred while trying to show the \"display stock\" window. Please check" + pth);
        }
    }
    @FXML
    private void displayAddStock(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String pth = "/fxml/AddStock.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pth));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e)
        {
            Logger.error("An error occurred while trying to show the \"add stock\" window. Please check" + pth);
        }
    }
    @FXML
    private void DisplayOrder(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String pth = "/fxml/NewOrder.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pth));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e)
        {
            Logger.error("An error occurred while trying to show the \"add order\" window. Please check" + pth);
        }
    }

    @FXML
    private void displayAddRecipe(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String pth = "/fxml/AddRecipe.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pth));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e)
        {
            Logger.error("An error occurred while trying to show the \"add recipe\" window. Please check" + pth);
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Logger.info("Exiting the application...");
        Platform.exit();
    }
}

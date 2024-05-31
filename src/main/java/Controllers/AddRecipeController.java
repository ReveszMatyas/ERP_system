package Controllers;

import Logic.ILogic;
import Logic.ProductInfoLogic;
import Logic.RecipeLogic;
import Logic.model.Product;
import Logic.model.Recipe;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.junit.platform.commons.util.StringUtils;
import org.tinylog.Logger;
import com.google.common.base.Strings;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AddRecipeController {

    Map<Product, Number> requirement;

    @FXML
    private void initialize(){
        requirement = new HashMap<>();
        bindTableColumns();
    }

    private void populateTable() {
        if (requirement.isEmpty()) {
            return;
        }

        tableRequirements.getItems().clear();

        ObservableList<Map.Entry<Product, Number>> items = FXCollections.observableArrayList();
        items.addAll(requirement.entrySet());
        tableRequirements.setItems(items);
    }

    private void bindTableColumns(){
        tableColReqProductName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getProdName()));
        tableColReqQuantity.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValue().doubleValue()).asObject());
    }

    @FXML
    void addRecipe(ActionEvent event) {
        Product product = ProductInfoLogic.getProductByProdName(txtFieldProdName.getText());
        if (product == null){
            displayMessage(lblErrorMsg2, "Choosen product does not exist.\nPlease check spelling.");
            return;
        }

        if (!ILogic.isNumeric(txtFieldleadTime.getText())){
            displayMessage(lblErrorMsg2, "Leadtime is not numeric.");
            return;
        }

        int leadTime = 0;

        try {
            leadTime = Integer.parseInt(txtFieldleadTime.getText());
        } catch (Exception e){
            displayMessage(lblErrorMsg2, "Leadtime is not a valid.");
            return;
        }

        Recipe recipe = new Recipe(product.getId(), populateRecipeHashMap(), leadTime);
        try {
            RecipeLogic.addNewRecipe(recipe);
            displayMessage(lblErrorMsg2, "Recipe added to product: " + product.getProdName() + ".");
            requirement.clear();
        } catch (IOException e){
            Logger.error(MessageFormat.format("An error occurred while trying to add recipe to {0}", product.getProdName()));
        }
    }

    private void displayMessage(Label label, String message){
        label.setText(message);
        msgVisible(label,true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    label.setText("");
                    msgVisible(label,false);
                });
            }
        }, 10000);
    }

    private void msgVisible(Label label, boolean isVisible){
        label.setVisible(isVisible);
    }

    @FXML
    void addRequirement(ActionEvent event) {
        Product product = ProductInfoLogic.getProductByProdName(prodName.getText());
        if (product == null){
            displayMessage(lblErrorMsg, "Required product does not exist.");
            return;
        }

        if (ILogic.isNullOrEmpty(qty.getText()) || !ILogic.isNumeric(qty.getText())){
            displayMessage(lblErrorMsg, "Please enter a valid quantity number");
            return;
        }

        double qtyAsDouble = Double.parseDouble(qty.getText());

        if (qtyAsDouble <= 0){
            displayMessage(lblErrorMsg, "Please enter a positive number");
            return;
        }

        if (requirement.containsKey(product)){
            displayMessage(lblErrorMsg, "Product is already in the requirement list.\nThe value will be overwritten.");
        }

        requirement.put(product, qtyAsDouble);
        populateTable();
    }

    private HashMap<String, Number> populateRecipeHashMap(){
        HashMap<String, Number> toReturn = new HashMap<>();
        for (Map.Entry<Product, Number> entry : requirement.entrySet()){
            toReturn.put(String.valueOf(entry.getKey().getId()), entry.getValue());
        }
        return toReturn;
    }

    @FXML
    private Label lblErrorMsg;

    @FXML
    private Label lblErrorMsg2;

    @FXML
    private TextField prodName;

    @FXML
    private TextField qty;

    @FXML
    private TableColumn<Map.Entry<Product, Number>, String> tableColReqProductName;

    @FXML
    private TableColumn<Map.Entry<Product, Number>, Double> tableColReqQuantity;

    @FXML
    private TableView<Map.Entry<Product, Number>> tableRequirements;

    @FXML
    private TextField txtFieldProdName;

    @FXML
    private TextField txtFieldleadTime;

}

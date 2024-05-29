package Controllers;

import Logic.ILogic;
import Logic.ProductInfoLogic;
import Logic.StockLogic;
import Logic.model.Product;
import Logic.model.Stock;
import com.mysql.cj.util.StringUtils;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddStockController {

    private String selectedProduct;
    private Product prod;

    @FXML
    private void initialize(){
        initProductList();
        addSelectionListenerToList();
        fillComboBoxTypes();
    }

    private void addSelectionListenerToList(){
        productsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedProduct = newValue;
            populateSelectedProductData();
        });
    }

    private void populateSelectedProductData(){
        if (selectedProduct == null){
            return;
        }

        prod = ProductInfoLogic.getProductByProdName(selectedProduct);

        prodnameLabel.setText(prod.getProdName());
        productIdLabel.setText(String.valueOf(prod.getId()));
        isAvailableLabel.setText(String.valueOf(prod.isActive()));
        typeLabel.setText(prod.getType());
        unitLabel.setText(prod.getUnit());
        currentQtyLabel.setText(String.valueOf(StockLogic.getStockById(prod.getId()).getQty()));
    }

    private void initProductList(){
        // Convert the list of Product objects to a list of product names
        ObservableList<String> productNames = FXCollections.observableArrayList();
        for (Product product : ProductInfoLogic.getProducts()) {
            productNames.add(product.getProdName());
        }

        // Set the items in the ListView
        productsListView.setItems(productNames);
    }


    @FXML
    private Button btnAddNewItem;

    @FXML
    private Button btnAddStockExistingItem;

    @FXML
    private Label currentQtyLabel;

    @FXML
    private Label isAvailableLabel;

    @FXML
    private TextField newProdNameTxtField;

    @FXML
    private TextField newQtyTxtField;

    @FXML
    private ComboBox<String> newTypeCombo;

    @FXML
    private ComboBox<String> newUnitCombo;

    @FXML
    private Label prodnameLabel;

    @FXML
    private Label productIdLabel;

    @FXML
    private ListView<String> productsListView;

    @FXML
    private Label typeLabel;

    @FXML
    private Label unitLabel;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Label errorMessageLabel2;

    @FXML
    private TextField qtyToAddTextField;

    @FXML
    private CheckBox newisActiveBox;

    @FXML
    void AddNewItem(ActionEvent event) throws IOException {
        if (StringUtils.isNullOrEmpty(newProdNameTxtField.getText()))
        {
            errorMessage(errorMessageLabel2, "Please enter a valid product name!");
        } else if(ProductInfoLogic.getProductByProdName(newProdNameTxtField.getText()) != null){
            errorMessage(errorMessageLabel2, "The product name already exists. If you need to add stock to an existing product, use the fields above!");
        } else if(StringUtils.isNullOrEmpty(newTypeCombo.getValue()) || StringUtils.isNullOrEmpty(newUnitCombo.getValue())){
            errorMessage(errorMessageLabel2, "Please enter a valid item type and unit!");
        } else if(StringUtils.isNullOrEmpty(newQtyTxtField.getText()) ||  !ILogic.isNumeric(newQtyTxtField.getText())) {
            errorMessage(errorMessageLabel2, "Please enter a valid quantity numer+");
        }
        else {
            double qty = Double.parseDouble(newQtyTxtField.getText());
            if (qty <= 0){
                errorMessage(errorMessageLabel2, "Please enter a positive number!");
                return;
            }

            int newProdId = ProductInfoLogic.getNewItemId();

            Product newProd = new Product(newProdId, newProdNameTxtField.getText(), newUnitCombo.getValue(), newTypeCombo.getValue(), newisActiveBox.isSelected());

            ProductInfoLogic.addNewProductInfo(newProd); // TODO: catch exception

            Stock newStock = new Stock(newProdId, qty);

            StockLogic.addNewStock(newStock); // TODO: catch exception

            initProductList();
        }


    }

    private void errorMsgVisible(Label errorLbl, boolean bool){
        errorLbl.setVisible(bool);
    }

    private void errorMessage(Label errorLbl, String message){
        errorLbl.setText(message);
        errorMsgVisible(errorLbl,true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    errorLbl.setText("");
                    errorMsgVisible(errorLbl,false);
                });
            }
        }, 10000);
    }

    @FXML
    void addStockToExistingItem(ActionEvent event) throws IOException {
        if (selectedProduct == null){
            errorMessage(errorMessageLabel, "Please select an item before progressing!");
        } else if(prod == null) {
            errorMessage(errorMessageLabel,"No product has been choosen, or product does not exist.");
        }
        else if (StringUtils.isNullOrEmpty(qtyToAddTextField.getText()) ||  !ILogic.isNumeric(qtyToAddTextField.getText())){
            errorMessage(errorMessageLabel, "Please input a valid number!");
        } else {
            double qty = Double.parseDouble(qtyToAddTextField.getText());
            if (qty <= 0){
                errorMessage(errorMessageLabel,"Please enter a positive number!");
                return;
            }
            StockLogic.addStockToItem(prod.getId(), qty); // TODO: catch exception
            populateSelectedProductData();
        }
        qtyToAddTextField.setText("");
    }


    @FXML
    void fillComboBoxTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        for (String type : ProductInfoLogic.getTypesDistinct()){
            types.add(type);
        }

        ObservableList<String> units = FXCollections.observableArrayList();
        for (String unit : ProductInfoLogic.getUnitsDistinct()){
            units.add(unit);
        }

        newTypeCombo.setItems(types);
        newUnitCombo.setItems(units);
    }

}
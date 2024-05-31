package Controllers;

import Logic.ILogic;
import Logic.ProductInfoLogic;
import Logic.StockLogic;
import Logic.exceptions.ProductWithoutIdException;
import Logic.model.Product;
import Logic.model.Stock;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.tinylog.Logger;

import java.io.IOException;
import java.text.MessageFormat;
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
    void AddNewItem(ActionEvent event) {
        if (ILogic.isNullOrEmpty(newProdNameTxtField.getText())) {
            errorMessage(errorMessageLabel2, "Please enter a valid product name!");
        } else if(ProductInfoLogic.getProductByProdName(newProdNameTxtField.getText()) != null){
            errorMessage(errorMessageLabel2, "The product name already exists. If you need to add stock to an existing product, use the fields above!");
        } else if(ILogic.isNullOrEmpty(newTypeCombo.getValue()) || ILogic.isNullOrEmpty(newUnitCombo.getValue())){
            errorMessage(errorMessageLabel2, "Please enter a valid item type and unit!");
        } else if(ILogic.isNullOrEmpty(newQtyTxtField.getText()) ||  !ILogic.isNumeric(newQtyTxtField.getText())) {
            errorMessage(errorMessageLabel2, "Please enter a valid quantity numer");
        }
        else {
            double qty = Double.parseDouble(newQtyTxtField.getText());
            if (qty < 0){
                errorMessage(errorMessageLabel2, "Please enter a positive number!");
                return;
            }

            int newProdId = ProductInfoLogic.getNewItemId();

            Product newProd = new Product(newProdId, newProdNameTxtField.getText(), newUnitCombo.getValue(), newTypeCombo.getValue(), newisActiveBox.isSelected());
            try {
                ProductInfoLogic.addNewProductInfo(newProd);
                Logger.info(MessageFormat.format("New product was added to the product information repo. id:{0} name:{1}", newProd.getId(), newProd.getProdName()));
            } catch (ProductWithoutIdException e){
                Logger.error(e.getMessage());
            } catch (IOException e){
                Logger.error(MessageFormat.format("An error occurred while trying to add {0} product to repository {1}", newProd.getProdName(), ProductInfoLogic.path));
                Logger.error(e.getMessage());
                Logger.error(e.getStackTrace());
            }

            Stock newStock = new Stock(newProdId, qty);

            try {
                StockLogic.addNewStock(newStock);
                Logger.info(MessageFormat.format("New stock info was added to the stock repo.id: {0} qty: {1}", newProdId, qty));
            } catch (IOException e){
                Logger.error(MessageFormat.format("An error occurred while trying to add stock to the new product: {0} in quantity: {1}", newProd.getProdName(), newStock.getQty() , e));
            } finally {
                initProductList();
            }
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
    void addStockToExistingItem(ActionEvent event){
        if (selectedProduct == null){
            errorMessage(errorMessageLabel, "Please select an item before progressing!");
        } else if(prod == null) {
            errorMessage(errorMessageLabel,"No product has been choosen, or product does not exist.");
        }
        else if (ILogic.isNullOrEmpty(qtyToAddTextField.getText()) ||  !ILogic.isNumeric(qtyToAddTextField.getText())){
            errorMessage(errorMessageLabel, "Please input a valid number!");
        } else {
            double qty = Double.parseDouble(qtyToAddTextField.getText());
            if (qty <= 0){
                errorMessage(errorMessageLabel,"Please enter a positive number!");
                return;
            }
            try {
                StockLogic.addStockToItem(prod.getId(), qty);
            }
            catch (IOException e){
                Logger.error("An error occurred while trying to add stock to selected item. product:{0} quantity:{1}", prod.getProdName() ,qty);
            }

            populateSelectedProductData();
        }
        qtyToAddTextField.setText("");
    }

    @FXML
    void fillComboBoxTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        types.addAll(ProductInfoLogic.getTypesDistinct());

        ObservableList<String> units = FXCollections.observableArrayList();
        units.addAll(ProductInfoLogic.getUnitsDistinct());

        newTypeCombo.setItems(types);
        newUnitCombo.setItems(units);
    }

}
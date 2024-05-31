package Controllers;

import Logic.*;
import Logic.Manufacture;
import Logic.model.Product;
import Logic.model.Recipe;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.*;

public class NewOrderController {

    private String selectedProductName;

    private Map<Product, Double> rawRequirements;

    private Recipe recipeOfSelected;

    private int qtyQuotient = 1;

    @FXML
    private void initialize(){
        bindTableColumns();
        initCraftableList();
        addQtyListenerToTextField();
        addSelectionListenerToList();
    }

    private void initCraftableList(){
        ObservableList<String> craftableProducts = FXCollections.observableArrayList();
        for (Product prod : Objects.requireNonNull(ProductInfoLogic.getAllCraftableProducts())){
            craftableProducts.add(prod.getProdName());
        }
        listCraftableItems.setItems(craftableProducts);
    }

    private void addSelectionListenerToList(){
        listCraftableItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedProductName = newValue;

            if (ILogic.isNullOrEmpty(selectedProductName)){
                displayMessage(errorMessage, "The selected product does not exist, or the name is empty");
                return;
            }
            rawRequirements = new HashMap<>();

            TreeItem<String> rootItem = createRoot(selectedProductName);
            populateRequiredItemsTreeView(rootItem);
            populateRequiredRawItemsTable();
            isPossibleToManufacture();
        });
    }

    private void addQtyListenerToTextField(){
        orderQtyTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
            changeQuantityQuotient();

            if (rawRequirements == null || rawRequirements.size() == 0)
                return;

            populateRequiredRawItemsTable();
            errorMsgVisible(errorMessage, false);
            isPossibleToManufacture();
        });
    }

    private TreeItem<String> createRoot(String selected){
        recipeOfSelected = RecipeLogic.getRecipeByProductName(selected);

        if (recipeOfSelected == null){
            displayMessage(errorMessage, "The selected product does not have a valid recipe.");
            return null;
        }


        String rootString = getProductRequirementString(selected, 1.0);

        TreeItem<String> root = new TreeItem<>(rootString);

        addBranches(root, recipeOfSelected, 1);

        return root;
    }

    private void addBranches(TreeItem<String> parent, Recipe rec, Number parentRequiredQuantity){
        for (java.util.Map.Entry<String , Number> requirement : rec.getRequiredProducts().entrySet()){

            int prodNum = Integer.parseInt((requirement.getKey()));
            Product prod = ProductInfoLogic.getProductById(prodNum);

            if (prod == null) {
                displayMessage(errorMessage, "An error occurred while checking for requirements.\nPlease consult the maintainer of the program.");
                return;
            }
            TreeItem<String> newBranch = new TreeItem<>(getProductRequirementString(prod.getProdName(), requirement.getValue()));

            parent.getChildren().add(newBranch);

            Recipe recipeOfRequirement = RecipeLogic.getFirstRecipebyId(prodNum);
            boolean hasRecipe = recipeOfRequirement != null;

            if (hasRecipe){
                addBranches(newBranch, recipeOfRequirement, requirement.getValue());
            } else {
                addToRawRequirementMap(rawRequirements, prod, requirement.getValue().doubleValue() * parentRequiredQuantity.doubleValue());
            }
        }
    }

    private void bindTableColumns(){
        ReqProdIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getKey().getId()).asObject());
        reqItemsTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getProdName()));
        RequiredItemsQtyTableCol.setCellValueFactory(cellData -> new SimpleDoubleProperty((cellData.getValue().getValue()) * qtyQuotient).asObject());
        reqItemsUnitTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getUnit()));
    }

    private void addToRawRequirementMap(Map<Product, Double> map, Product product, Number qty){
        double qtyAsDouble = qty.doubleValue();

        if (map.containsKey(product)){
            map.put(product, map.get(product) + qtyAsDouble);
        } else {
            map.put(product, qtyAsDouble);
        }
    }

    private String getProductRequirementString(String prodName, Number qty){
        double qtyAsDbl = qty.doubleValue();
        return "%s       QTY:%.2f".formatted(prodName, qtyAsDbl);
    }


    private void populateRequiredItemsTreeView(TreeItem<String> rootItem){
        requiredItemsTreeView.setRoot(rootItem);
    }

    private void populateRequiredRawItemsTable(){

        requiredRawMaterialTable.getItems().clear();

        ObservableList<Map.Entry<Product, Double>> items = FXCollections.observableArrayList();

        items.addAll(rawRequirements.entrySet());

        requiredRawMaterialTable.setItems(items);

    }


    @FXML
    private void placeManufactureOrder(){
        if (recipeOfSelected == null)
            displayMessage(errorMessage, "Selected recipe does not exist!");

        if(!isPossibleToManufacture()){
            displayMessage(errorMessage, "It is not possible to manufacture the selected amount!");
            return;
        }

        if (ILogic.isNullOrEmpty(orderQtyTxtField.getText()) || !ILogic.isNumeric(orderQtyTxtField.getText())){
            displayMessage(errorMessage, "Please select a valid quantity to manufacture");
            return;
        }

        Manufacture.manufactureProduct(recipeOfSelected.getProdId(), qtyQuotient);
        displayMessage(lblOrderPlaced, String.format("The manufacturing process has started \nfor %d unit of %s", qtyQuotient, selectedProductName));
    }

    private boolean isPossibleToManufacture() {
        if(!StockLogic.isManufacturePossible(rawRequirements, qtyQuotient)){
            displayMessage(errorMessage, "Manufacturing of selected items in selected\nquantity is not possible!");
            return false;
        }
        return true;
    }

    private void errorMsgVisible(Label label, boolean bool){
        label.setVisible(bool);
    }

    private void displayMessage(Label label, String message){
        label.setText(message);
        errorMsgVisible(label,true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    label.setText("");
                    errorMsgVisible(label,false);
                });
            }
        }, 10000);
    }

    @FXML
    void changeQuantityQuotient(){
        if (ILogic.isNullOrEmpty(orderQtyTxtField.getText()) || !ILogic.isNumeric(orderQtyTxtField.getText())){
            qtyQuotient = 1;
        } else {
            double qty = Double.parseDouble(orderQtyTxtField.getText());
            int integerPart = (int) qty;
            qtyQuotient = integerPart;
        }
    }

    //*********** FXML elements
    @FXML
    private TableColumn<Map.Entry<Product, Double>, Double> RequiredItemsQtyTableCol;

    @FXML
    private TableColumn<Map.Entry<Product, Double>, Integer> ReqProdIdColumn;

    @FXML
    private TableColumn<Map.Entry<Product, Double>, String> reqItemsTableColumn;


    @FXML
    private TableView<Map.Entry<Product, Double>> requiredRawMaterialTable;

    @FXML
    private Label errorMessage;

    @FXML
    private TableColumn<Map.Entry<Product, Double>, String> reqItemsUnitTableColumn;

    @FXML
    private ListView<String> listCraftableItems;

    @FXML
    private TextField orderQtyTxtField;

    @FXML
    private TreeView<String> requiredItemsTreeView;

    @FXML
    private Label lblOrderPlaced;

}
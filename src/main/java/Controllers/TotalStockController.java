package Controllers;

import Logic.ProductInfoLogic;
import Logic.model.Product;
import Logic.model.Stock;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TotalStockController {

    private List<Map<Product, Double>> data;

    private void refreshData(){
        try {
            data = ProductInfoLogic.getProductAndStock();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshTable(){

        stockTable.getItems().clear();

        ObservableList<Map.Entry<Product, Double>> items = FXCollections.observableArrayList();
        for (Map<Product, Double> map : data) {
            items.addAll(map.entrySet());
        }

        stockTable.setItems(items);

    }

    public void initialize(){

        // Binding values
        itemIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getKey().getId()).asObject());
        prodNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getProdName()));
        qtyColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValue()).asObject());
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getType()));
        unitColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getUnit()));
        isActiveColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getKey().isActive()));

        refreshData();
        refreshTable();

    }

    @FXML
    private TableView<Map.Entry<Product, Double>> stockTable;

    @FXML
    private TableColumn<Map.Entry<Product, Double>, Boolean> isActiveColumn;

    @FXML
    private TableColumn<Map.Entry<Product, Double>, Integer> itemIdColumn;

    @FXML
    private TableColumn<Map.Entry<Product, Double>, String> prodNameColumn;

    @FXML
    private TableColumn<Map.Entry<Product, Double>, Double> qtyColumn;


    @FXML
    private TableColumn<Map.Entry<Product, Double>, String> typeColumn;

    @FXML
    private TableColumn<Map.Entry<Product, Double>, String> unitColumn;


}


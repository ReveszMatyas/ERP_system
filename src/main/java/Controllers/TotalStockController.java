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
import org.tinylog.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TotalStockController {

    private void refreshTable(){

        stockTable.getItems().clear();

        ObservableList<Map.Entry<Product, Double>> items = FXCollections.observableArrayList();
        try {
            for (Map<Product, Double> map : ProductInfoLogic.getProductAndStock()) {
                items.addAll(map.entrySet());
            }
        } catch (IOException e) {
            Logger.error("An error occurred while trying to add products and stock to table. Please check the method call and the repo.", e);
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


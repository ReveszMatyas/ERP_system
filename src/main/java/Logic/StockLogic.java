package Logic;

import DB.JacksonIO;
import Logic.model.Product;
import Logic.model.Stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class StockLogic {
    static final Class<Stock> clazz = Stock.class;
    static final String path = "src/main/resources/stock.json";

    private static List<Stock> stocks;

    static {
        try {
            stocks = getAllStock();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Stock> getStock() {
        return stocks;
    }

    static ArrayList<Stock> getAllStock() throws IOException {
        return (ArrayList<Stock>) JacksonIO.getObjectListFromJson(path, clazz);
    }

    public static void addNewStock(Stock stock) throws IOException {
        ILogic.addObjectToRepo(path, stock);
        refreshStockCache();
    }

    static void updateStockRepo() throws IOException {
        ILogic.updateRepo(path, stocks);
    }


    public static void addStockToItem(int id, double qty) throws IOException {
        stocks.stream()
                .filter(stock -> stock.getId() == id)
                .forEach(stock -> {
                    stock.setQty(stock.getQty() + qty);
                });
        updateStockRepo();
        refreshStockCache();
    }

    static void refreshStockCache() throws IOException {
        stocks = getAllStock();
    }

    public static Stock getStockById(int id) {
        return stocks.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(new Stock(id, 0));
    }

    public static boolean hasEnoughStock(int id, double requiredQuantity) {
        double currentStock = getStockById(id).getQty();
        if (currentStock >= requiredQuantity){
            return true;
        }
        System.out.println(ProductInfoLogic.getProductById(id).getProdName() + " required: " + requiredQuantity + " current: " + currentStock);
        return false;
    }

    public static boolean isCraftingPossible(Map<Product, Double> requirements, int manufactureQuotient) {
        for (Map.Entry<Product, Double> entry : requirements.entrySet()){
            if (!hasEnoughStock(entry.getKey().getId(), entry.getValue() * manufactureQuotient)){
                return false;
            }
        }
        return true;
    }
}

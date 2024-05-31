package Logic;

import DB.JacksonIO;
import Logic.model.Product;
import Logic.model.Stock;
import org.tinylog.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * StockLogic is an abstract class that provides methods for handling stock data.
 * The stock data is loaded from a JSON file and deserialized into a list of Stock objects.
 */
public abstract class StockLogic {
    /**
     * The Class object representing the Stock POJO class.
     */
    static final Class<Stock> clazz = Stock.class;

    /**
     * The path to the JSON file containing the stock data.
     */
    static final String path = "src/main/resources/stock.json";

    /**
     * A list of Stock objects representing the stock data. Used as a cache to speed up query.
     */
    private static List<Stock> stocks;

    /**
     * Static initializer block that loads the stock data from the JSON file.
     * If an IOException occurs during loading, it is wrapped in a RuntimeException and thrown.
     */
    static {
        try {
            stocks = getAllStock();
        } catch (IOException e) {
            Logger.error(MessageFormat.format("An error occurred while trying to get the stock cache from path {0}. Please check if repo exists, and that it is accessible.", path));
            Logger.error(e.getMessage());
            Logger.error(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }
    /**
     * Returns the list of Stock objects representing the stock data.
     *
     * @return a list of Stock objects. If the stock cache is empty, returns an empty list.
     */
    public static List<Stock> getStock() {
        return stocks;
    }

    /**
     * Retrieves all stock items from the JSON file.
     *
     * @return a list of {@link Stock} objects.
     * @throws IOException if there is an issue reading the JSON file.
     */
    static List<Stock> getAllStock() throws IOException {
        return JacksonIO.getObjectListFromJson(path, clazz);
    }


    /**
     * Adds a new stock item to the repository and refreshes the stock cache.
     *
     * @param stock the Stock object to add.
     * @throws IOException if there is an issue writing to the JSON file.
     */
    public static void addNewStock(Stock stock) throws IOException {
        ILogic.addObjectToRepo(path, stock);
        refreshStockCache();
    }
    /**
     * Updates the stock repository with the current list of stocks.
     *
     * @throws IOException if there is an issue writing to the JSON file.
     */
    static void updateStockRepo() throws IOException {
        ILogic.updateRepo(path, stocks);
    }

    /**
     * Adds a specified quantity to the stock of a specific item by its ID.
     *
     * @param id  the ID of the stock item to update.
     * @param qty the quantity to add to the stock item.
     * @throws IOException if there is an issue updating the stock repository.
     */
    public static void addStockToItem(int id, double qty) throws IOException {
        stocks.stream()
                .filter(stock -> stock.getId() == id)
                .forEach(stock -> {
                    stock.setQty(stock.getQty() + qty);
                });
        updateStockRepo();
        refreshStockCache();
    }

    /**
     * Refreshes the stock cache by reloading the stock data from the JSON file.
     *
     * @throws IOException if there is an issue reading the JSON file.
     */
    static void refreshStockCache() throws IOException {
        stocks = getAllStock();
    }
    /**
     * Retrieves a stock item by its ID.
     *
     * @param id the ID of the stock item to retrieve.
     * @return the Stock object with the specified ID, or a new Stock object with quantity 0 if not found.
     */
    public static Stock getStockById(int id) {
        return stocks.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(new Stock(id, 0));
    }

    /**
     * Checks if there is enough stock of a specific item by its ID.
     *
     * @param id the ID of the stock item to check.
     * @param requiredQuantity the required quantity of the stock item.
     * @return true if the current stock is greater than or equal to the required quantity, false otherwise.
     */
    public static boolean hasEnoughStock(int id, double requiredQuantity) {
        double currentStock = getStockById(id).getQty();
        if (currentStock >= requiredQuantity){
            return true;
        }
        //System.out.println(ProductInfoLogic.getProductById(id).getProdName() + " required: " + requiredQuantity + " current: " + currentStock);
        return false;
    }
    /**
     * Checks if Manufacture is possible given the requirements and manufacture quotient i.e. how many is needed from the base item.
     *
     * @param requirements a map of products and their required quantities.
     * @param manufactureQuotient the multiplier for the required quantities.
     * @return true if there is enough stock for all required products, false otherwise.
     */
    public static boolean isManufacturePossible(Map<Product, Double> requirements, int manufactureQuotient) {
        for (Map.Entry<Product, Double> entry : requirements.entrySet()){
            if (!hasEnoughStock(entry.getKey().getId(), entry.getValue() * manufactureQuotient)){
                return false;
            }
        }
        return true;
    }
}

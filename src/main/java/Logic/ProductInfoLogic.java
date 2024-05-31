package Logic;

import DB.JacksonIO;
import Logic.exceptions.ProductWithoutIdException;
import Logic.model.Product;
import org.tinylog.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Predicate;


/**
 * This class collects all methods regarding general product information.
 * It provides methods to access and manipulate product data loaded from a JSON file.
 */
public abstract class ProductInfoLogic{
    /**
     * The class type (POJO) of the product.
     */
    static final Class<Product> clazz = Product.class;
    /**
     * The path to the JSON file containing product information.
     */
    public static final String path = "src/main/resources/product_info.json"; // external config file?

    /**
     * A list of all products loaded from the JSON file. Acts as a memory cache to improve speed
     * (no need to access the repo for every query).
     */
    private static List<Product> products;


    static {
        try {
            products = getAllProductInfo();
        } catch (IOException e) {
            Logger.error(MessageFormat.format("An error occurred while trying to get the product cache from path {0}. Please check if repo exists, and that it is accessible.", path));
            Logger.error(e.getMessage());
            Logger.error(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the list of products.
     *
     * @return a list of {@link Product} objects.
     */
    public static List<Product> getProducts(){
        return products;
    }

    /**
     * Reads all product information from the JSON file and returns a list of products.
     * This method is called during the static initialization block, so that product info is
     * stored in the products cache.
     *
     * @return an {@link ArrayList} of {@link Product} objects.
     * @throws IOException if an I/O error occurs during reading the JSON file.
     */
    private static ArrayList<Product> getAllProductInfo() throws IOException {
        return (ArrayList<Product>) JacksonIO.getObjectListFromJson(path, clazz);
    }


    /**
     * Adds a new product to the list product cache and updates the repository.
     *
     * @param product the {@link Product} to be added.
     * @throws IOException if an I/O error occurs during the update of the repository.
     * @throws RuntimeException if the product ID is equal to {@link Integer#MAX_VALUE}.
     */
    public static void addNewProductInfo(Product product) throws IOException, ProductWithoutIdException {
        if (product.getId() == Integer.MAX_VALUE)
            throw new ProductWithoutIdException("The adding of new product was not successful, as the generated product has no id. Please revise the product id generation method.");
        products.add(product);
        ILogic.updateRepo(path, products);
    }

    /**
     * Retrieves a product by its product name.
     *
     * @param prodName the name of the product to retrieve.
     * @return the {@link Product} with the specified name, or {@code null} if not found.
     */
    public static Product getProductByProdName(String prodName){
        return getFirstItemByAttribute((prod -> (prod.getProdName().equals(prodName))));
    }
    /**
     * Retrieves a list of products that match the specified predicate.
     *
     * @param predicate the {@link Predicate} used to filter products.
     * @return a list of {@link Product} objects that match the predicate; if none, then returns an empty list.
     */
    public static List<Product> getItemsByAttributeAsList(Predicate<Product> predicate){
        return products.stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Retrieves the first product that matches the specified predicate.
     *
     * @param predicate the {@link Predicate} used to filter products.
     * @return the first {@link Product} that matches the predicate, or {@code null} if none match.
     */
    public static Product getFirstItemByAttribute(Predicate<Product> predicate){

        List<Product> validProds = getItemsByAttributeAsList(predicate);

        if (validProds.size() == 0)
            return null;

        return getItemsByAttributeAsList(predicate).get(0);
    }
    /**
     * Retrieves a list of maps containing products and their corresponding stock quantities.
     *
     * @return a list of maps where each map contains a {@link Product} as the key and its stock quantity as the value.
     * @throws IOException if an I/O error occurs during the retrieval of stock information.
     */
    public static List<Map<Product, Double>> getProductAndStock() throws IOException {
        List<Map<Product, Double>> info = new ArrayList<>();

        products.stream().forEach(item -> {
            Map<Product, Double> productStockMap = new HashMap<>();
            double stockQty = StockLogic.getStockById(item.getId()).getQty();
            productStockMap.put(item,stockQty);
            info.add(productStockMap);
        });

        return info;
    }
    /**
     * Retrieves a list of distinct unit types from the products.
     *
     * @return a list of distinct unit types as strings.
     */
    public static List<String> getUnitsDistinct(){
        return products.stream()
                .map(Product::getUnit)
                .distinct()
                .toList();
    }

    /**
     * Retrieves a list of distinct product types.
     *
     * @return a list of distinct product types as strings.
     */
    public static List<String> getTypesDistinct(){
        return products.stream()
                .map(Product::getType)
                .distinct()
                .toList();
    }

    /**
     * Generates a new unique product ID.
     *
     * @return a new unique product ID, that is 1 higher, then the current maximum. If there are no id-s, {@link Integer#MAX_VALUE}.
     */
    public static int getNewItemId(){
        int maxId = products.stream()
                    .map(Product::getId)
                    .max(Comparator.naturalOrder())
                    .orElse(Integer.MAX_VALUE - 1);

        return maxId + 1;

    }
    /**
     * Retrieves a list of all products, that have a recipe associated with it.
     *
     * @return a list of all craftable products, or {@code null} if none are found.
     */
    public static List<Product> getAllCraftableProducts(){
        List<Product> craftables = products.stream()
                .filter(prod -> RecipeLogic.getFirstRecipebyId(prod.getId()) != null)
                .toList();

        if (craftables.size() == 0){
            return null;
        }
        return craftables;

    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve.
     * @return the {@link Product} with the specified ID, or {@code null} if the ID is {@link Integer#MAX_VALUE} or if no product is found.
     */
    public static Product getProductById(int id){
        if (id == Integer.MAX_VALUE)
            return null;

        return getFirstItemByAttribute(prod -> (prod.getId() == id));
    }
    /**
     * Retrieves the ID of a product by its name.
     *
     * @param prodName the name of the product.
     * @return the ID of the product with the specified name, or {@link Integer#MAX_VALUE} if no product is found.
     */
    public static int getIdByProdName(String prodName) {
        Product prod = getProductByProdName(prodName);

        if (prod == null)
            return Integer.MAX_VALUE;

        return getProductByProdName(prodName).getId();
    }
}

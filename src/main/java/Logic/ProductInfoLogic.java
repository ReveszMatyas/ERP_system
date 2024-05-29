package Logic;

import DB.JacksonIO;
import Logic.model.Product;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public abstract class ProductInfoLogic implements ILogic{
    static final Class<Product> clazz = Product.class;
    static final String path = "src/main/resources/product_info.json";

    private static List<Product> products;



    static {
        try {
            products = getAllProductInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Product> getProducts(){
        return products;
    }

    private static ArrayList<Product> getAllProductInfo() throws IOException {
        return (ArrayList<Product>) JacksonIO.getObjectListFromJson(path, clazz);
    }

    public static void addNewProductInfo(Product product) throws IOException {
        if (product.getId() == Integer.MAX_VALUE)
            throw new RuntimeException(); // TODO: add specific exception
        products.add(product);
        ILogic.updateRepo(path, products);
    }

    public static Product getProductByProdName(String prodName){
        return getFirstItemByAttribute((prod -> (prod.getProdName().equals(prodName))));
    }

    public static List<Product> getItemsByAttributeAsList(Predicate<Product> predicate){
        return products.stream()
                .filter(predicate)
                .toList();
    }

    public static Product getFirstItemByAttribute(Predicate<Product> predicate){

        List<Product> validProds = getItemsByAttributeAsList(predicate);

        if (validProds.size() == 0)
            return null;

        return getItemsByAttributeAsList(predicate).get(0);
    }

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

    public static List<String> getUnitsDistinct(){
        return products.stream()
                .map(Product::getUnit)
                .distinct()
                .toList();
    }

    public static List<String> getTypesDistinct(){
        return products.stream()
                .map(Product::getType)
                .distinct()
                .toList();
    }

    public static int getNewItemId(){
        int maxId = products.stream()
                    .map(Product::getId)
                    .max(Comparator.naturalOrder())
                    .orElse(Integer.MAX_VALUE - 1);

        return maxId + 1;

    }

    public static List<Product> getAllCraftableProducts(){
        List<Product> craftables = products.stream()
                .filter(prod -> RecipeLogic.getFirstRecipebyId(prod.getId()) != null)
                .toList();

        if (craftables.size() == 0){
            return null;
        }
        return craftables;

    }

    public static Product getProductById(int id){
        if (id == Integer.MAX_VALUE)
            return null;

        return getFirstItemByAttribute(prod -> (prod.getId() == id));
    }

    static int getIdByProdName(String prodName) {
        Product prod = getProductByProdName(prodName);

        if (prod == null)
            return Integer.MAX_VALUE;

        return getProductByProdName(prodName).getId();
    }

}

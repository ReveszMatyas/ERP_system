package Logic;

import Logic.model.Recipe;
import org.tinylog.Logger;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * An abstract class that provides logic for the Recipe POJO.
 */
public abstract class RecipeLogic{
    /**
     * The class type (POJO) of the recipe.
     */
    static final Class<Recipe> clazz = Recipe.class;
    /**
     * The path to the JSON file containing the recipes.
     */
    static final String path = "src/main/resources/recipes.json";

    /**
     * A list of all recipes loaded from the JSON file. Acts as a memory cache to improve speed
     * (no need to access the repo for every query).
     */
    private static List<Recipe> recipes;

    static {
        try {
            recipes = getAllRecipes();
        } catch (IOException e) {
            Logger.error(MessageFormat.format("An error occurred while trying to get the recipe cache from path {0}. Please check if repo exists, and that it is accessible.", path));
            Logger.error(e.getMessage());
            Logger.error(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }


    /**
     * Reads all recipe information from the JSON file and returns a list of recipes.
     * This method is called during the static initialization block, so that recipes are
     * stored in the recipes cache.
     *
     * @return a list of {@link Recipe} objects.
     */
    private static List<Recipe> getAllRecipes() throws IOException {
        return ILogic.getAllFromRepoAsList(path, clazz);
    }

    /**
     * Adds a new Recipe  repository and refreshes the stock cache.
     *
     * @param recipe the Recipe object to add.
     * @throws IOException if there is an issue writing to the JSON file.
     */
    public static void addNewRecipe(Recipe recipe) throws IOException {
        recipes.add(recipe);
        ILogic.updateRepo(path, recipes);
    }

    /**
     * Returns a list of {@link Recipe} objects by id.
     * @param Id the id of the product we need the recipe for
     * @return a list of {@link Recipe} objects
     */
    public static List<Recipe> getRecipebyId(int Id) {
        if (Id == Integer.MAX_VALUE){
            return null;
        }
        return recipes.stream()
                .filter(rec -> rec.getProdId() == Id)
                .toList();
    }
    /**
     * Returns a {@link Recipe} object by id. If there are more recipes available,
     * only the first is returned. If there is no available recipe, returns null.
     * @param id the id of the product we need the recipe for
     * @return a {@link Recipe} object
     */
    public static Recipe getFirstRecipebyId(int id) {

        List<Recipe> recipes = getRecipebyId(id);

        if (recipes.size() == 0){
            return null;
        }
        return recipes.get(0);
    }

    /**
     * Returns a {@link Recipe} object, by product name.
     * @param prodName the product name as string.
     * @return a {@link Recipe} object. If no recipe exists for the given name, returns null.
     */
    public static Recipe getRecipeByProductName(String prodName){
        if (ProductInfoLogic.getIdByProdName(prodName) != Integer.MAX_VALUE)
            return getFirstRecipebyId(ProductInfoLogic.getIdByProdName(prodName));
        return null;
    }
}

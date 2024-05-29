package Logic;

import Logic.model.Product;
import Logic.model.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class RecipeLogic{
    static final Class<Recipe> clazz = Recipe.class;
    static final String path = "src/main/resources/recipes.json";

    private static List<Recipe> recipes;

    static {
        try {
            recipes = getAllRecipes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Recipe> getRecipes(){
        return recipes;
    }

    private static List<Recipe> getAllRecipes() throws IOException {
        return ILogic.getAllFromRepoAsList(path, clazz);
    }

    public static void addNewRecipe(Recipe recipe) throws IOException {
        recipes.add(recipe);
        ILogic.updateRepo(path, recipes);
    }

    static List<Recipe> getRecipebyId(int Id) {

        return recipes.stream()
                .filter(rec -> rec.getProdId() == Id)
                .toList();
    }

    public static Recipe getFirstRecipebyId(int id) {

        List<Recipe> recipes = getRecipebyId(id);

        if (recipes.size() == 0){
            return null;
        }
        return recipes.get(0);
    }

    public static Recipe getRecipeByProductName(String prodName){
        if (ProductInfoLogic.getIdByProdName(prodName) != Integer.MAX_VALUE)
            return getFirstRecipebyId(ProductInfoLogic.getIdByProdName(prodName));
        return null;
    }
}

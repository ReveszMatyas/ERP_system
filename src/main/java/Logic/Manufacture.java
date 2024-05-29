package Logic;

import Logic.model.Product;
import Logic.model.Recipe;

import java.io.IOException;
import java.util.Map;

public abstract class Manufacture {

    public static void manufactureProduct(int id, Number qty) throws IOException {
        Recipe recipe = RecipeLogic.getFirstRecipebyId(id);

        if (recipe == null){
             return;
        }

        for (Map.Entry<String, Number> requirement : recipe.getRequiredProducts().entrySet())
        {
            int prodId = Integer.parseInt(requirement.getKey());
            Number reqQty = requirement.getValue();
            manufactureProduct(prodId, reqQty.doubleValue() * qty.doubleValue());
            StockLogic.addStockToItem(prodId, -reqQty.doubleValue() * qty.doubleValue());

        }

         StockLogic.addStockToItem(id, qty.doubleValue());

    }
}

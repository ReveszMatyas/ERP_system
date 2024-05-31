package Logic;

import Logic.model.Recipe;
import org.tinylog.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

public abstract class Manufacture {

    /**
     * Method to manufacture a specific product in a specific quantity. It uses recursive
     * algorithm to manufacture all the required items, before manufacturing the requested items.
     * It works reading into the repositories and writing to them.
     *
     * @param id the id if the product to manufacture.
     * @param qty the quantity of products required.
     * @throws IOException if an error occurred while trying to write to the repo
     */
    public static void manufactureProduct(int id, Number qty){
        // Todo: if craftable requirements are already in the stocks repo, not to manufacture it.
        // Todo: reafactoring required

        Recipe recipe = RecipeLogic.getFirstRecipebyId(id);

        // if the parent item does not have requirements (so raw material), no further manufacturing is needed.
        if (recipe == null){
             return;
        }

        // loop of requirments
        for (Map.Entry<String, Number> requirement : recipe.getRequiredProducts().entrySet())
        {
            int reqId = Integer.parseInt(requirement.getKey());
            Number reqQty = requirement.getValue();
            // recursive call to manufacture requirement
            manufactureProduct(reqId, reqQty.doubleValue() * qty.doubleValue());

            // when requirement crafted, remove them, as it will be used up in the last line
            try {
                StockLogic.addStockToItem(reqId, -reqQty.doubleValue() * qty.doubleValue());
            }
            catch (IOException e){
                String str = MessageFormat.format("An error occurred while trying to manufacture item:{0} in quantity: {1} while and removing the prerequisite items name:{3} qty:{4}.", ProductInfoLogic.getProductById(id).getProdName(), qty, ProductInfoLogic.getProductById(reqId).getProdName(), -reqQty.doubleValue() * qty.doubleValue());
                Logger.error(str);
            }

        }
        // actually adding the manufacture products to stock.
        try {
            StockLogic.addStockToItem(id, qty.doubleValue());
        } catch (IOException e){
            String str = MessageFormat.format("An error occurred while trying to manufacture item:{0} in quantity: {1}.", ProductInfoLogic.getProductById(id).getProdName(), qty);
            Logger.error(str);
        }
        Logger.info(MessageFormat.format("The crafting of id:{0} in qty:{1} was successful.", id, qty));
    }
}

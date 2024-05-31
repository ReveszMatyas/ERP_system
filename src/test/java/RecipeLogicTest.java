import Logic.RecipeLogic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeLogicTest {
    @Test
    void getRecipeByIdIsNullWhenIdIsMAXINT(){
        assertEquals(RecipeLogic.getRecipebyId(Integer.MAX_VALUE), null);
    }
    @Test
    void getRecipeByIdIsEmptyListWhenIdIsZero(){
            assert(RecipeLogic.getRecipebyId(0).isEmpty());
    }

    // TODO further testing

}
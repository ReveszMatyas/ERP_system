import Logic.RecipeLogic;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RecipeLogicTest {
    // TODO further testing

    @Test
    void isRecipeCacheNotEmpty(){
        assertFalse(RecipeLogic.getRecipes().isEmpty());
    }

    @Test
    void getRecipeByIdIsNullWhenIdIsMAXINT(){
        assertNull(RecipeLogic.getRecipebyId(Integer.MAX_VALUE));
    }

    @Test
    void getRecipeByIdIsEmptyListWhenIdIsZero(){
        assert(Objects.requireNonNull(RecipeLogic.getRecipebyId(0)).isEmpty());
    }

    @Test
    void getFirstRecipeByIdIsNullWhenIdIsMAXINT(){
        assertNull(RecipeLogic.getFirstRecipebyId(Integer.MAX_VALUE));
    }

    @Test
    void getFirstRecipeByIdIsEmptyListWhenIdIsZero(){
        assertNull(RecipeLogic.getFirstRecipebyId(0));
    }




}
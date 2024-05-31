import Logic.StockLogic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockLogicTest {
    @Test
    void isStockCacheNotEmpty(){
        assertFalse(StockLogic.getStock().isEmpty());
    }

    @Test
    void getStockByIdReturnsZeroIfIdMaxInt(){
        assertEquals(StockLogic.getStockById(Integer.MAX_VALUE).getQty(), 0);
    }

    @Test
    void getStockByIdReturnsZeroIfIdIsZero(){
        assertEquals(StockLogic.getStockById(0).getQty(), 0);
    }

    @Test
    void hasEnoughStockReturnsFalseIfIdZero(){
        assert(!StockLogic.hasEnoughStock(0, 1));
    }

    @Test
    void hasEnoughStockReturnsFalseIfIdMAXVAL(){
        assert(!StockLogic.hasEnoughStock(Integer.MAX_VALUE, 1));
    }

    // TODO: further testing

}
import Logic.ProductInfoLogic;
import Logic.exceptions.ProductWithoutIdException;
import Logic.model.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductInfoLogicTest {

    /*
    @Mock
    private List<Product> products;
    */

    @Test
    void isProductCacheNotEmpty(){
        assertTrue(!ProductInfoLogic.getProducts().isEmpty(), "The cache array should not be empty.");
    }

    @Test
    void isProductCacheNotNull(){
        assertNotEquals(ProductInfoLogic.getProducts(), null, "The cache array should not be null.");
    }

    @Test
    void getNewItemIdIsNotMAXVAL(){
        assertNotEquals(ProductInfoLogic.getNewItemId(), Integer.MAX_VALUE);
    }

    @Test
    void getProductByIdwithMAXVALisNull(){
        assertEquals(ProductInfoLogic.getProductById(Integer.MAX_VALUE), null);
    }

    @Test
    void getProductByIdwithzeroisNull(){
        assertEquals(ProductInfoLogic.getProductById(Integer.MAX_VALUE), null);
    }

    @Test
    void getIdByProdNameIsMAXVALIfProdNameNull(){
        assertEquals(ProductInfoLogic.getIdByProdName(null), Integer.MAX_VALUE);
    }

    @Test
    void getIdByProdNameIsMAXVALIfProdNameisEmptyString(){
        assertEquals(ProductInfoLogic.getIdByProdName(""), Integer.MAX_VALUE);
    }

    @Test
    void getTypesDistinctIsNotEmpty(){
        assert(!ProductInfoLogic.getTypesDistinct().isEmpty());
    }

    @Test
    void getUnitsDistinctIsNotEmpty(){
        assert(!ProductInfoLogic.getUnitsDistinct().isEmpty());
    }

    @Test
    void addNewProdInfoWithIntegerMaxValueThrowsException(){
        assertThrows(ProductWithoutIdException.class, () -> {
            ProductInfoLogic.addNewProductInfo(new Product(Integer.MAX_VALUE, "test", "test", "test", true));
        });
    }

    // TODO: IsnullOrempty testing
}
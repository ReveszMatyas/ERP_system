package Logic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    /**
     * The id of the product
     */
    int id;
    /**
     * The available quantity on stock
     */
    double qty;
}

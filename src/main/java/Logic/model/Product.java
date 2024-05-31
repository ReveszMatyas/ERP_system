package Logic.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

/**
 * Represents a product with its details.
 * This class is used to deserialize JSON data from external sources.
 * It contains product details such as id, name, description, and price.
 */
public class Product {

    @JsonProperty
    private int id;
    @JsonProperty("prod_name")
    private String prodName;
    @JsonProperty
    private String type;
    @JsonProperty
    private String unit;
    @JsonProperty
    private boolean isActive;
}

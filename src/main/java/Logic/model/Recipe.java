package Logic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data

/**
 * Represents a recipe of a product with its details. (POJO)
 * This class is used to deserialize JSON data from external sources.
 * It contains product details such as id, requirements, and the lead time of the manufacture.
 */
public class Recipe {
    /**
     * prodId acts as a key, that enables the join of the product and the recipe.
     */
    @JsonProperty
    int prodId;

    /**
     * requiredProducts is a Map of String and Number.
     * The first one is the String representation of the required product,
     * and the Number is the required quantity.
     */
    @JsonProperty
    Map<String, Number> requiredProducts;

    /**
     * leadTime is an integer, that is the manufacture's lead time in seconds.
     */
    @JsonProperty("lead_time_in_secs")
    int leadTime;
}

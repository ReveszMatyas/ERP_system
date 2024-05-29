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
public class Recipe {
    @JsonProperty
    int prodId;

    @JsonProperty
    Map<String, Number> requiredProducts;

    @JsonProperty("lead_time_in_secs")
    int leadTime;
}

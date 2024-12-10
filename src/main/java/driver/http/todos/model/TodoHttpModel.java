package driver.http.todos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoHttpModel {
    @JsonProperty(required = true)
    private BigInteger id;
    @JsonProperty(required = true)
    private String text;
    @JsonProperty(required = true)
    private Boolean completed;
}

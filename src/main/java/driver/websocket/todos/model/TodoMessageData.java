package driver.websocket.todos.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TodoMessageData {
    private BigInteger id;
    private String text;
    private Boolean completed;
}

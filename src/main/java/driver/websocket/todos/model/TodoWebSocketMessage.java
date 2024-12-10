package driver.websocket.todos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoWebSocketMessage {
    private TodoMessageData data;
    private TodoMessageType type;
}

package driver.websocket.todos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import driver.websocket.WebSocketMessageReceiver;
import driver.websocket.todos.model.TodoMessageType;
import driver.websocket.todos.model.TodoWebSocketMessage;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.List;

public class TodoWebSocketMessageReader {

    private final WebSocketMessageReceiver webSocketMessageReceiver;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public TodoWebSocketMessageReader(URI uri, ObjectMapper objectMapper) {
        this.webSocketMessageReceiver = new WebSocketMessageReceiver(uri);
        this.objectMapper = objectMapper;
    }

    public void startRead() {
        webSocketMessageReceiver.connect();
    }

    public void close() {
        webSocketMessageReceiver.close();
    }

    public List<TodoWebSocketMessage> getAllMessages() {
        return webSocketMessageReceiver.getRawMessages().stream().map(rm -> {
            try {
                return objectMapper.readValue(rm, TodoWebSocketMessage.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public List<TodoWebSocketMessage> getNewTodoMessages() {
        return getAllMessages().stream()
                .filter(m -> TodoMessageType.NEW_TODO.equals(m.getType()))
                .toList();
    }
}

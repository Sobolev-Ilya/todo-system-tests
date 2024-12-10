package driver.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestConfig;
import driver.ObjectMapperFactory;
import driver.websocket.todos.TodoWebSocketMessageReader;
import lombok.SneakyThrows;

import java.net.URI;

public class WebSocketMessageReaderFactory {

    @SneakyThrows
    public static TodoWebSocketMessageReader createTodoMessagesReader() {
        URI uri = TestConfig.webSocketUri();
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        return new TodoWebSocketMessageReader(uri, objectMapper);
    }
}

package websocket.todos;

import config.TestConfig;
import data.generators.TodoHttpModelGenerator;
import driver.http.HttpClientFactory;
import driver.http.todos.TodoHttpClient;
import driver.http.todos.model.TodoHttpModel;
import driver.websocket.WebSocketMessageReaderFactory;
import driver.websocket.todos.TodoWebSocketMessageReader;
import driver.websocket.todos.model.TodoWebSocketMessage;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class NewTodoMessageTests {

    TodoWebSocketMessageReader todoWebSocketMessageReader = WebSocketMessageReaderFactory.createTodoMessagesReader();
    TodoHttpClient todoHttpClient = HttpClientFactory.getTodoHttpClientWithoutCredentials();

    @BeforeClass
    public void connect() {
        todoWebSocketMessageReader.startRead();
    }

    @AfterClass
    public void close() {
        todoWebSocketMessageReader.close();
    }

    @Test
    public void updateReceivedWhenTodoCreated() {
        TodoHttpModel todoHttpModel = TodoHttpModelGenerator.withUniqueId();
        todoHttpClient.create(todoHttpModel).check201();

        TodoWebSocketMessage message = Awaitility
                .await("No updates received about creating todo with id " + todoHttpModel.getId())
                .atMost(TestConfig.websocketMaximumLatencyMills(), TimeUnit.MILLISECONDS)
                .until(() -> todoWebSocketMessageReader.getNewTodoMessages().stream()
                                .filter(m -> m.getData().getId().equals(todoHttpModel.getId())).findAny(),
                        Optional::isPresent
                ).get();

        assertThat(message.getData().getText())
                .as("Incorrect message text").isEqualTo(todoHttpModel.getText());
        assertThat(message.getData().getCompleted())
                .as("Incorrect completed status").isEqualTo(todoHttpModel.getCompleted());
    }

    @Test
    public void updateReceivedOnlOnesWhenTodoCreatingRequestedTwoTimes() throws InterruptedException {
        TodoHttpModel todoHttpModel = TodoHttpModelGenerator.withUniqueId();
        todoHttpClient.create(todoHttpModel).check201();
        todoHttpClient.create(todoHttpModel).check400();

        Thread.sleep(TestConfig.websocketMaximumLatencyMills());
        List<TodoWebSocketMessage> messages = todoWebSocketMessageReader.getNewTodoMessages().stream().filter(m -> m.getData().getId().equals(todoHttpModel.getId())).toList();

        Assertions.assertThat(messages).as("Should be 1 and only 1 message about todo creation").hasSize(1);
    }
}

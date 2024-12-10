package http.todos;

import data.generators.TodoHttpModelGenerator;
import driver.http.HttpClientFactory;
import driver.http.model.ResponseWrapper;
import driver.http.todos.TodoHttpClient;
import driver.http.todos.model.TodoHttpModel;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateTodoTests {

    TodoHttpClient todoHttpClient = HttpClientFactory.getTodoHttpClientWithoutCredentials();

    @Test
    public void todoCouldBeSuccessfullyCreatedWithAllFields() {
        TodoHttpModel createTodoRequestBody = TodoHttpModelGenerator.withUniqueId();

        Integer statusCode = todoHttpClient.create(createTodoRequestBody).getStatusCode();

        assertThat(statusCode)
                .as("Successful todo creation response should be with 201 status code")
                .isEqualTo(201);

        Optional<TodoHttpModel> todoFromGetRequestOpt = getTodoById(createTodoRequestBody.getId());
        assertThat(todoFromGetRequestOpt).as("Could not find todo with id " + createTodoRequestBody.getId())
                .isPresent();
        assertThat(todoFromGetRequestOpt.get()).as("ToDo created with unexpected params")
                .isEqualTo(createTodoRequestBody);
    }

    @Test
    public void creatingTodoWithExistingIdReturns400() {
        TodoHttpModel createTodoRequestBody1 = TodoHttpModelGenerator.withUniqueId();
        BigInteger id = createTodoRequestBody1.getId();
        TodoHttpModel createTodoRequestBody2 = TodoHttpModelGenerator.withUniqueId();
        createTodoRequestBody2.setId(id);

        todoHttpClient.create(createTodoRequestBody1).check201();
        Integer statusCode = todoHttpClient.create(createTodoRequestBody2).getStatusCode();

        assertThat(statusCode)
                .as("Creating todo with existing id should returns 400")
                .isEqualTo(400);
    }

    @Test
    public void creatingTodoWithoutIdNotPossible() {
        TodoHttpModel todo = TodoHttpModelGenerator.withUniqueId();
        todo.setId(null);

        ResponseWrapper<Object> response = todoHttpClient.create(todo);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
        Assertions.assertThat(response.getRawBodyResponse())
                .startsWith("Request body deserialize error: missing field `id`");
    }

    @Test
    public void creatingTodoWithoutTextNotPossible() {
        TodoHttpModel todo = TodoHttpModelGenerator.withUniqueId();
        todo.setText(null);

        ResponseWrapper<Object> response = todoHttpClient.create(todo);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
        Assertions.assertThat(response.getRawBodyResponse())
                .startsWith("Request body deserialize error: missing field `text`");
    }

    @Test
    public void creatingTodoWithoutCompletedStatusNotPossible() {
        TodoHttpModel todo = TodoHttpModelGenerator.withUniqueId();
        todo.setCompleted(null);

        ResponseWrapper<Object> response = todoHttpClient.create(todo);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
        Assertions.assertThat(response.getRawBodyResponse())
                .startsWith("Request body deserialize error: missing field `completed`");
    }

    private Optional<TodoHttpModel> getTodoById(BigInteger id) {
        return todoHttpClient.getAll().getBody().stream().filter(o -> o.getId().equals(id)).findFirst();
    }
}

package http.todos;

import data.generators.TodoHttpModelGenerator;
import data.registries.CredentialsRegistry;
import driver.http.HttpClientFactory;
import driver.http.model.HttpBasicAuthCredentials;
import driver.http.todos.TodoHttpClient;
import driver.http.todos.model.TodoHttpModel;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Optional;

public class DeleteTodoTests {

    @Test
    public void successfulTodoDeleting() {
        BigInteger todoId = createTodo();
        HttpBasicAuthCredentials adminCredentials = CredentialsRegistry.getAdminCredentials();
        TodoHttpClient todoHttpClientWithIncorrectCredentials = HttpClientFactory.getTodoHttpClient(adminCredentials);

        Integer statusCode = todoHttpClientWithIncorrectCredentials.delete(todoId).getStatusCode();

        Assertions.assertThat(statusCode)
                .as("Incorrect status when deleting with correct credentials")
                .isEqualTo(204);

        checkTodoRemoved(todoId);
    }

    @Test
    public void deletingWithoutAuthIsNotPossible() {
        BigInteger todoId = createTodo();
        TodoHttpClient todoHttpClientWithoutCredentials = HttpClientFactory.getTodoHttpClient(null);

        Integer statusCode = todoHttpClientWithoutCredentials.delete(todoId).getStatusCode();

        Assertions.assertThat(statusCode)
                .as("Incorrect status when deleting without auth")
                .isEqualTo(401);
        checkTodoExists(todoId);
    }

    @Test
    public void deletingWithIncorrectCredentialsIsNotPossible() {
        BigInteger todoId = createTodo();
        HttpBasicAuthCredentials incorrectCredentials = new HttpBasicAuthCredentials("incorrect", "creds");
        TodoHttpClient todoHttpClientWithIncorrectCredentials = HttpClientFactory.getTodoHttpClient(incorrectCredentials);

        Integer statusCode = todoHttpClientWithIncorrectCredentials.delete(todoId).getStatusCode();

        Assertions.assertThat(statusCode)
                .as("Incorrect status when deleting without auth")
                .isEqualTo(401);

        checkTodoExists(todoId);
    }

    @Test
    public void deletingNotExistingTodoReturns404() {
        BigInteger notExistingTodoId = new BigInteger(String.valueOf(System.currentTimeMillis()));
        HttpBasicAuthCredentials adminCredentials = CredentialsRegistry.getAdminCredentials();
        TodoHttpClient todoHttpClientWithIncorrectCredentials = HttpClientFactory.getTodoHttpClient(adminCredentials);

        Integer statusCode = todoHttpClientWithIncorrectCredentials.delete(notExistingTodoId).getStatusCode();

        Assertions.assertThat(statusCode)
                .as("Incorrect status when deleting with correct credentials")
                .isEqualTo(404);
    }

    private BigInteger createTodo() {
        TodoHttpClient todoHttpClient = HttpClientFactory.getTodoHttpClientWithoutCredentials();
        TodoHttpModel todoHttpModel = TodoHttpModelGenerator.withUniqueId();
        todoHttpClient.create(todoHttpModel).check201();
        return todoHttpModel.getId();
    }

    private void checkTodoExists(BigInteger id) {
        Optional<TodoHttpModel> todoHttpModelOpt = HttpClientFactory.getTodoHttpClientWithoutCredentials().getAll()
                .getBody()
                .stream().filter(o -> o.getId().equals(id)).findFirst();
        Assertions.assertThat(todoHttpModelOpt)
                .as("todo should present after unsuccessful deleting")
                .isPresent();
    }

    private void checkTodoRemoved(BigInteger id) {
        Optional<TodoHttpModel> todoHttpModelOpt = HttpClientFactory.getTodoHttpClientWithoutCredentials().getAll()
                .getBody()
                .stream().filter(o -> o.getId().equals(id)).findFirst();
        Assertions.assertThat(todoHttpModelOpt)
                .as("todo should not presents after deleting")
                .isNotPresent();
    }
}

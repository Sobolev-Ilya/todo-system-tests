package http.todos;

import data.generators.TodoHttpModelGenerator;
import driver.http.HttpClientFactory;
import driver.http.todos.TodoHttpClient;
import driver.http.todos.model.TodoHttpModel;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GetTodosTests {

    private final int MINIMUM_TODOS_COUNT_SYSTEM_HAVE = 3;
    TodoHttpClient todoHttpClient = HttpClientFactory.getTodoHttpClientWithoutCredentials();

    @BeforeClass
    public void populateSomeTodos() {
        for (int i = 0; i < MINIMUM_TODOS_COUNT_SYSTEM_HAVE; i++) {
            todoHttpClient.create(TodoHttpModelGenerator.withUniqueId());
        }
    }

    @Test
    public void gettingTodosWithoutParametersReturnsSomeTodos() {
        List<TodoHttpModel> todoList = todoHttpClient.getAll().getBody();

        Assertions.assertThat(todoList).hasSizeGreaterThanOrEqualTo(MINIMUM_TODOS_COUNT_SYSTEM_HAVE);
    }

    @Test
    public void receivedTodoListIsSortedByIdAsc() {
        List<TodoHttpModel> todoList = todoHttpClient.getAll().getBody();

        Assertions.assertThat(todoList)
                .as("ToDo list should be sorted by id ascending")
                .isSortedAccordingTo(Comparator.comparing(TodoHttpModel::getId));
    }

    @Test
    public void limitBy1ReturnsOnly1stTodo() {
        List<TodoHttpModel> allTodos = todoHttpClient.getAll().getBody();
        Assertions.assertThat(allTodos)
                .as("Test requires at least 2 todos")
                .hasSizeGreaterThanOrEqualTo(2);

        List<TodoHttpModel> todoList = todoHttpClient.get(Map.of("limit", "1")).getBody();

        Assertions.assertThat(todoList)
                .as("Request with limit=1 should return only 1 todo")
                .containsExactly(allTodos.get(0));
    }

    @Test
    public void limitByMaxLongValueReturnsSameTodosAsRequestWithoutParams() {
        List<TodoHttpModel> allTodos = todoHttpClient.getAll().getBody();
        Assertions.assertThat(allTodos)
                .as("Test requires at least 2 todos")
                .hasSizeGreaterThanOrEqualTo(2);
        long limitValue = Long.MAX_VALUE;
        List<TodoHttpModel> todoListWithMaxInteger = todoHttpClient.get(Map.of("limit", String.valueOf(limitValue))).getBody();

        Assertions.assertThat(todoListWithMaxInteger)
                .as("Request with limit=%d should return same list size as request without limit", limitValue)
                .containsExactlyElementsOf(allTodos);
    }

    @Test
    public void limitAndOffsetBy1Returns2ndTodo() {
        List<TodoHttpModel> allTodosList = todoHttpClient.getAll().getBody();
        Assertions.assertThat(allTodosList)
                .as("Test required at least 3 created todos")
                .hasSizeGreaterThanOrEqualTo(3);

        List<TodoHttpModel> limitAndOffsetBy1List = todoHttpClient.get(Map.of("limit", "1", "offset", "1")).getBody();

        Assertions.assertThat(limitAndOffsetBy1List)
                .as("limit=1&offset=1 should return only 2nd todo")
                .containsExactly(allTodosList.get(1));
    }


    /**
     * Checklist for future implementation:
     * offset=1 returns 2nd todo as first
     * offset=0 did not affect result
     * limit=0 returns empty result
     */
}

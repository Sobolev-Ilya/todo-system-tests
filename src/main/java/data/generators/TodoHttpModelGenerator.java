package data.generators;

import driver.http.todos.model.TodoHttpModel;

import java.math.BigInteger;

public class TodoHttpModelGenerator {

    public static TodoHttpModel withUniqueId() {
        return new TodoHttpModel(new BigInteger(Long.toString(System.currentTimeMillis())), "some task", false);
    }
}

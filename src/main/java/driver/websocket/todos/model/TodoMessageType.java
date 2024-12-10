package driver.websocket.todos.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TodoMessageType {
    NEW_TODO("new_todo"),
    UPDATE_TODO("update_todo"),
    DELETE_TODO("delete_todo");

    private final String type;

    TodoMessageType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}

package driver.http.todos;

import com.fasterxml.jackson.core.type.TypeReference;
import driver.http.model.HttpBasicAuthCredentials;
import driver.http.model.ResponseWrapper;
import driver.http.todos.model.TodoHttpModel;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TodoHttpClient {

    private final String backendAddress;
    private final RequestSpecBuilder requestSpecBuilder;
    private final HttpBasicAuthCredentials httpBasicAuthCredentials;

    public TodoHttpClient(String backendAddress, RequestSpecBuilder requestSpecBuilder, HttpBasicAuthCredentials httpBasicAuthCredentials) {
        this.backendAddress = backendAddress;
        this.requestSpecBuilder = requestSpecBuilder;
        this.httpBasicAuthCredentials = httpBasicAuthCredentials;
    }

    @SneakyThrows
    public ResponseWrapper<Object> create(TodoHttpModel todoHttpModel) {
        Response response = RestAssured.given().spec(requestSpecBuilder.build())
                .contentType(ContentType.JSON)
                .body(todoHttpModel)
                .post(backendAddress + "/todos");
        return new ResponseWrapper<>(response);
    }

    public ResponseWrapper<List<TodoHttpModel>> getAll() {
        return get(Collections.emptyMap());
//        Response response = RestAssured.given().spec(requestSpecBuilder.build())
//                .accept(ContentType.JSON)
//                .get(backendAddress + "/todos");
//        return new ResponseWrapper<>(response, new TypeReference<>() {
//        });
    }

    public ResponseWrapper<List<TodoHttpModel>> get(Map<String,String> queryParams) {
        Response response = RestAssured.given().spec(requestSpecBuilder.build())
                .accept(ContentType.JSON)
                .queryParams(queryParams)
                .get(backendAddress + "/todos");
        return new ResponseWrapper<>(response, new TypeReference<>() {
        });
    }

    public ResponseWrapper<Object> delete(BigInteger id) {
        RequestSpecification spec = RestAssured.given().spec(requestSpecBuilder.build());
        if (httpBasicAuthCredentials != null)
            spec.auth().preemptive().basic(httpBasicAuthCredentials.getUsername(), httpBasicAuthCredentials.getPassword());
        Response response = spec.delete(backendAddress + "/todos/" + id);

        return new ResponseWrapper<>(response);
    }
}

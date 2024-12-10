package driver.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestConfig;
import driver.ObjectMapperFactory;
import driver.http.model.HttpBasicAuthCredentials;
import driver.http.todos.TodoHttpClient;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class HttpClientFactory {

    public static TodoHttpClient getTodoHttpClient(HttpBasicAuthCredentials httpBasicAuthCredentials) {
        String backendAddress = TestConfig.backendAddress();
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .setConfig(RestAssuredConfig.config().objectMapperConfig(getRestAssuredObjectMapperConfig(objectMapper)));

        return new TodoHttpClient(backendAddress, requestSpecBuilder, httpBasicAuthCredentials);
    }

    public static TodoHttpClient getTodoHttpClientWithoutCredentials() {
        return getTodoHttpClient(null);
    }

    private static ObjectMapperConfig getRestAssuredObjectMapperConfig(ObjectMapper objectMapper) {
        return new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> objectMapper);
    }
}

package driver.http.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import driver.ObjectMapperFactory;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;

public class ResponseWrapper<T> {

    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    private final Response response;
    private TypeReference<T> typeRef;

    public ResponseWrapper(Response response, TypeReference<T> typeRef) {
        this.response = response;
        this.typeRef = typeRef;
    }

    public ResponseWrapper(Response response) {
        this.response = response;
    }

    @SneakyThrows
    public T getBody() {
        if (typeRef == null) throw new IllegalArgumentException("Response type was not specified");
        Assertions.assertThat(response.statusCode())
                .as("Negative response status code, could not map body response")
                .isLessThan(400);
        return objectMapper.readValue(response.asString(), typeRef);
    }

    public Integer getStatusCode() {
        return response.statusCode();
    }

    public String getRawBodyResponse() {
        return response.asString();
    }

    public ResponseWrapper<T> check201() {
        checkStatusCode(201);
        return this;
    }

    public ResponseWrapper<T> check400() {
        checkStatusCode(400);
        return this;
    }

    private ResponseWrapper<T> checkStatusCode(int expectedStatusCode) {
        Assertions.assertThat(getStatusCode()).as("Not expected status code").isEqualTo(expectedStatusCode);
        return this;
    }
}

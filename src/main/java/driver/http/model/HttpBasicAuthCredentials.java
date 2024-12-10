package driver.http.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpBasicAuthCredentials {

    private String username;
    private String password;

}

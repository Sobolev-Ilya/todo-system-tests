package data.registries;

import config.TestConfig;
import driver.http.model.HttpBasicAuthCredentials;

public class CredentialsRegistry {

    public static HttpBasicAuthCredentials getAdminCredentials() {
        return new HttpBasicAuthCredentials(TestConfig.adminUsername(), TestConfig.adminPassword());
    }
}

package config;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

public class TestConfig {

    private static final Properties properties = new Properties();

    static {
        String testProfile = System.getProperty("testEnv", "local");
        String configFile = "config/%s.properties".formatted(testProfile);
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new RuntimeException("Configuration file not found: config/local.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file", e);
        }
    }

    @SneakyThrows
    public static URI webSocketUri() {
        return new URI(properties.getProperty("websocket.uri"));
    }

    public static String backendAddress() {
        return properties.getProperty("backend.address");
    }

    public static int websocketMaximumLatencyMills() {
        return Integer.parseInt(properties.getProperty("websocket.maximum.latency.millis"));
    }

    public static String adminUsername() {
        return properties.getProperty("admin.username");
    }

    public static String adminPassword() {
        return properties.getProperty("admin.password");
    }
}

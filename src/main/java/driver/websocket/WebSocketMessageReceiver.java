package driver.websocket;

import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebSocketMessageReceiver extends WebSocketClient {

    protected final List<String> rawMessages = Collections.synchronizedList(new ArrayList<>());
    private final AtomicBoolean isOpen = new AtomicBoolean(false);

    public WebSocketMessageReceiver(URI uri) {
        super(uri);
    }

    @Override
    public void connect() {
        super.connect();
        isOpen.set(true);
    }

    @Override
    public void close() {
        super.close();
        isOpen.set(false);
    }

    public List<String> getRawMessages() {
        if (!isOpen.get())
            throw new IllegalArgumentException("Connection is not open or was closed.");
        return rawMessages;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("WebSocket connection opened to " + uri);
    }

    @Override
    public void onMessage(String s) {
        rawMessages.add(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("WebSocket connection closed");
    }

    @SneakyThrows
    @Override
    public void onError(Exception e) {
        throw e;
    }
}

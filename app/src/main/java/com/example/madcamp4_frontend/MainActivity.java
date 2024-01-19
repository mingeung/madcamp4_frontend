package com.example.madcamp4_frontend;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to WebSocket server
        connectWebSocket();
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://172.10.7.13:80");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        Draft[] drafts = {new Draft_6455()};
        mWebSocketClient = new WebSocketClient(uri, drafts[0]) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.d("WebSocket", "Connection opened");
                mWebSocketClient.send("Hello, Server!"); // Sending a message
            }

            @Override
            public void onMessage(String message) {
                Log.d("WebSocket", "Received message: " + message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.d("WebSocket", "Connection closed");
            }

            @Override
            public void onError(Exception e) {
                Log.e("WebSocket", "Error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        };
        mWebSocketClient.connect();
    }
}

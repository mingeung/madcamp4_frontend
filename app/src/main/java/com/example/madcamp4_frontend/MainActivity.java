package com.example.madcamp4_frontend;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to Socket.IO server
        new ConnectSocketIOAsyncTask().execute();
    }
    private class ConnectSocketIOAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            connectSocketIO();
            return null;
        }
    }

    private void connectSocketIO() {
        try {
            mSocket = IO.socket("http://172.10.7.13:80"); // Socket.IO 서버 주소로 변경
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mSocket.on(Socket.EVENT_CONNECT, args -> {
            Log.d("Socket.IO", "Connected");
            mSocket.emit("message", "Hello, Server!"); // Sending a message
        });

        mSocket.on("message", args -> {
            String message = (String) args[0];
            Log.d("Socket.IO", "Received message: " + message);

            runOnUiThread(() -> showToast("Received message: " + message));
        });

        mSocket.on(Socket.EVENT_DISCONNECT, args -> {
            Log.d("Socket.IO", "Disconnected");
        });

        mSocket.connect();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off();
        }
    }
}

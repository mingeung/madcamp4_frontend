package com.example.madcamp4_frontend;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.net.URI;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import android.os.AsyncTask;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.navigation_maps) {
                selectedFragment = new MapsFragment();
            } else if (itemId == R.id.navigation_chat) {
                selectedFragment = new ChattingFragment();
            } else if (itemId == R.id.navigation_mypage) {
                selectedFragment = new MypageFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
        // Set the default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MapsFragment())
                .commit();


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
            URI uri = new URI("http://172.10.7.13:80");
            mSocket = IO.socket(uri);
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

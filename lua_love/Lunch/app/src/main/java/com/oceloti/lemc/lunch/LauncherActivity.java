package com.oceloti.lemc.lunch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LauncherActivity extends AppCompatActivity {

  private static final String TAG = "LauncherActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_launcher);

    // Initialize the main view
    View mainView = findViewById(R.id.main);
    if (mainView != null) {
      ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
      });
    } else {
      Log.e(TAG, "View with ID 'main' not found in activity_launcher.xml");
    }

    // Initialize the Start Game button
    Button startGameButton = findViewById(R.id.button_start_game);
    if (startGameButton != null) {
      startGameButton.setOnClickListener(view -> {
        try {
          Intent intent = new Intent(LauncherActivity.this, GameActivity.class);
          startActivity(intent);
          //finish(); // Optional: Close LauncherActivity so user can't return to it
        } catch (Exception e) {
          Log.e(TAG, "Failed to launch GameActivity", e);
        }
      });
    } else {
      Log.e(TAG, "Button with ID 'button_start_game' not found in activity_launcher.xml");
    }
  }
}